package io.github.victorjimenez.expense_tracker_api.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;


import io.github.victorjimenez.expense_tracker_api.dto.PaymentHistoryDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.repository.BaseCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.CreditCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.DebitCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
import io.github.victorjimenez.expense_tracker_api.repository.PaymentHistoryRepository;
import io.github.victorjimenez.expense_tracker_api.snapshots.BaseCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.CreditCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.DebitCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.ExpenseSnapshot;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import projection.CardUsageSummary;
import projection.ExpenseSummary;
@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService{
    ExpenseService expenseService;
    DebitCardService debitCardService;
    CreditCardService creditCardService;
    ExpenseRepository expenseRepository;
    DebitCardRepository debitCardRepository;
    CreditCardRepository creditCardRepository;
    BaseCardRepository baseCardRepository;
    PaymentHistoryRepository paymentHistoryRepository;
    BaseCardService baseCardService;
    @Autowired
    public PaymentHistoryServiceImpl(ExpenseService expenseService, DebitCardService debitCardService, CreditCardService creditCardService,
            ExpenseRepository expenseRepository, DebitCardRepository debitCardRepository, CreditCardRepository creditCardRepository,
            BaseCardRepository baseCardRepository, PaymentHistoryRepository paymentHistoryRepository, BaseCardService baseCardService) {

        this.expenseService = expenseService;
        this.debitCardService = debitCardService;
        this.creditCardService = creditCardService;
        this.expenseRepository = expenseRepository;
        this.debitCardRepository = debitCardRepository;
        this.creditCardRepository = creditCardRepository;
        this.baseCardRepository = baseCardRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.baseCardService = baseCardService;
    }
    //Create Operation
    //before thinking about creating a payment history we have to consider the relationships and the snapshot
    // right now our payment history DTO gives us the expense id, amount paid, payment date and the card number
    // is this enough?
    // well first of all we would have to use the ids to get the expense and base card objects.
    // besides that everything else is fine.
    // however we also want to create our snapshots. This is where we take the objects and turn them into snapshots.
    // fortunately the constructor of the snapshots takes care of this.
    // so we do have all the stuff so lets implement it
    // also make sure to maintain relationships.
    @Override
    @Transactional
    public PaymentHistory create(PaymentHistoryDTO paymentHistoryDTO) {
        Expense expense = expenseRepository.findById(paymentHistoryDTO.getExpenseId()).orElseThrow(() -> new EntityNotFoundException("Expense with id " + paymentHistoryDTO.getExpenseId() + " not found"));
        BaseCard basecard = baseCardRepository.findById(paymentHistoryDTO.getPaidByCardNumber()).orElseThrow(() -> new EntityNotFoundException("BaseCard with card number " + paymentHistoryDTO.getPaidByCardNumber() + " not found"));
        ExpenseSnapshot expenseSnapshot= new ExpenseSnapshot(expense);
        BaseCardSnapshot baseCardSnapshot;
        BaseCard card;
        PaymentHistory paymentHistory;
        if (basecard instanceof DebitCard) {
            card = (DebitCard) basecard;
            baseCardSnapshot = new DebitCardSnapshot((DebitCard) basecard);
            paymentHistory = new PaymentHistory(expense, paymentHistoryDTO.getAmountPaid(), paymentHistoryDTO.getPaymentDate(), card, (DebitCardSnapshot) baseCardSnapshot, expenseSnapshot);
            
        }
       else{
            baseCardSnapshot = new CreditCardSnapshot((CreditCard) basecard);
            card = (CreditCard) basecard;
            paymentHistory = new PaymentHistory(expense, paymentHistoryDTO.getAmountPaid(), paymentHistoryDTO.getPaymentDate(), card, (CreditCardSnapshot) baseCardSnapshot, expenseSnapshot);
       }

        
        expenseService.addPaymentHistory(paymentHistory, expense);
        baseCardService.addPaymentHistory(paymentHistory, basecard);

        return paymentHistoryRepository.save(paymentHistory);
    }
    //Read Operation
    
    public List<PaymentHistory> findByAmountPaid(double amountPaid) {
        return paymentHistoryRepository.findByAmountPaid(amountPaid);
    }

    public List<PaymentHistory> findByAmountPaidBetween(double minAmountPaid, double maxAmountPaid) {
        return paymentHistoryRepository.findByAmountPaidBetween(minAmountPaid, maxAmountPaid);

    }

    public List<PaymentHistory> findByPaymentDate(LocalDate datePaid) {
        return paymentHistoryRepository.findByPaymentDate(datePaid);
    }

    public List<PaymentHistory> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate) {
        return paymentHistoryRepository.findByPaymentDateBetween(startDate, endDate);
    }

    // Write Operations

    @Override
    @Transactional
    public void delete(Long id) {
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("PaymentHistory with id " + id + " not found"));
        baseCardService.removePaymentHistory(paymentHistory, paymentHistory.getPaidByCard());
        expenseService.removePaymentHistory(paymentHistory, paymentHistory.getExpensePaid());
        // before this we delete the relationship with Expense and BaseCard object.
        paymentHistoryRepository.deleteById(id);
    }

    // Business Logic

    @Override
    public Page<ExpenseSummary> getExpenseSummaryPerCategory(Pageable pageable) {
        return paymentHistoryRepository.summarizeExpensesType(pageable);
    }

    @Override
    public Page<ExpenseSummary> getExpenseSummaryPerCategoryBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return paymentHistoryRepository.summarizeExpensesTypeBetweenDates(startDate, endDate, pageable);
    }

    @Override
    public ExpenseSummary getExpenseSummaryBetweenDates(LocalDate startDate, LocalDate endDate) {
        return paymentHistoryRepository.summarizeExpensesBetweenDates(startDate, endDate);
    }

    @Override
    public Page<CardUsageSummary> getCardUsageSummary(Pageable pageable) {
        return paymentHistoryRepository.summarizeCardUsage(pageable);
    }

    @Override
    public Page<CardUsageSummary> getCardUsageSummaryBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return paymentHistoryRepository.summarizeCardUsageBetweenDates(startDate, endDate, pageable);
    }

    @Override
    public Page<CardUsageSummary> getAverageTransactionAmountPerCard(Pageable pageable) {
        return paymentHistoryRepository.findAverageTransactionAmountPerCard(pageable);
    }

    @Override
    public Page<CardUsageSummary> getAverageTransactionAmountPerCardBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return paymentHistoryRepository.findAverageTransactionAmountPerCardBetweenDates(startDate, endDate, pageable);
    }





}   