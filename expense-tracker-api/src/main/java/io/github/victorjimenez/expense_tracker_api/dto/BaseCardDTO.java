package io.github.victorjimenez.expense_tracker_api.dto;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/**
 * Data Transfer Object that represents a base card with properties like card number, card provider, balance, and base fees.
 * Provides methods to get  these properties.
 */
public abstract class BaseCardDTO {
    private String cardNumber;
    private String cardProvider;
    private double balance;
    private List<Long> expenseTypeToPayForIds;

    private Map<String, Double> baseFees = new HashMap<>();
    
    /**
     * Constructor for the BaseCardDTO class.
     * @param cardNumber
     * @param cardProvider
     * @param balance
     * @param expenseTypeToPayForIds
     * @param baseFees
     */
    public BaseCardDTO(String cardNumber, String cardProvider, double balance, List<Long> expenseTypeToPayForIds, Map<String, Double> baseFees) {
        this.cardNumber = cardNumber;
        this.cardProvider = cardProvider;
        this.balance = balance;
        this.expenseTypeToPayForIds = expenseTypeToPayForIds;
        this.baseFees = baseFees;
    }

    /**
     * Gets the card number of the base card.
     * @return the card number of the base card
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Gets the card provider of the base card.
     * @return the card provider of the base card
     */
    public String getCardProvider() {
        return cardProvider;
    }

    /**
     * Gets the balance of the base card.
     * @return the balance of the base card
     */
    public double getBalance() {
        return balance;
    }



    /**
     * Gets the list of expense type IDs that this card is intended to pay for.
     * @return a list of expense type IDs
     */
    public List<Long> getExpenseTypeToPayForIds() {
        return expenseTypeToPayForIds;
    }
    
    /**
     * Retrieves the base fees associated with the card.
     * The fees are represented as a map where the key is the fee type
     * and the value is the fee amount.
     * 
     * @return a map of base fee types to their corresponding amounts
     */

    public Map<String, Double> getBaseFees() {
        return baseFees;
    }
}
