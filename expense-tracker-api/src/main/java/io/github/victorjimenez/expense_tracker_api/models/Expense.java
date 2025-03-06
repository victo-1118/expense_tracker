package io.github.victorjimenez.expense_tracker_api.models;

import jakarta.persistence.*;
import java.util.Objects;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
import lombok.NoArgsConstructor;

@Entity

@NoArgsConstructor
@Table(indexes = {@Index(name = "expense_type_id_index", columnList = "expense_type_id")})
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "expensePaid", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<PaymentHistory> paymentHistories = new HashSet<>();
    private String expenseName;
    @ManyToMany(mappedBy = "eligibleExpenses")
    private Set<BaseCard> eligibleCards = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "expense_type_id")
    private ExpenseTypeEntity expenseType;
    private double expenseAmount;
    private LocalDate expenseDate;
    private boolean reoccurring;
    private LocalDate frequency;
   
    /**
     * Constructs an expense with the specified type, amount, and date.
     * @param expenseType the type of expense
     * @param expenseAmount the amount of the expense
     * @param expenseDate the date of the expense
     * @param expenseName the name of the expense
     * @param reoccurring whether the expense is reoccurring
     * @param frequency the frequency of the reoccurring expense
     * 
     */
    public Expense(ExpenseTypeEntity expenseType, double expenseAmount, LocalDate expenseDate, String expenseName, boolean reoccurring, LocalDate frequency) {
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseName = expenseName;
        this.reoccurring = reoccurring;
        this.frequency = frequency;
    }
    /**
     * Gets the ID of the expense.
     * @return the ID of the expense
     */
    public Long getId(){
        return id;
    }
    /**
     * Sets the type of expense.
     * @param expenseType the type of expense, must not be null
     */
    public void setExpenseType(ExpenseTypeEntity expenseType){
        this.expenseType = expenseType;
    }
    /**
     * Gets the type of expense.
     * @return the type of expense
     */
    public ExpenseTypeEntity getExpenseType(){
        return expenseType;
    }
    /**
     * Sets the amount of the expense.
     * @param expenseAmount the amount of the expense, must not be null
     */
    public void setExpenseAmount(double expenseAmount){
        this.expenseAmount = expenseAmount;
    }
    /**
     * Gets the amount of the expense.
     * @return the amount of the expense
     */
    public double getExpenseAmount(){
        return expenseAmount;
    }
    /**
     * Sets the date of the expense.
     * @param expenseDate the date of the expense, must not be null
     */

    public void setExpenseDate(LocalDate expenseDate){
        this.expenseDate = expenseDate;
    }
    /**
     * Gets the date of the expense.
     * @return the date of the expense
     */
    public LocalDate getExpenseDate(){
        return expenseDate;
    }

    public void setExpenseName(String expenseName){
        this.expenseName = expenseName;
    }

    public String getExpenseName(){
        return expenseName;
    }
    public void setReoccurring(boolean reoccurring){
        this.reoccurring = reoccurring;
    }
    public boolean getReoccurring(){
        return reoccurring;
    }
    public void setFrequency(LocalDate frequency){
        this.frequency = frequency;
    }
    public LocalDate getFrequency(){
        return frequency;
    }
    /**
     * Returns a string representation of the expense in the format:
     * "Expense[id=<id>, expenseType=<expenseType>, expenseAmount=<expenseAmount>, expenseDate=<expenseDate>]"
     * @return a string representation of the expense
     */
    @Override
    public String toString(){
        return "Expense[" + 
            "id=" + this.id +
            ", expenseType='" + expenseType.getId() +
            "', expenseAmount=" + this.expenseAmount +
            ", expenseDate='" + this.expenseDate +
            "', expenseName='" + this.expenseName +
            "', reoccurring=" + this.reoccurring +
            ", frequency=" + this.frequency +
            "]";
    }
    /**
     * Compares this expense with the given object for equality.
     * 
     * Two expenses are equal if their IDs, expense types, expense amounts, and expense dates are all equal.
     * 
     * @param obj the given object to compare with
     * @return true if the given object is an equal expense, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        Expense that = (Expense) obj;
        return Objects.equals(id, that.id) && Objects.equals(expenseType.getId(), that.expenseType.getId()) &&
                Objects.equals(expenseAmount, that.expenseAmount) && Objects.equals(expenseDate, that.expenseDate) &&
                Objects.equals(expenseName, that.expenseName) && Objects.equals(reoccurring, that.reoccurring) &&
                Objects.equals(frequency, that.frequency);
    }
    /**
     * Returns a hash code value for the expense. This is computed by calling 
     * {@link Objects#hash(Object...)} with the id, expenseType, expenseAmount, and expenseDate.
     * @return the hash code value
     */
    @Override
    public int hashCode(){
        return Objects.hash(id, expenseType.getId(), expenseAmount, expenseDate, expenseName, reoccurring, frequency);
    }
}

