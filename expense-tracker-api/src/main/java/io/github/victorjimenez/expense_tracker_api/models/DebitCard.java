package io.github.victorjimenez.expense_tracker_api.models;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
 
@Entity
@DiscriminatorValue("DebitCard")
@NoArgsConstructor

public class DebitCard extends BaseCard{
    private double budget;
    @ElementCollection
    @CollectionTable(name="debit_card_fees", joinColumns = @JoinColumn(name="card_number"))
    @MapKeyColumn(name="fee_type")  
    @Column(name="debit_card_fee_amount")  
    private Map<String, Double> debitFees = new HashMap<>();

    /**
     * Constructs a debit card with the specified card type, card number, balance, and budget.
     * @param cardNumber
     * @param cardProvider
     * @param balance
     * @param budget
     * @param expenseTypeToPayFor
     */
    public DebitCard(String cardNumber, String cardProvider, double balance,  double budget){
        super(cardNumber, cardProvider, balance);
        this.budget = budget;
    }
    

    /**
     * Sets the budget to the specified amount.
     * @param budget the budget to set. Must not be below 0 and must be less than or equal to the current balance.
     */
    public void setBudget(double budget){
        this.budget = budget;
    }

    /**
     * Gets the budget of the debit card.
     * @return the budget
     */
    public double getBudget(){
        return budget;
    }

    public void addDebitFee(DebitCardFeesType feeType, double fee){
        String feeTypeString = feeType.getDisplayName();
        debitFees.put(feeTypeString, fee);
    }

    public void removeDebitFee(DebitCardFeesType feeType){
        String feeTypeString = feeType.getDisplayName();
        debitFees.remove(feeTypeString);
    }

    public double getDebitFee(DebitCardFeesType feeType){
        String feeTypeString = feeType.getDisplayName();
        return debitFees.get(feeTypeString);
    }

    public Map<String, Double> getDebitFees(){
        return debitFees;
    }
    

    /**
     * Returns a string representation of the debit card in the format:
     * "DebitCard[cardType=<cardType>, cardNumber=<cardNumber>, balance=<balance>, budget=<budget>, expenseTypeToPayFor={<expenseTypeToPayFor>}]"
     * @return a string representation of the debit card
     */
    @Override
    public String toString() {

        
        StringBuilder expensesIds = new StringBuilder();
        for (Expense expense : eligibleExpenses) {
            expensesIds.append(expense.getId()).append(", ");
        }
        if (!eligibleExpenses.isEmpty()){
            expensesIds.delete(expensesIds.length() - 2, expensesIds.length());
        }
        String expensesIdsString = expensesIds.toString();
        StringBuilder expenseTypeIds = new StringBuilder();
        for (ExpenseTypeEntity expenseType : expenseTypeToPayFor) {
            expenseTypeIds.append(expenseType.getId()).append(", ");
        }
        if (!expenseTypeToPayFor.isEmpty()){
            expenseTypeIds.delete(expenseTypeIds.length() - 2, expenseTypeIds.length());
        }
        String expenseTypeIdsString = expenseTypeIds.toString();
        StringBuilder paymentHistoriesIds = new StringBuilder();
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoriesIds.append(paymentHistory.getId()).append(", ");
        }
        if (!paymentHistories.isEmpty()){
           paymentHistoriesIds.delete(paymentHistoriesIds.length() - 2, paymentHistoriesIds.length());
        }
        String paymentHistoriesIdsString = paymentHistoriesIds.toString();
        String baseFeesString = baseFees.toString();
        String debitFeesString = debitFees.toString();
        
        
        return "DebitCard[" +
               "cardNumber='" + cardNumber + "', " +
               "cardProvider='" + cardProvider + "', " +
               "balance=" + balance + ", " +
               "budget=" + budget + ", " +
               "expenseTypeToPayFor=[" + expenseTypeIdsString + "]" +
               ", eligibleExpenses=[" + expensesIdsString + "]" +
                ", paymentHistories=[" + paymentHistoriesIdsString + "]" +
               ", baseFees=" + baseFeesString + "" +
               ", debitFees=" + debitFeesString + "" +
               "]";
    }

    /**
     * Compares this debit card with the given object for equality.
     * 
     * Two debit cards are equal if their card types, card numbers, balances, budgets, and expense types to pay for are all equal.
     * 
     * @param obj the given object to compare with
     * @return true if the given object is an equal debit card, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        DebitCard that = (DebitCard) obj;
        Set<Long> expenseIds = new HashSet<>();
        for (Expense expense : eligibleExpenses) {
            expenseIds.add(expense.getId());
        }
        Set<Long> thatExpenseIds = new HashSet<>();
        for (Expense thatExpense : that.eligibleExpenses) {
            thatExpenseIds.add(thatExpense.getId());
        }
        Set<Long> paymentHistoryIds = new HashSet<>();
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoryIds.add(paymentHistory.getId());
        }
        Set<Long> thatPaymentHistoryIds = new HashSet<>();
        for (PaymentHistory thatPaymentHistory : that.paymentHistories) {
            thatPaymentHistoryIds.add(thatPaymentHistory.getId());
        }
        Set<Long> expenseTypeIds = new HashSet<>();
        for (ExpenseTypeEntity expenseType : expenseTypeToPayFor) {
            expenseTypeIds.add(expenseType.getId());
        }
        Set<Long> thatExpenseTypeIds = new HashSet<>();
        for (ExpenseTypeEntity thatExpenseTypeEntity: that.expenseTypeToPayFor) {
            thatExpenseTypeIds.add(thatExpenseTypeEntity.getId());
        }
        return Objects.equals(cardProvider, that.cardProvider) && Objects.equals(cardNumber, that.cardNumber) &&
         Objects.equals(balance, that.balance) && Objects.equals(budget, that.budget) &&
         Objects.equals(expenseTypeIds, thatExpenseTypeIds) && Objects.equals(baseFees, that.baseFees) &&
         Objects.equals(debitFees, that.debitFees) && Objects.equals(expenseIds, thatExpenseIds) &&
         Objects.equals(paymentHistoryIds, thatPaymentHistoryIds);
    }

    /**
     * Returns a hash code value for the debit card. This is computed by calling
     * {@link Objects#hash(Object...)} with the card number, card type, balance, budget, and expense types to pay for.
     * @return the hash code value
     */
    @Override
    public int hashCode(){
        Set<Long> expenseIds = new HashSet<>();
        for (Expense expense : eligibleExpenses) {
            expenseIds.add(expense.getId());
        }
        Set<Long> paymentHistoryIds = new HashSet<>();
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoryIds.add(paymentHistory.getId());
        }
        Set<Long> expenseTypeIds = new HashSet<>();
        for (ExpenseTypeEntity expenseType : expenseTypeToPayFor) {
            expenseTypeIds.add(expenseType.getId());
        }

        return Objects.hash(cardNumber, cardProvider, balance, budget, expenseTypeIds, baseFees, debitFees, expenseIds,
         paymentHistoryIds);
    }

}
