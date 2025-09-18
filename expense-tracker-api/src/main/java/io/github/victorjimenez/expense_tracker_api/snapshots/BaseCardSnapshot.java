package io.github.victorjimenez.expense_tracker_api.snapshots;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Immutable;

import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
// do we need a snpashot of the relationship with PaymentHistory?
// any benefits? Well the user could take a look at their payment history at that moment of time. Makes it easier
// to see corresponding history. could add this to the frontend so the user can see.
// not really any downsides i think? does take up some space and there might be issues with recursion.
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;

// having an issue here because JPA doesnt recognize how to structure the database schema. Will need to fix all
// snapshots/embeddables.

//will work on later. Basically replace all embeddables with entities. We will create a one to one with PaymentHistory
//we have already made it so that base card and expense can be nullable in PaymentHistory so that  we can delete them
//without deleting payment history
@Entity
@Immutable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_snapshot_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BaseCardSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long baseCardSnapshotId;
    protected String cardNumber;
    @ElementCollection
    @CollectionTable(name="base_card_snapshot_fees", joinColumns = @JoinColumn(name = "card_snapshot_id"))
    @MapKeyColumn(name="fee_type")
    @Column(name="base_card_fee_amount")
    protected Map<String, Double> baseFees = new HashMap<>();
    protected String cardProvider;
    protected double balance;
    @ElementCollection
    @CollectionTable(name="base_card_snapshot_expense_types", joinColumns = @JoinColumn(name = "card_snapshot_id"))
    @Column(name="cards_expense_type_id")
    protected Set<Long> expenseTypeToPayForIds = new HashSet<>();
    @ElementCollection
    @CollectionTable(name="base_card_snapshot_eligible_expenses", joinColumns = @JoinColumn(name = "card_snapshot_id"))
    @Column(name="eligible_expense_id")
    protected Set<Long> eligibleExpensesIds = new HashSet<>();
    @ElementCollection
    @CollectionTable(name="base_card_snapshot_payment_histories", joinColumns = @JoinColumn(name = "card_snapshot_id"))
    @Column(name="payment_history_id")
    protected Set<Long> paymentHistoriesIds = new HashSet<>();

    public BaseCardSnapshot() {};

    public BaseCardSnapshot(BaseCard baseCard) {
        this.baseFees = new HashMap<>(baseCard.getFees());
        this.cardProvider = baseCard.getCardProvider();
        this.balance = baseCard.getBalance();
        this.cardNumber = baseCard.getCardNumber();
        for (ExpenseTypeEntity expenseType : baseCard.getExpenseTypeToPayFor()) {
            this.expenseTypeToPayForIds.add(expenseType.getId());
        }
        for (Expense expense : baseCard.getEligibleExpenses()) {
            this.eligibleExpensesIds.add(expense.getId());
        }
        for (PaymentHistory paymentHistory : baseCard.getPaymentHistories()) {
            this.paymentHistoriesIds.add(paymentHistory.getId());
        }
    }

    public Map<String, Double> getFees() {
        return baseFees;
    }
    public double getFee(BaseCardFeesType feeType) {
        String feeTypeString = feeType.getDisplayName();
        return baseFees.get(feeTypeString);
    }

    public String getCardProvider() {    
        return cardProvider;
    }

    public double getBalance() {
        return balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Long getSnapshotId() {
        return baseCardSnapshotId;
    }

    public Set<Long> getExpenseTypeToPayForIds() {
        return expenseTypeToPayForIds;
    }

    public Set<Long> getEligibleExpensesIds() {
        return eligibleExpensesIds;
    }

    public Set<Long> getPaymentHistoriesIds() {
        return paymentHistoriesIds;
    }
    @Override
    public String toString() {
        return "" +
                "baseFees=" + baseFees +
                ", cardProvider='" + cardProvider + '\'' +
                ", balance=" + balance +
                ", cardNumber='" + cardNumber + '\'' +
                ", expenseTypeToPayForIds=" + expenseTypeToPayForIds +
                ", eligibleExpensesIds=" + eligibleExpensesIds +
                ", paymentHistoriesIds=" + paymentHistoriesIds;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCardSnapshot that = (BaseCardSnapshot) o;
        return Double.compare(that.balance, balance) == 0 && cardProvider.equals(that.cardProvider) && cardNumber.equals(that.cardNumber) && baseFees.equals(that.baseFees)
                && expenseTypeToPayForIds.equals(that.expenseTypeToPayForIds) && eligibleExpensesIds.equals(that.eligibleExpensesIds) && paymentHistoriesIds.equals(that.paymentHistoriesIds)
                && baseFees.equals(that.baseFees);
    }
    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, cardProvider, balance, baseFees, expenseTypeToPayForIds, eligibleExpensesIds, paymentHistoriesIds);
    }
}