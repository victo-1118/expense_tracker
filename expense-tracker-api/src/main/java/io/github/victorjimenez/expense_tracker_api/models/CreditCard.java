package io.github.victorjimenez.expense_tracker_api.models;

import java.util.Map;
import java.util.Objects;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

import org.springframework.cglib.core.Local;
import java.time.Period;
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
    private double minimumPaymentDynamic;
    private Period periodDueDateAfterCycle;
    private Period billingCyclePeriod;
    private LocalDate startOfBillingCycle;

    @ElementCollection
    @CollectionTable(name="credit_card_fees", joinColumns = @JoinColumn(name="card_number"))
    @MapKeyColumn(name="fee_type")
    @Column(name="credit_card_fee_amount")
    private Map<String, Double> creditFees = new HashMap<>(); 


    public CreditCard(String cardNumber, String cardProvider, double balance, double creditLimit, double personalLimit,
     double interestRate, double minimumPayment, double minimumPaymentDynamic, Period periodDueDateAfterCycle, Period billingCyclePeriod, LocalDate startOfBillingCycle) {
         
     
        super(cardNumber, cardProvider, balance);
        this.creditLimit = creditLimit;
        this.personalLimit = personalLimit;
        this.interestRate = interestRate;
        this.minimumPayment = minimumPayment;
        this.minimumPaymentDynamic = minimumPaymentDynamic;
        this.periodDueDateAfterCycle = periodDueDateAfterCycle;
        this.startOfBillingCycle = startOfBillingCycle;
        this.billingCyclePeriod = billingCyclePeriod;
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

    public void setMinimumPaymentDynamic(double minimumPaymentDynamic){
        this.minimumPaymentDynamic = minimumPaymentDynamic;
    }
    public double getMinimumPaymentDynamic(){
        return minimumPaymentDynamic;
    }

    public void setPeriodDueDateAfterCycle(Period periodDueDateAfterCycle){
        this.periodDueDateAfterCycle = periodDueDateAfterCycle;
    }
    public Period getPeriodDueDateAfterCycle(){
        return periodDueDateAfterCycle;
    }

    public void setStartOfBillingCycle(LocalDate startOfBillingCycle){
       
        this.startOfBillingCycle = startOfBillingCycle;
    }
    public LocalDate getStartOfBillingCycle(){
        return startOfBillingCycle;
    }

    public void addCreditFee(CreditCardFeesType feeType, double fee){
        String feeTypeString = feeType.getDisplayName();
        creditFees.put(feeTypeString, fee);
    }

    public void setBillingCyclePeriod(Period billingCyclePeriod){
        this.billingCyclePeriod = billingCyclePeriod;
    }

    public Period getBillingCyclePeriod(){
        return billingCyclePeriod;
    }

    public double getCreditFee(CreditCardFeesType feeType){
        String feeTypeString = feeType.getDisplayName();
        return creditFees.get(feeTypeString);
    }

    public Map<String, Double> getCreditFees(){
        return creditFees;
    }

    public void removeCreditFee(CreditCardFeesType feeType){
        String feeTypeString = feeType.getDisplayName();
        creditFees.remove(feeTypeString);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return "CreditCard[" +
               "cardNumber='" + cardNumber + "', " +
               "cardProvider='" + cardProvider + "', " +
               "balance=" + balance + ", " +
               "creditLimit=" + creditLimit + ", " +
               "personalLimit=" + personalLimit + ", " +
                "interestRate=" + interestRate + ", " +
                "minimumPayment=" + minimumPayment + ", " +
                "minimumPaymentDynamic=" + minimumPaymentDynamic + ", " +
                "periodDueDateAfterCycle=" + periodDueDateAfterCycle.toString() + ", " +
                "billingCyclePeriod=" + billingCyclePeriod.toString() + ", " +
                "startOfBillingCycle=" + startOfBillingCycle.format(formatter) + ", " +
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
           Objects.equals(eligibleExpensesBuildersSet, thatEligibleExpensesSet) &&
            Objects.equals(paymentHistoriesSet, thatPaymentHistoriesSet) &&
             Objects.equals(periodDueDateAfterCycle, that.periodDueDateAfterCycle) &&
             Objects.equals(billingCyclePeriod, that.billingCyclePeriod) &&
              Objects.equals(startOfBillingCycle, that.startOfBillingCycle) && 
            Objects.equals(minimumPaymentDynamic, that.minimumPaymentDynamic); 
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
         balance, interestRate, minimumPayment, minimumPaymentDynamic, periodDueDateAfterCycle,
          billingCyclePeriod, startOfBillingCycle, creditFees, baseFees, eligibleExpensesBuildersSet, paymentHistoriesSet);
    }
}

