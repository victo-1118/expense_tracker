package io.github.victorjimenez.expense_tracker_api.models;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;/**
 * Represents a debit card with properties like card type, card number, balance, and budget.
 * Provides methods to get and set these properties, with validation to ensure data consistency.
 *
 * @author Victor Jimenez Pucheta
 */
import lombok.NoArgsConstructor;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "\"base_card\"", indexes = {
    @Index(name="card_balance_index", columnList = "balance"),
    @Index(name="expense_type_index", columnList = "expenseTypeToPayFor"),
    @Index(name="eligible_expense_index", columnList = "eligibleExpenses"),
    @Index(name="payment_history_index", columnList = "paymentHistories"),
    @Index(name="debit_card_budget_index", columnList = "card_type, budget"),
    @Index(name="credit_card_credit_limit_index", columnList = "card_type, creditLimit"),
    @Index(name="credit_card_personal_limit_index", columnList = "card_type, personalLimit"),
    @Index(name="credit_card_interest_rate_index", columnList = "card_type, interestRate"),
    @Index(name="credit_card_minimum_payment_index", columnList = "card_type, minimumPayment"),
    
})

@NoArgsConstructor
public abstract class BaseCard{
    @ElementCollection
    @CollectionTable(name="base_card_fees", joinColumns = @JoinColumn(name = "card_number"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name="baseFee_amount")
    protected Map<BaseCardFeesType, Double> baseFees = new HashMap<>();
    @Column(name = "card_provider")
    protected String cardProvider;
    protected double balance;

    @Id
    @Column(name= "card_number",unique = true, length = 4)
    protected String cardNumber;
    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "base_card_expense_type",
            joinColumns = @JoinColumn(name = "card_number"),
            inverseJoinColumns = @JoinColumn(name = "expense_type_id"))
    protected Set<ExpenseTypeEntity> expenseTypeToPayFor = new HashSet<>();
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "base_card_expense",
            joinColumns = @JoinColumn(name = "card_number"),
            inverseJoinColumns = @JoinColumn(name = "expense_id"))
    protected Set<Expense> eligibleExpenses = new HashSet<>();

    @OneToMany(mappedBy = "paidByCard")
    protected Set<PaymentHistory> paymentHistories = new HashSet<>();
    
    /**
     * Constructs a base card with the specified card number, card provider, and balance.
     * @param cardNumber
     * @param cardProvider
     * @param balance
     */
    protected BaseCard(String cardNumber, String cardProvider,  double balance) {
        this.cardProvider = cardProvider;
        this.cardNumber = cardNumber;
        this.balance = balance;
    }
    public void addFee(BaseCardFeesType feeType, double fee){
        baseFees.put(feeType, fee);
    }
    /**
     * Removes a fee from the card based on the provided fee type.
     * 
     * @param feeType the type of fee to be removed
     */
    public void removeFee(BaseCardFeesType feeType){
        baseFees.remove(feeType);
    }
    public double getFee(BaseCardFeesType feeType){
        return baseFees.get(feeType);
    }
    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }
    public String getCardNumber(){
        return cardNumber;
    }
    public void setCardProvider(String cardProvider){
        this.cardProvider = cardProvider;
    }
    public String getCardProvider(){
        return cardProvider;
    }

    public Set<ExpenseTypeEntity> getExpenseTypeToPayFor(){
        return expenseTypeToPayFor;
    }

    public void addExpenseTypeToPayFor(ExpenseTypeEntity expenseType){
        expenseTypeToPayFor.add(expenseType);
    }

    


    public void removeExpenseTypeToPayFor(ExpenseTypeEntity expenseType){
        expenseTypeToPayFor.remove(expenseType);
    }
    public void addExpense(Expense expense){
        eligibleExpenses.add(expense);
    }
    public void removeExpense(Expense expense){
        eligibleExpenses.remove(expense);
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public double getBalance(){
        return balance;
    }

    public Set<Expense> getEligibleExpenses(){
        return eligibleExpenses;
    }

    public void addPaymentHistory(PaymentHistory paymentHistory){
        paymentHistories.add(paymentHistory);
    }

    public void removePaymentHistory(PaymentHistory paymentHistory){
        paymentHistories.remove(paymentHistory);
    }

    public Set<PaymentHistory> getPaymentHistories(){
        return paymentHistories;
    }



}


