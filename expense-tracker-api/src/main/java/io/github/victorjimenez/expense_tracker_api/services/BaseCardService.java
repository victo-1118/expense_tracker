package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.dto.BaseCardDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import java.util.List;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import org.springframework.data.domain.Sort;
public interface BaseCardService {
    //Create Operation
    // keep in mind this doesnt actually create a BaseCard. All this does is help credit card and debit card
    // with their create method so that I can reduce boiler plate code
    void create(BaseCardDTO baseCardDTO, BaseCard baseCard);

    //Read Operations
    List<BaseCard> findAll(Sort sort);
    List<BaseCard> findByCardProvider(String cardProvider);
    List<BaseCard> findByBalance(double balance);
    List<BaseCard> findByBalanceBetween(double minBalance, double maxBalance);
    List<BaseCard> findByBaseFeeType(String feeType);
    List<BaseCard> findByBaseFeeAmount(double feeAmount);
    List<BaseCard> findByBaseFeeAmountBetween(double minFeeAmount, double maxFeeAmount);
    List<BaseCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds);
    List<BaseCard> findByAllExpenseTypes(List<Long> expenseTypeIds, int minSize);
    List<BaseCard> findByEligibleExpenses_IdIn(List<Long> expenseIds);
    List<BaseCard> findByAllExpenses(List<Long> expenseIds, int minSize);

    //Write Operations

    void delete(String baseCardCardNumber);
    BaseCard update(BaseCardDTO baseCardDTO, BaseCard baseCard);
    void updateBalance(String baseCardNumber, Double balance);
    void addBaseFee(String baseCardNumber, String feeType, Double fee);
    void removeBaseFee(String baseCardNumber, String feeType);
    void addExpenseTypeToPayFor(BaseCard baseCard, List<Long> expenseTypeId); 
    void removeExpenseTypeToPayFor(BaseCard baseCard, List<Long> expenseTypeId, Boolean expenseDeleted);
    void removeEligibleExpenses(BaseCard baseCard, List<Long> expenseTypeId);
    void removeEligibleExpense(List<BaseCard> baseCards, Long expenseId);
    // also need to add method to remove and also make sure to manage eligible expenses again.


    //Business Logic
    void addEligibleExpenses(BaseCard baseCard, Long expenseTypeId);
    void addEligibleExpense(Expense expense, List<BaseCard> baseCards);
    void addPaymentHistory(PaymentHistory paymentHistory, BaseCard baseCard);
    void removePaymentHistory(PaymentHistory paymentHistory, BaseCard baseCard);
    
    Expense chargeFee(String baseCardNumber, String feeType, LocalDate date);
    

    
}
