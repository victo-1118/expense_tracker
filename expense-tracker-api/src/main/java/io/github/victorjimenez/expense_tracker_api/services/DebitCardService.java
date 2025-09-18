package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.warnings.BudgetWarningException;
import io.github.victorjimenez.expense_tracker_api.dto.DebitCardDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DebitCardService {
    //Create Operation
    DebitCard create(DebitCardDTO debitCardDTO);
    //Read Operations
    List<DebitCard> findAll(Sort sort);
 
    List<DebitCard> findByCardProvider(String cardProvider);
    List<DebitCard> findByBalance(double balance);
    List<DebitCard> findByBalanceBetween(double minBalance, double maxBalance);
    List<DebitCard> findByBudget(double budget);
    List<DebitCard> findByBudgetBetween(double minBudget, double maxBudget);
    List<DebitCard> findByBaseFeeType(String feeType);
    List<DebitCard> findByBaseFeeAmount(double feeAmount);
    List<DebitCard> findByBaseFeeAmountBetween(double minFeeAmount, double maxFeeAmount);
    List<DebitCard> findByDebitFeeType(String feeType);
    List<DebitCard> findByDebitFeeAmount(double feeAmount);
    List<DebitCard> findByDebitFeeAmountBetween(double minFeeAmount, double maxFeeAmount);
    List<DebitCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds);
    List<DebitCard> findByAllExpenseTypes(List<Long> expenseTypeIds);
    List<DebitCard> findByEligibleExpenses_IdIn(List<Long> expenseIds);
    List<DebitCard> findByAllExpenses(List<Long> expenseIds);

    //Write Operations
    DebitCard update(DebitCardDTO debitCardDTO);
    void delete(String cardNumber); 
    void addDebitFee(String cardNumber, String feeType, Double fee);
    void removeDebitFee(String cardNumber, String feeType);

    //Business Logic
    void transferFundsDebit(String debitCardSenderNumber, String debitCardReceiverNumber, double amount, Boolean ignoreWarning); // debitCardTransfer
    void transferFundsCredit(String debitCardSenderNumber, String creditCardReceiverNumber, double amount, Boolean ignoreWarning);
    Boolean approveExpensePaymentBalance(DebitCard debitCard, BigDecimal expenseAmount, Boolean ignoreWarning);
    Expense chargeDebitFee(String debitCardNumber, String feeType, LocalDate date);

}
