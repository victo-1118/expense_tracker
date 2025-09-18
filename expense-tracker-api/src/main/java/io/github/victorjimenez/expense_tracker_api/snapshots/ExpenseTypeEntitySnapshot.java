package io.github.victorjimenez.expense_tracker_api.snapshots;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Immutable;

import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
@Immutable
public class ExpenseTypeEntitySnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snapshotId;

    private Long expenseTypeEntityId;

    private String expenseType;
    private double budgetAmount;
    @ElementCollection
    @CollectionTable(name="expense_type_entity_snapshot_base_cards", joinColumns = @JoinColumn(name="expense_type_entity_snapshot_id"))
    @Column(name="base_card_id")
    private List<String> baseCardNumbers;

    public ExpenseTypeEntitySnapshot() {}

    public ExpenseTypeEntitySnapshot(ExpenseTypeEntity expenseTypeEntity) {
        this.expenseTypeEntityId = expenseTypeEntity.getId();
        this.expenseType = expenseTypeEntity.getExpenseType();
        this.budgetAmount = expenseTypeEntity.getBudgetAmount();
        
        for (BaseCard baseCard : expenseTypeEntity.getBaseCards()) {
            baseCardNumbers.add(baseCard.getCardNumber());
        }

    }

    public Long getExpenseTypeEntityId() {
        return expenseTypeEntityId;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public List<String> getBaseCardNumbers() {
        return baseCardNumbers;
    }

    @Override
    public String toString() {
        return "ExpenseTypeEntitySnapshot[" +
                "snapshotId=" + snapshotId +
                "expenseTyepEntityId=" + expenseTypeEntityId +
                ", expenseType='" + expenseType + '\'' +
                ", budgetAmount=" + budgetAmount +
                ", baseCardNumbers=" + baseCardNumbers +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseTypeEntitySnapshot that = (ExpenseTypeEntitySnapshot) o;
        return expenseTypeEntityId.equals(that.expenseTypeEntityId) && expenseType.equals(that.expenseType) &&
         budgetAmount == that.budgetAmount && baseCardNumbers.equals(that.baseCardNumbers) && snapshotId.equals(that.snapshotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshotId, expenseTypeEntityId, expenseType, budgetAmount, baseCardNumbers);
    }
}
