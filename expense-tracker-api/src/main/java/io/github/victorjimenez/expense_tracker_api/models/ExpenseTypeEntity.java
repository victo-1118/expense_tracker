package io.github.victorjimenez.expense_tracker_api.models;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "expense_type_index", columnList = "expenseType")})
public class ExpenseTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean active = true;
    @Column(unique = true)
    private String expenseType;
    // theres an argument for this being nullable
    private double budgetAmount;


    @ManyToMany(mappedBy = "expenseTypeToPayFor")
    private Set<BaseCard> baseCards = new HashSet<>();

    /**
     * Constructs an expense type entity with the specified type.
     * @param expenseType the type of expense
     */
    public ExpenseTypeEntity(String expenseType, double budgetAmount) {
        this.expenseType = expenseType;
        this.budgetAmount = budgetAmount;
        
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }   
    /**
     * Returns the type of expense.
     * @return the type of expense
     */
    public String getExpenseType(){
        return expenseType;
    }
    /**
     * Sets the type of expense.
     * @param expenseType the type of expense, must not be null
     */
    public void setExpenseType(String expenseType){
        this.expenseType = expenseType;
    }
    /**
     * Returns the ID of the expense type entity.
     * @return the ID of the expense type entity
     */
    public Long getId(){
        return id;
    }
    /**
     * Returns whether the expense is reoccurring or not.
     * @return true if the expense is reoccurring, false otherwise
     */

    /**
     * Returns a string representation of the expense type entity in the format:
     * "ExpenseTypeEntity[id=<id>, expenseType=<expenseType>]"
     * @return a string representation of the expense type entity
     */
    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
    public double getBudgetAmount() {
        return budgetAmount;
    }   

    public void addBaseCard(BaseCard baseCard) {
        baseCards.add(baseCard);
    }

    public void removeBaseCard(BaseCard baseCard) {
        baseCards.remove(baseCard);
    }
    public Set<BaseCard> getBaseCards() {
        return baseCards;
    }

    


    @Override
    public String toString(){
       Set<String> cardNumbers = new HashSet<>();
        for (BaseCard baseCard : baseCards) {
            cardNumbers.add(baseCard.getCardNumber());
        }
        return "ExpenseTypeEntity[" +
                "id=" + id +
                ", expenseType='" + expenseType + "'" +
  
                ", budgetAmount=" + budgetAmount +

                ", baseCards=" + cardNumbers.toString() +
                ']';
    }
    /**
     * Compares this expense type entity with the given object for equality.
     * 
     * Two expense type entities are equal if their IDs and expense types are all equal.
     * 
     * @param obj the given object to compare with
     * @return true if the given object is an equal expense type entity, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        ExpenseTypeEntity that = (ExpenseTypeEntity) obj;
        return Objects.equals(id, that.id) && Objects.equals(expenseType, that.expenseType) &&
         Objects.equals(budgetAmount, that.budgetAmount) && 
         Objects.equals(baseCards, that.baseCards);
    }
    /**
     * Returns a hash code value for the expense type entity. This is computed by calling
     * {@link Objects#hash(Object...)} with the id and expenseType.
     * @return the hash code value
     */
    @Override
    public int hashCode(){
        Set<String> cardNumbers = new HashSet<>();
        for (BaseCard baseCard : baseCards) {
            cardNumbers.add(baseCard.getCardNumber());
        }
        return Objects.hash(id, expenseType, budgetAmount, cardNumbers);
    }
}

