package io.github.victorjimenez.expense_tracker_api.models;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.util.Objects;

import io.github.victorjimenez.expense_tracker_api.snapshots.BaseCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.CreditCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.DebitCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.ExpenseSnapshot;
import lombok.AccessLevel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
/**
 * Represents a payment history of an expense.
 * Used to analyze payment patterns for the user.
 * No setter methods are provided as the fields are immutable.
 * @Author Victor Jimenez Pucheta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(indexes={@Index(name="expense_id_index", columnList="expense_id")
    ,@Index(name="card_number_index", columnList="card_number"),
    @Index(name="payment_date_index", columnList="payment_date")})
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double amountPaid;
    private LocalDate paymentDate;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "card_number", nullable = true)
    private BaseCard paidByCard;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "expense_id", nullable = true)
    
    private Expense expensePaid;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = BaseCardSnapshot.class, orphanRemoval = true)
    @JoinColumn(name = "base_card_snapshot_id", updatable = false)
    private BaseCardSnapshot baseCardSnapshot;



    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "snapshot_expense_id",  updatable = false)
    private ExpenseSnapshot expenseSnapshot;
    /**
     * Constructor for PaymentHistory.
     * @param expensePaid
     * @param amountPaid
     * @param paymentDate
     * @param paidByCard
     * @param baseCardSnapshot
     * @param expenseSnapshot
     */
    public PaymentHistory(Expense expensePaid, double amountPaid, LocalDate paymentDate, BaseCard paidByCard, BaseCardSnapshot baseCardSnapshot, ExpenseSnapshot expenseSnapshot) {
        this.expensePaid = expensePaid;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paidByCard = paidByCard;
        this.baseCardSnapshot = baseCardSnapshot;
        this.expenseSnapshot = expenseSnapshot;
    }

    

    public Long getId() {
        return id;
    }

    public Expense getExpensePaid() {
        return expensePaid;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public BaseCard getPaidByCard() {
        return paidByCard;
    }

    public BaseCardSnapshot getBaseCardSnapshot() {
        return baseCardSnapshot;
    }

    public ExpenseSnapshot getExpenseSnapshot() {
        return expenseSnapshot;
    }
    @Override
    public String toString() {
        return "PaymentHistory[" +
                "id=" + id +
                ", expensePaid=" + expensePaid.getId() +
                ", amountPaid=" + amountPaid +
                ", paymentDate=" + paymentDate +
                ", paidByCard=" + paidByCard.getCardNumber() +
                ", baseCardSnapshot=" + baseCardSnapshot.toString() +
                ']';

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentHistory that = (PaymentHistory) o;
        return id.equals(that.id) && amountPaid == that.amountPaid && paymentDate.equals(that.paymentDate) && expensePaid.getId().equals(that.expensePaid.getId()) && paidByCard.getCardNumber().equals(that.paidByCard.getCardNumber());
    }
    @Override
    public int hashCode() {
        return Objects.hash(id,expensePaid.getId(), amountPaid, paymentDate,paidByCard.getCardNumber());
    }


}
