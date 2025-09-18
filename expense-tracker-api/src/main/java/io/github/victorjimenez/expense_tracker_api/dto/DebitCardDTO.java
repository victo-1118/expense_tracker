package io.github.victorjimenez.expense_tracker_api.dto;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/**
 * Data Transfer Object that represents a debit card with properties like card number, card provider, balance, and base fees.
 * Provides methods to get these properties.
 */
public class DebitCardDTO extends BaseCardDTO{
    private Double budget;
    private Map<String, Double> debitFees;
    /**
     * Constructor for the DebitCardDTO class.
     * @param cardNumber
     * @param cardProvider
     * @param balance
     * @param expenseTypeToPayForIds
     * @param baseFees
     * @param budget
     * @param debitFees
     */
    public DebitCardDTO(String cardNumber, String cardProvider, Double balance,List<Long> expenseTypeToPayForIds, Map<String, Double> baseFees, Double budget , Map<String, Double> debitFees) {
        super(cardNumber, cardProvider, balance, expenseTypeToPayForIds, baseFees);
        this.budget = budget;
        this.debitFees = debitFees;
    }

    /**
     * Retrieves the budget associated with this debit card.
     * 
     * @return the budget as a Double
     */

    public Double getBudget() {
        return budget;
    }
    /**
     * Retrieves the debit fees associated with this debit card.
     * 
     * @return a map where the keys are the names of the fees and the values are the amounts for each fee.
     */

    public Map<String, Double> getDebitFees() {
        return debitFees;
    }


}
