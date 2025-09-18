package io.github.victorjimenez.expense_tracker_api.snapshots;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCardFeesType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
@Entity
@DiscriminatorValue("DEBIT")
public class DebitCardSnapshot extends BaseCardSnapshot{
    private double budget;
    @ElementCollection
    @CollectionTable(name="debit_card_snapshot_fees", joinColumns = @JoinColumn(name="card_snapshot_number"))
    @MapKeyColumn(name="fee_type")  
    @Column(name="debit_card_fee_amount")  
    private Map<String, Double> debitFees = new HashMap<>();

    public DebitCardSnapshot() {}

    public DebitCardSnapshot(DebitCard debitCard) {
        super(debitCard);
        this.budget = debitCard.getBudget();
        this.debitFees = new HashMap<>(debitCard.getDebitFees());
    }

    public double getBudget() {
        return budget;
    }

    public Map<String, Double> getDebitFees() {
        return debitFees;
    }

    public double getDebitFee(DebitCardFeesType feeType) {
        String feeTypeString = feeType.getDisplayName();
        return debitFees.get(feeTypeString);
    }

    @Override
    public String toString() {
        return "DebitCardSnapshot{"+
                super.toString() +
                "budget=" + budget +
                ", debitFees=" + debitFees +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DebitCardSnapshot that = (DebitCardSnapshot) o;
        return Double.compare(that.budget, budget) == 0 && debitFees.equals(that.debitFees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), budget, debitFees);
    }

}
