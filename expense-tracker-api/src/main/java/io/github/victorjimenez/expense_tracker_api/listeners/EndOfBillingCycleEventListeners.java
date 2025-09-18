package io.github.victorjimenez.expense_tracker_api.listeners;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.events.EndOfBillingCycleEvent;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.repository.CreditCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;
import jakarta.persistence.EntityNotFoundException;

@Component
public class EndOfBillingCycleEventListeners {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;
    @EventListener
    public Expense chargeInterest(EndOfBillingCycleEvent event) {
        CreditCard creditCard = creditCardRepository.findById(event.getCardNumber()).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        BigDecimal expenseAmount = BigDecimal.valueOf(creditCard.getBalance()).multiply(BigDecimal.valueOf(creditCard.getInterestRate()));

        ExpenseTypeEntity interestExpenseTypeEntity = expenseTypeRepository.findByExpenseType("Interest");
        LocalDate expenseDate = creditCard.getStartOfBillingCycle().plus(creditCard.getPeriodDueDateAfterCycle());

        
        ExpenseDTO interestExpense = new ExpenseDTO(null,"Interest", expenseAmount.doubleValue(), expenseDate, false, null, interestExpenseTypeEntity.getId());

        return expenseService.create(interestExpense);
    }
    @EventListener
    public Expense chargeMinimumPayment(EndOfBillingCycleEvent event) {
        CreditCard creditCard = creditCardRepository.findById(event.getCardNumber()).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        BigDecimal staticExpenseAmount = BigDecimal.valueOf(creditCard.getMinimumPayment());
        BigDecimal dynamicExpenseAmount = BigDecimal.valueOf(creditCard.getMinimumPaymentDynamic()).multiply(BigDecimal.valueOf(creditCard.getBalance()));
        BigDecimal expenseAmount;
        if (dynamicExpenseAmount.compareTo(staticExpenseAmount) > 0){
            expenseAmount = dynamicExpenseAmount;
        }
        else{
            expenseAmount = staticExpenseAmount;
        }

        ExpenseTypeEntity minimumPaymentExpenseTypeEntity = expenseTypeRepository.findByExpenseType("Minimum Payment");
        LocalDate expenseDate = creditCard.getStartOfBillingCycle().plus(creditCard.getPeriodDueDateAfterCycle());
       
        ExpenseDTO minimumPaymentExpense = new ExpenseDTO(null,"Minimum Payment", expenseAmount.doubleValue(), expenseDate, false, null, minimumPaymentExpenseTypeEntity.getId());

        return expenseService.create(minimumPaymentExpense);

    }
}
