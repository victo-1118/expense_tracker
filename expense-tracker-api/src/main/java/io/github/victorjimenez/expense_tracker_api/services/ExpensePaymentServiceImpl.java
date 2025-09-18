package io.github.victorjimenez.expense_tracker_api.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import io.github.victorjimenez.expense_tracker_api.dto.PaymentHistoryDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.warnings.PersonalLimitWarningException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import io.github.victorjimenez.expense_tracker_api.events.ExpensePaymentProcessedEvent;
import io.github.victorjimenez.expense_tracker_api.events.NonSufficientFundsEvent;
import io.github.victorjimenez.expense_tracker_api.events.PersonalLimitWarningEvent;

public class ExpensePaymentServiceImpl implements ExpensePaymentService {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    //Business Logic
    @Override
    public Boolean approveExpensePaymentCategory(String baseCardNumber, Long expenseId) {
        BaseCard baseCard = baseCardRepository.findByCardNumber(baseCardNumber);
        Expense expense = expenseRepository.findById(expenseId).get();
        if (baseCard.getExpenseTypeToPayFor().contains(expense.getExpenseType())) {
            return true;
        }
        return false;
    }
    @Override
    @Transactional
    public void payForExpense(String baseCardNumber, Long expenseId, double amount, Boolean ignoreWarning) {

        BaseCard baseCard = baseCardRepository.findById(baseCardNumber).orElseThrow(() -> new EntityNotFoundException("Base card with card number " + baseCardNumber + " not found"));
        
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new EntityNotFoundException("Expense with id " + expenseId + " not found"));
        if (!approveExpensePaymentCategory(baseCardNumber, expenseId)){
            throw new IllegalArgumentException(baseCardNumber + " is not eligible to pay for " + expenseId + " due to incorrect category");
        }
        BigDecimal baseCardBigDecimal = BigDecimal.valueOf(baseCard.getBalance());
        BigDecimal amountBigDecimal = BigDecimal.valueOf(amount);
        BigDecimal result = baseCardBigDecimal.subtract(amountBigDecimal);
        BigDecimal newCardBalance = result.setScale(2, RoundingMode.HALF_UP);
        // Type-specific logic
        // approveExpensePaymentBalance function might have to be implemented here as helper methods
        // or just most of it in this function
        boolean isApproved = false;
        if (baseCard instanceof DebitCard) {
            DebitCard debitCard = (DebitCard) baseCard;
            isApproved = debitCardService.approveExpensePaymentBalance(debitCard, newCardBalance, ignoreWarning);
        } 
        else if (baseCard instanceof CreditCard) {
            CreditCard creditCard = (CreditCard) baseCard;
            isApproved = approveExpensePaymentCreditBalance(creditCard, newCardBalance, ignoreWarning);
        } 
        else {
            throw new IllegalArgumentException("Unsupported card type: " + baseCard.getClass().getSimpleName());
        }
        if (!isApproved) {
            return;
        }
        CompletableFuture<Boolean> userResponse = new CompletableFuture<>();

        if (!userResponse.get()) {
            throw new RuntimeException("User did not confirm the payment");
        }
        // ideally we tell user in the frontend confirm or no and it should show a  pop up. Then based on what they click
        // I send a message to complete completableFuture?
        // so the way I need to do it is in the event listener I need the event listener to call SSE it will also have completableFuture
        // I then get the response but its only in the event listeners completable future.
        // the goal is to get that completable future and send its respond to the function that sent the warning.
        // this works well and an added benefit is that this function pauses as it waits for the other completable future
        Double newBalance = newCardBalance.doubleValue();

        // from here we can assume the payment is valid
        //should publish an event here. expensePayedForEvent or something like that
        applicationEventPublisher.publishEvent(new ExpensePaymentProcessedEvent(expenseId, baseCardNumber, amount, LocalDate.now()));

        
        
        expenseService.payingExpenseAmount(amount, expense);// I am worried about this though. Well i know that this function should not be in expense service at least
        // what i probably do is at least include the function in this service somewhere whether it be here or a helper method
        expenseRepository.save(expense);

        baseCard.setBalance(newBalance);
        baseCardRepository.save(baseCard);
    }
    // lets create personal limit warning event instead of error. same with overBudget warning
    public Boolean approveExpensePaymentCreditBalance(CreditCard creditCard, BigDecimal expenseAmount, Boolean ignoreWarning)  {
        
        BigDecimal overPersonalLimit = BigDecimal.valueOf(creditCard.getPersonalLimit()).subtract(expenseAmount);
        if (BigDecimal.valueOf(creditCard.getPersonalLimit()).compareTo(expenseAmount) >= 0){

            applicationEventPublisher.publishEvent(new PersonalLimitWarningEvent(String.format("Personal Limit Warning. Personal Limit exceeded by %,.2f. Would you like to ignore this warning?", overPersonalLimit.doubleValue())));
        }
        
        if (BigDecimal.valueOf(creditCard.getBalance()).compareTo(expenseAmount) > 0){
            applicationEventPublisher.publishEvent(new NonSufficientFundsEvent("Insufficient funds.", creditCard));
        } //Similar method for debit will be made but the message would be different

        return true;
    }
}
