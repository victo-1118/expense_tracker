package io.github.victorjimenez.expense_tracker_api.models;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AccessLevel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

import jakarta.persistence.CascadeType;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(indexes={@Index(name="expense_id_index", columnList="expense_id")
    ,@Index(name="paid_by_card_number_index", columnList="paid_by_card_number"),
    @Index(name="payment_date_index", columnList="payment_date")})
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expensePaid;
    private double amountPaid;
    private LocalDate paymentDate;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "paid_by_card_number")
    private BaseCard paidByCard;

    public PaymentHistory(Expense expensePaid, double amountPaid, LocalDate paymentDate, BaseCard paidByCard) {
        this.expensePaid = expensePaid;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paidByCard = paidByCard;
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
    @Override
    public String toString() {
        return "PaymentHistory[" +
                "id=" + id +
                ", expensePaid=" + expensePaid.getId() +
                ", amountPaid=" + amountPaid +
                ", paymentDate=" + paymentDate +
                ", paidByCard=" + paidByCard.getCardNumber() +
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
