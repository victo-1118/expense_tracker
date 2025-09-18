package io.github.victorjimenez.expense_tracker_api.snapshots;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Immutable;
import org.springframework.cglib.core.Local;

import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Immutable
public class ExpenseSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseSnapshotId;
    private Long expenseId;
    private String expenseName;
    private double expenseAmount;
    @ElementCollection
    @CollectionTable(name="expense_snapshot_payment_histories", joinColumns = @JoinColumn(name="expense_snapshot_id"))
    @Column(name="expenses_payment_history_id")
    private Set<Long>  paymentHistoriesIds;
    @ElementCollection
    @CollectionTable(name="expense_snapshot_eligible_cards", joinColumns = @JoinColumn(name="expense_snapshot_id"))
    @Column(name="card_number")
    private Set<String> eligibleCardsIds;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "expense_type_id")
    private ExpenseTypeEntitySnapshot expenseTypesSnapshot;
    private boolean reoccurring;
    private Period frequency;
    private LocalDate expenseDate;

    public ExpenseSnapshot() {};

    public ExpenseSnapshot(Expense expense) {
        this.expenseId = expense.getId();
        this.expenseName = expense.getExpenseName();
        this.expenseAmount = expense.getExpenseAmount();
        for (PaymentHistory paymentHistory : expense.getPaymentHistories()) {
            this.paymentHistoriesIds.add(paymentHistory.getId());
        }
        for (BaseCard baseCard : expense.getEligibleCards()) {
            this.eligibleCardsIds.add(baseCard.getCardNumber());
        }
        this.expenseTypesSnapshot = new ExpenseTypeEntitySnapshot(expense.getExpenseType());
        this.reoccurring = expense.getReoccurring();
        this.frequency = expense.getFrequency();
        this.expenseDate = expense.getExpenseDate();
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public Long getSnapshotId() {
        return expenseSnapshotId;
    }

    public ExpenseTypeEntitySnapshot getExpenseType() {
        return expenseTypesSnapshot;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public boolean getReoccurring() {
        return reoccurring;
    }

    public Period getFrequency() {
        return frequency;
    }

    public Set<Long> getPaymentHistoriesIds() {
        return paymentHistoriesIds;
    }

    public Set<String> getEligibleCardsIds() {
        return eligibleCardsIds;
    }


    @Override
    public String toString() {
        return "ExpenseSnapshot{" +
                "expenseSnapshotId=" + expenseSnapshotId +
                ", expenseId=" + expenseId +
                ", expenseName='" + expenseName + '\'' +
                ", expenseAmount=" + expenseAmount +
                ", paymentHistoriesIds=" + paymentHistoriesIds +
                ", eligibleCardsIds=" + eligibleCardsIds +
                ", expenseType=" + expenseTypesSnapshot.toString() +
                ", reoccurring=" + reoccurring +
                ", frequency=" + frequency +
                ", expenseDate=" + expenseDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseSnapshot that = (ExpenseSnapshot) o;
        return Double.compare(that.expenseAmount, expenseAmount) == 0 && expenseTypesSnapshot.equals(that.expenseTypesSnapshot) &&
         reoccurring == that.reoccurring && frequency.equals(that.frequency) && expenseDate.equals(that.expenseDate)
                && expenseSnapshotId.equals(that.expenseSnapshotId) && expenseName.equals(that.expenseName) && paymentHistoriesIds.equals(that.paymentHistoriesIds)
                && eligibleCardsIds.equals(that.eligibleCardsIds) && expenseId.equals(that.expenseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expenseSnapshotId, expenseId, expenseName, expenseAmount, paymentHistoriesIds, eligibleCardsIds, expenseTypesSnapshot, reoccurring, frequency, expenseDate);
    }


}
