package io.github.victorjimenez.expense_tracker_api.dto;
import java.time.LocalDate;
import java.time.Period;
/**
 * Data Transfer Object that represents an expense with properties like ID, name, amount, date, reoccurring, frequency, and expense type entity ID.
 * Provides methods to get these properties.
 */
public class ExpenseDTO {
    private Long id;
    private String expenseName;
    private Double expenseAmount;
    private LocalDate expenseDate;
    private Boolean reoccurring;
    private Period frequency;
    private Long expenseTypeEntityId;
    /**
     * Constructor for the ExpenseDTO class.
     * @param id
     * @param expenseName
     * @param expenseAmount
     * @param expenseDate
     * @param reoccurring
     * @param frequency
     * @param expenseTypeEntityId
     */
    public ExpenseDTO(Long id, String expenseName, Double expenseAmount, LocalDate expenseDate, Boolean reoccurring, Period frequency, Long expenseTypeEntityId) {
        this.id = id;
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.reoccurring = reoccurring;
        this.frequency = frequency;
        this.expenseTypeEntityId = expenseTypeEntityId;
    }
    
    /**
     * Gets the ID of the expense.
     * @return the ID of the expense
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Gets the name of the expense.
     * @return the name of the expense
     */
    public String getExpenseName() {
        return expenseName;
    }
    /**
     * Gets the amount of the expense.
     * @return the amount of the expense
     */
    public Double getExpenseAmount() {
        return expenseAmount;
    }
    /**
     * Gets the date of the expense.
     * @return the date of the expense
     */

    public LocalDate getExpenseDate() {
        return expenseDate;
    }
    /**
     * Checks if the expense is reoccurring.
     * @return true if the expense is reoccurring, false otherwise
     */

    public Boolean isReoccurring() {
        return reoccurring;
    }
    /**
     * Gets the frequency of the expense.
     * @return the frequency as a Period
     */

    public Period getFrequency() {
        return frequency;
    }
    /**
     * Gets the ID of the associated expense type entity.
     * @return the ID of the expense type entity
     */

    public Long getExpenseTypeEntityId() {
        return expenseTypeEntityId;
    }
}

