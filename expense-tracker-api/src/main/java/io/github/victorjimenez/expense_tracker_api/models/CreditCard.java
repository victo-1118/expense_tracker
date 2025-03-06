package io.github.victorjimenez.expense_tracker_api.models;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CreditCard")
@NoArgsConstructor

/**
 * Represents a credit card with properties like card type, card number, credit limit, personal limit, balance, budget as well as fees including base and credit card fees.
 * Inherits from the BaseCard class and provides additional methods to get and set these properties
 *
 * @author Victor Jimenez Pucheta
 */
public class CreditCard extends BaseCard{
    private double creditLimit;
    private double personalLimit;
    private double interestRate;
    private double minimumPayment;

    @ElementCollection
    @CollectionTable(name="credit_card_fees", joinColumns = @JoinColumn(name="card_number"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name="credit_card_fee_amount")
    private Map<CreditCardFeesType, Double> creditFees = new HashMap<>(); 

    /**
     * Constructs a credit card with the specified card type, card number, credit limit, and personal limit.
     * @param cardNumber
     * @param cardProvider
     * @param creditLimit
     * @param personalLimit
     * @param expenseTypeToPayFor
     * @param balance
     */
    public CreditCard(String cardNumber, String cardProvider, double balance, double creditLimit, double personalLimit, double interestRate, double minimumPayment){ 
        super(cardNumber, cardProvider, balance);
        this.creditLimit = creditLimit;
        this.personalLimit = personalLimit;
        this.interestRate = interestRate;
        this.minimumPayment = minimumPayment;
    }

    /**
     * Sets the credit limit of the credit card.
     * @param creditLimit the credit limit, must be 0 or greater
     */
    public void setCreditLimit(double creditLimit){

        this.creditLimit = creditLimit;
    }
    /**
     * Gets the credit limit of the credit card.
     * @return the credit limit
     */
    public double getCreditLimit(){
        return creditLimit;
    }

    /**
     * Sets the personal limit of the credit card.
     * @param personalLimit the personal limit, must be 0 or greater, and less than or equal to the credit limit
     */
    public void setPersonaLimit(double personalLimit){
        this.personalLimit = personalLimit;
    }
    
    /**
     * Gets the personal limit of the credit card.
     * @return the personal limit
     */
    public double getPersonalLimit(){
        return personalLimit;
    }

    public void setInterestRate(double interestRate){
        this.interestRate = interestRate;
    }
    public double getInterestRate(){
        return interestRate;

    }
    public void setMinimumPayment(double minimumPayment){
        this.minimumPayment = minimumPayment;
    }
    public double getMinimumPayment(){
        return minimumPayment;
    }

    public void addCreditFee(CreditCardFeesType feeType, double fee){
        creditFees.put(feeType, fee);
    }

    public double getCreditFee(CreditCardFeesType feeType){
        return creditFees.get(feeType);
    }

    public void removeCreditFee(CreditCardFeesType feeType){
        creditFees.remove(feeType);
    }
    /**
     * Returns a string representation of the credit card in the format:
     * "CreditCard[cardType=<cardType>, cardNumber=<cardNumber>, creditLimit=<creditLimit>, personalLimit=<personalLimit>, expenseTypeToPayFor={<expenseTypeToPayFor>}, balance=<balance>, fees={<fees>}]"
     * @return a string representation of the credit card
     */
    @Override
    public String toString() {

        
        
        String baseFeesString = baseFees.toString();
        String creditFeesString = creditFees.toString();
        StringBuilder expenseTypes = new StringBuilder();
        for (ExpenseTypeEntity expenseType : expenseTypeToPayFor) {
            expenseTypes.append(expenseType.getId()).append(", ");
        }
        if (!expenseTypeToPayFor.isEmpty()) {
            expenseTypes.delete(expenseTypes.length() - 2, expenseTypes.length());
        }
        String eligibleExpensesBuilderTypesString = expenseTypes.toString();
        StringBuilder paymentHistoriesBuilder = new StringBuilder();
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoriesBuilder.append(paymentHistory.getId()).append(", ");
        }
        if (!paymentHistories.isEmpty()) {

            paymentHistoriesBuilder.delete(paymentHistoriesBuilder.length() - 2, paymentHistoriesBuilder.length());
        }
        String paymentHistoriesString = paymentHistoriesBuilder.toString();
        StringBuilder eligibleExpensesBuilder = new StringBuilder();
        for (Expense expense : eligibleExpenses) {
            eligibleExpensesBuilder.append(expense.getId()).append(", ");
        }
        if (!eligibleExpenses.isEmpty()) {
            eligibleExpensesBuilder.delete(eligibleExpensesBuilder.length() - 2, eligibleExpensesBuilder.length());

        }
        String eligibleExpensesBuildersString = eligibleExpensesBuilder.toString();
        
        
        return "DebitCard[" +
               "cardNumber='" + cardNumber + "', " +
               "cardProvider='" + cardProvider + "', " +
               "balance=" + balance + ", " +
               "creditLimit=" + creditLimit + ", " +
               "personalLimit=" + personalLimit + ", " +
                "interestRate=" + interestRate + ", " +
                "minimumPayment=" + minimumPayment + ", " +
               "expenseTypeToPayFor=[" + eligibleExpensesBuilderTypesString + "]" +
               ", eligibleExpenses=[" + eligibleExpensesBuildersString + "]" +
                ", paymentHistories=[" + paymentHistoriesString + "]" +
               ", baseFees=" + baseFeesString + "" +
               ", creditFees=" + creditFeesString + "" +
               "]";
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        CreditCard that = (CreditCard) obj;
        Set<Long> expenseTypeToPayForSet = new HashSet<>();
        for (ExpenseTypeEntity expenseType : expenseTypeToPayFor) {
            expenseTypeToPayForSet.add(expenseType.getId());
        }
        Set<Long> thatExpenseTypeToPayForSet = new HashSet<>();
        for (ExpenseTypeEntity thatExpenseType : that.expenseTypeToPayFor) {
            thatExpenseTypeToPayForSet.add(thatExpenseType.getId());
        }
        Set<Long> eligibleExpensesBuildersSet = new HashSet<>();
        for (Expense expense : eligibleExpenses) {
            eligibleExpensesBuildersSet.add(expense.getId());
        }
        Set<Long> thatEligibleExpensesSet = new HashSet<>();
        for (Expense thatExpense : that.eligibleExpenses) {
            thatEligibleExpensesSet.add(thatExpense.getId());
        }
        Set<Long> paymentHistoriesSet = new HashSet<>();
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoriesSet.add(paymentHistory.getId());
        }    
        Set<Long> thatPaymentHistoriesSet = new HashSet<>();
        for (PaymentHistory thatPaymentHistory : that.paymentHistories) {
            thatPaymentHistoriesSet.add(thatPaymentHistory.getId());
        }
        return Objects.equals(cardProvider, that.cardProvider) && Objects.equals(cardNumber, that.cardNumber) &&
         Objects.equals(creditLimit, that.creditLimit) && Objects.equals(personalLimit, that.personalLimit) &&
          Objects.equals(expenseTypeToPayForSet, thatExpenseTypeToPayForSet) && Objects.equals(balance, that.balance)
           && Objects.equals(interestRate, that.interestRate) && Objects.equals(minimumPayment, that.minimumPayment)
           && Objects.equals(creditFees, that.creditFees) && Objects.equals(baseFees, that.baseFees) && 
           Objects.equals(eligibleExpensesBuildersSet, thatEligibleExpensesSet) && Objects.equals(paymentHistoriesSet, thatPaymentHistoriesSet); 
    }

    @Override
    public int hashCode(){
        Set<Long> expenseTypeToPayForSet = new HashSet<>();
        for (ExpenseTypeEntity expenseType : expenseTypeToPayFor) {
            expenseTypeToPayForSet.add(expenseType.getId());
        }
        Set<Long> eligibleExpensesBuildersSet = new HashSet<>();
        for (Expense expense : eligibleExpenses) {
            eligibleExpensesBuildersSet.add(expense.getId());
        }
        Set<Long> paymentHistoriesSet = new HashSet<>();
        for (PaymentHistory paymentHistory : paymentHistories) {
            paymentHistoriesSet.add(paymentHistory.getId());
        }
        return Objects.hash(cardNumber, cardProvider, creditLimit, personalLimit, expenseTypeToPayForSet,
         balance, interestRate, minimumPayment, creditFees, baseFees, eligibleExpensesBuildersSet, paymentHistoriesSet);
    }
}

