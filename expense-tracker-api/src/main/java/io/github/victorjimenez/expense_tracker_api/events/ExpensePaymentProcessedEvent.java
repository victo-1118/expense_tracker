package io.github.victorjimenez.expense_tracker_api.events;

import java.time.LocalDate;


public class ExpensePaymentProcessedEvent {
    private final Long expenseId;
    private final String paidByCardNumber;
    private final double amountPaid;
    private final LocalDate paymentDate;

    public ExpensePaymentProcessedEvent(Long expenseId, String paidByCardNumber, double amountPaid, LocalDate paymentDate) {
        this.expenseId = expenseId;
        this.paidByCardNumber = paidByCardNumber;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public String getPaidByCardNumber() {
        return paidByCardNumber;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
