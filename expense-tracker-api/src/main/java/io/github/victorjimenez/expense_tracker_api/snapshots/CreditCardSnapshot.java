package io.github.victorjimenez.expense_tracker_api.snapshots;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.hibernate.annotations.Immutable;

import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCardFeesType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
@Entity
@DiscriminatorValue("CREDIT")
public class CreditCardSnapshot extends BaseCardSnapshot{
    private double creditLimit;
    private double personalLimit;
    private double interestRate;
    private double minimumPayment;
    private double minimumPaymentDynamic;
    private Period periodDueDateAfterCycle;
    private Period billingCyclePeriod;
    private LocalDate startOfBillingCycle;
    @ElementCollection
    @CollectionTable(name="credit_card_snapshot_fees", joinColumns = @JoinColumn(name="card_snapshot_number"))
    @MapKeyColumn(name="fee_type")
    @Column(name="credit_card_fee_amount")
    private Map<String, Double> creditFees = new HashMap<>(); 

    public CreditCardSnapshot() {}

    public CreditCardSnapshot(CreditCard creditCard) {
        super(creditCard);
        this.creditLimit = creditCard.getCreditLimit();
        this.personalLimit = creditCard.getPersonalLimit();
        this.interestRate = creditCard.getInterestRate();
        this.minimumPayment = creditCard.getMinimumPayment();
        this.creditFees = new HashMap<>(creditCard.getCreditFees());
        this.periodDueDateAfterCycle = creditCard.getPeriodDueDateAfterCycle();
        this.startOfBillingCycle = creditCard.getStartOfBillingCycle();
        this.billingCyclePeriod = creditCard.getBillingCyclePeriod();
        this.minimumPaymentDynamic = creditCard.getMinimumPaymentDynamic();
    }

    public double getCreditLimit() {
        return creditLimit;
    }
    
    public double getPersonalLimit() {
        return personalLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumPayment() {
        return minimumPayment;
    }

    public double getMinimumPaymentDynamic() {
        return minimumPaymentDynamic;
    }

    public Period getPeriodDueDateAfterCycle() {
        return periodDueDateAfterCycle;
    }

    public Period getBillingCyclePeriod() {
        return billingCyclePeriod;
    }

    public LocalDate getStartOfBillingCycle() {
        return startOfBillingCycle;
    }

    public Map<String, Double> getCreditFees() {
        return creditFees;
    }

    public double getCreditFee(CreditCardFeesType feeType) {
        String feeTypeString = feeType.getDisplayName();
        return creditFees.get(feeTypeString);
    }
    @Override
    public String toString() {
        return "CreditCardSnapshot[" + super.toString() + 
                ", creditLimit=" + creditLimit + 
                ", personalLimit=" + personalLimit + 
                ", interestRate=" + interestRate + 
                ", minimumPayment=" + minimumPayment + 
                ", minimumPaymentDynamic=" + minimumPaymentDynamic + 
                ", periodDueDateAfterCycle=" + periodDueDateAfterCycle + 
                ", billingCyclePeriod=" + billingCyclePeriod + 
                ", startOfBillingCycle=" + startOfBillingCycle + 
                ", creditFees=" + creditFees.toString() + "]";
    }
    @Override

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreditCardSnapshot that = (CreditCardSnapshot) o;
        return Double.compare(that.creditLimit, creditLimit) == 0 && 
                Double.compare(that.personalLimit, personalLimit) == 0 && 
                Double.compare(that.interestRate, interestRate) == 0 && 
                Double.compare(that.minimumPayment, minimumPayment) == 0 && 
                Double.compare(that.minimumPaymentDynamic, minimumPaymentDynamic) == 0 && 
                periodDueDateAfterCycle.equals(that.periodDueDateAfterCycle) && 
                billingCyclePeriod.equals(that.billingCyclePeriod) && 
                startOfBillingCycle.equals(that.startOfBillingCycle) && 
                creditFees.equals(that.creditFees);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), creditLimit, interestRate);
    }
    
}
