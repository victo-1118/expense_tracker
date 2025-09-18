package io.github.victorjimenez.expense_tracker_api.dto;
import java.time.LocalDate;
// where is id field for payment history itself
public class PaymentHistoryDTO {
    private Long expenseId;
    private double amountPaid;
    private LocalDate paymentDate;
    private String paidByCardNumber;
    public PaymentHistoryDTO(Long expenseId, double amountPaid, LocalDate paymentDate, String paidByCardNumber) {
        this.expenseId = expenseId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paidByCardNumber = paidByCardNumber;
    }
    public Long getExpenseId() {
        return expenseId;
    }
    public double getAmountPaid() {
        return amountPaid;
    }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    public String getPaidByCardNumber() {
        return paidByCardNumber;
    }
}
