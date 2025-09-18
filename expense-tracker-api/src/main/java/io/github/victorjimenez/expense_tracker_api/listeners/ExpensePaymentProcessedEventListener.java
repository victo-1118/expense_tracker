package io.github.victorjimenez.expense_tracker_api.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import io.github.victorjimenez.expense_tracker_api.dto.PaymentHistoryDTO;
import io.github.victorjimenez.expense_tracker_api.events.ExpensePaymentProcessedEvent;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.repository.BaseCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;
import io.github.victorjimenez.expense_tracker_api.services.PaymentHistoryService;

@Component
public class ExpensePaymentProcessedEventListener {
    @Autowired
    private PaymentHistoryService paymentHistoryService;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private BaseCardRepository baseCardRepository;

    /**
     * Handles the creation of a payment history record when an expense is paid for. Listens to ExpensePaymentProcessedEvent.
     * The event is published by ExpensePaymentServiceImpl when the payment for an expense is processed. The event contains the expense ID, amount paid, payment date, and the card number used to pay for the expense.
     * This listener creates a new payment history record with the given details and associates it with the expense and the base card used to pay for it.
     * The payment history record is then saved to the database.
     */
    @TransactionalEventListener
    public void handlePaymentHistoryCreationOnExpensePaymentProcessedEvent(ExpensePaymentProcessedEvent event) {
        System.out.println("ExpensePaymentProcessedEvent handled by ExpensePaymentProcessedEventListener.");
        PaymentHistoryDTO paymentHistoryDTO = new PaymentHistoryDTO(event.getExpenseId(), event.getAmountPaid(), event.getPaymentDate(), event.getPaidByCardNumber());
        PaymentHistory paymentHistory = paymentHistoryService.create(paymentHistoryDTO);
        Expense expense = expenseRepository.findById(event.getExpenseId()).orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + event.getExpenseId()));
        expense.addPaymentHistory(paymentHistory);
        expenseRepository.save(expense);
        BaseCard baseCard = baseCardRepository.findById(event.getPaidByCardNumber()).orElseThrow(() -> new IllegalArgumentException("Base card not found with card number: " + event.getPaidByCardNumber()));
        baseCard.addPaymentHistory(paymentHistory);
        baseCardRepository.save(baseCard);
    }
    // definetly would have to add more event listeners later on but this is good for now.
}
