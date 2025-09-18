package io.github.victorjimenez.expense_tracker_api.dto;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
/**
 * Data Transfer Object that represents a credit card with additional properties like credit limit, personal limit, interest rate, minimum payment, and fees.
 * Provides methods to get these properties.
 */
public class CreditCardDTO extends BaseCardDTO{
    private Double creditLimit;
    private Double personalLimit;
    private Double interestRate;
    private Double minimumPayment;
    private Double minimumPaymentDynamic;
    private Period periodDueDateAfterCycle;
    private Period billingCyclePeriod;
    private LocalDate startOfBillingCycle;

    private Map<String, Double> creditFees;
    /**
     * Constructor for the CreditCardDTO class.
     * @param cardNumber
     * @param cardProvider
     * @param balance
     * @param expenseTypeToPayForIds
     * @param baseFees
     * @param creditLimit
     * @param personalLimit
     * @param interestRate
     * @param minimumPayment
     * @param minimumPaymentDynamic
     * @param periodDueDateAfterCycle
     * @param billingCyclePeriod
     * @param startOfBillingCycle
     * @param creditFees
     */
    public CreditCardDTO(String cardNumber, String cardProvider, Double balance,List<Long> expenseTypeToPayForIds,
     Map<String, Double> baseFees, Double creditLimit, Double personalLimit, Double interestRate, Double minimumPayment,
      Double minimumPaymentDynamic, Period periodDueDateAfterCycle, Period billingCyclePeriod, LocalDate startOfBillingCycle,
       Map<String, Double> creditFees) {
        super(cardNumber, cardProvider, balance, expenseTypeToPayForIds, baseFees);
        this.creditLimit = creditLimit;
        this.personalLimit = personalLimit;
        this.interestRate = interestRate;
        this.minimumPayment = minimumPayment;
        this.creditFees = creditFees;
        this.periodDueDateAfterCycle = periodDueDateAfterCycle;
        this.startOfBillingCycle = startOfBillingCycle;
        this.billingCyclePeriod = billingCyclePeriod;
        this.minimumPaymentDynamic = minimumPaymentDynamic;
    }
    /**
     * Gets the credit fees associated with the credit card.
     * The fees are represented as a map where the key is the fee type and the value is the fee amount.
     * 
     * @return a map containing the credit fee types and their corresponding amounts
     */


    public Map<String, Double> getCreditFees() {
        return creditFees;
    }

    /**
     * Gets the credit limit of the credit card.
     * @return the credit limit
     */
    public Double getCreditLimit() {
        return creditLimit;
    }
    /**
     * Gets the personal limit of the credit card.
     * The personal limit is a value below the credit limit that is set by the user to avoid overspending.
     * @return the personal limit
     */
    public Double getPersonalLimit() {
        return personalLimit;
    }
    /**
     * Gets the interest rate of the credit card.
     * The interest rate is a decimal value representing the percentage of the credit card balance that is charged as interest.
     * @return the interest rate
     */
    public Double getInterestRate() {
        return interestRate;
    }
    /**
     * Gets the minimum payment for the credit card.
     * The minimum payment is a value representing the minimum amount that the user must pay towards the credit card balance each period.
     * @return the minimum payment
     */
    public Double getMinimumPayment() {
        return minimumPayment;
    }
    /**
     * Gets the dynamic minimum payment for the credit card.
     * The dynamic minimum payment is a value representing the percentage of the credit card balance that is the minimum amount that the user must pay each period.
     * @return the dynamic minimum payment
     */
    public Double getMinimumPaymentDynamic() {
        return minimumPaymentDynamic;
    }
    /**
     * Gets the period between the start of the billing cycle and the due date of the credit card.
     * The period represents the amount of time between the start of the billing cycle and the due date of the credit card.
     * @return the period between the start of the billing cycle and the due date of the credit card
     */
    public Period getPeriodDueDateAfterCycle() {
        return periodDueDateAfterCycle;
    }   
    /**
     * Gets the billing cycle period for the credit card.
     * The billing cycle period is the duration of time between the start and end of a billing cycle.
     * @return the billing cycle period
     */

    public Period getBillingCyclePeriod() {
        return billingCyclePeriod;
    }
    /**
     * Gets the start of the billing cycle for the credit card.
     * The start of the billing cycle is the first day of the billing cycle period.
     * @return the start of the billing cycle
     */
    public LocalDate getStartOfBillingCycle() {
        return startOfBillingCycle;
    }
}
