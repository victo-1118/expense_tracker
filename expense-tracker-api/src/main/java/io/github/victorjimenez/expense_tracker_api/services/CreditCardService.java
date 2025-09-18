package io.github.victorjimenez.expense_tracker_api.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.warnings.PersonalLimitWarningException;
import org.springframework.data.domain.Sort;
import io.github.victorjimenez.expense_tracker_api.dto.CreditCardDTO;
public interface CreditCardService {
    //Create Operation
    CreditCard create(CreditCardDTO creditCardDTO);
    //Read Operations
    List<CreditCard> findAll(Sort sort);

    List<CreditCard> findByCardProvider(String cardProvider);
    List<CreditCard> findByBalance(double balance);
    List<CreditCard> findByBalanceBetween(double minBalance, double maxBalance);
    List<CreditCard> findByCreditLimit(double creditLimit);
    List<CreditCard> findByCreditLimitBetween(double minCreditLimit, double maxCreditLimit);
    List<CreditCard> findByInterestRate(double interestRate);
    List<CreditCard> findByInterestRateBetween(double minInterestRate, double maxInterestRate);
    List<CreditCard> findByMinimumPayment(double minimumPayment);
    List<CreditCard> findByMinimumPaymentBetween(double minMinimumPayment, double maxMinimumPayment);
    // How do i want to make this function?
    // the goal is to make it so that everytime the user opens the app the method to check whether a payment due date is up is run
    // How do we find all cards that are due?
    // well we would use this function. Then just loop through all credit cards and add their date by the variable
    //  that would give us the frequency of due dates. However wouldnt this be a inefficiency? IDK. 
    // I mean it is inefficient but i dont want to store any more variables and most of the times the user would not have that many cards

    List<CreditCard> findByStartOfBillingCycleAfter(LocalDate startOfBillingCycle);
    List<CreditCard> findByCreditFeeType(String feeType);
    List<CreditCard> findByCreditFeeAmount(double feeAmount);
    List<CreditCard> findByCreditFeeAmountBetween(double minFeeAmount, double maxFeeAmount);
    List<CreditCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds);
    List<CreditCard> findByAllExpenseTypes(List<Long> expenseTypeIds);
    List<CreditCard> findByEligibleExpenses_IdIn(List<Long> expenseIds);
    List<CreditCard> findByAllExpenses(List<Long> expenseIds);

    //Write Operations
    CreditCard update(CreditCardDTO creditCardDTO);
    void delete(String cardNumber);

    void addCreditFee(String cardNumber, String feeType, Double fee);
    void removeCreditFee(String cardNumber, String feeType);

    //Business Logic
    



    
}
