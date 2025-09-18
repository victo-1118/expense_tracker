package io.github.victorjimenez.expense_tracker_api.services;
import org.springframework.stereotype.Service;
//
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;

import io.github.victorjimenez.expense_tracker_api.dto.BaseCardDTO;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.repository.BaseCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseTypeEntityService;
import io.github.victorjimenez.expense_tracker_api.services.DebitCardService;
import io.github.victorjimenez.expense_tracker_api.services.CreditCardService;
import io.github.victorjimenez.expense_tracker_api.services.PaymentHistoryService;
import io.github.victorjimenez.expense_tracker_api.repository.PaymentHistoryRepository;
import io.github.victorjimenez.expense_tracker_api.dto.PaymentHistoryDTO;





import java.util.List;
import java.util.Set;
import java.time.LocalDate;

import javax.swing.text.html.parser.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Sort;

@Service
public class BaseCardServiceImpl implements BaseCardService{

    private final BaseCardRepository baseCardRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseService expenseService;
    private final ExpenseTypeEntityService expenseTypeEntityService;
    private final DebitCardService debitCardService;
    private final CreditCardService creditCardService;
    private final PaymentHistoryService paymentHistoryService;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    public BaseCardServiceImpl(BaseCardRepository baseCardRepository, ExpenseTypeRepository expenseTypeRepository, 
        ExpenseRepository expenseRepository, @Lazy ExpenseService expenseService, ExpenseTypeEntityService expenseTypeEntityService
        , DebitCardService debitCardService, CreditCardService creditCardService, PaymentHistoryService paymentHistoryService,
        PaymentHistoryRepository paymentHistoryRepository) {
        this.baseCardRepository = baseCardRepository;
        this.expenseTypeRepository = expenseTypeRepository;
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
        this.expenseTypeEntityService = expenseTypeEntityService;
        this.debitCardService = debitCardService;
        this.creditCardService = creditCardService;
        this.paymentHistoryService = paymentHistoryService;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }
    //"Create" Operation

    @Override
    @Transactional
    public void create(BaseCardDTO baseCardDTO, BaseCard baseCard){
        baseCard.setCardNumber(baseCardDTO.getCardNumber());
        baseCard.setCardProvider(baseCardDTO.getCardProvider());
        baseCard.setBalance(baseCardDTO.getBalance());
        for (String baseFeeString : baseCardDTO.getBaseFees().keySet()) {
            BaseCardFeesType feeType = BaseCardFeesType.fromString(baseFeeString);
            baseCard.addFee(feeType, baseCardDTO.getBaseFees().get(baseFeeString));
        }
        addExpenseTypeToPayFor(baseCard, baseCardDTO.getExpenseTypeToPayForIds());
        baseCardRepository.save(baseCard);
        
    }
    //Read Operations
    @Override
    public List<BaseCard> findAll(Sort sort) {
        return baseCardRepository.findAll(sort);
    }

    @Override
    public List<BaseCard> findByCardProvider(String cardProvider) {
        return baseCardRepository.findByCardProvider(cardProvider);
    }

    @Override
    public List<BaseCard> findByBalance(double balance) {
        return baseCardRepository.findByBalance(balance);
    }

    @Override
    public List<BaseCard> findByBalanceBetween(double minBalance, double maxBalance) {
        return baseCardRepository.findByBalanceBetween(minBalance, maxBalance);
    }

    @Override
    public List<BaseCard> findByBaseFeeType(String feeType) {
        return baseCardRepository.findByBaseFeeType(feeType);
    }

    @Override
    public List<BaseCard> findByBaseFeeAmount(double feeAmount) {
        return baseCardRepository.findByBaseFeeAmount(feeAmount);
    }

    @Override
    public List<BaseCard> findByBaseFeeAmountBetween(double minFeeAmount, double maxFeeAmount) {
        return baseCardRepository.findByBaseFeeAmountBetween(minFeeAmount, maxFeeAmount);
    }

    @Override
    public List<BaseCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds) {
        return baseCardRepository.findByExpenseTypeToPayFor_IdIn(expenseTypeIds);
    }

    @Override
    public List<BaseCard> findByAllExpenseTypes (List<Long> expenseTypeIds, int minSize) {
        return baseCardRepository.findByAllExpenseTypes(expenseTypeIds, minSize);
    }

    @Override
    public List<BaseCard> findByEligibleExpenses_IdIn(List<Long> expenseIds) {
        return baseCardRepository.findByEligibleExpenses_IdIn(expenseIds);
    }

    @Override
    public List<BaseCard> findByAllExpenses(List<Long> expenseIds, int minSize) {
        return baseCardRepository.findByAllExpenses(expenseIds, minSize);
    }
    //Write Operations
    // @Override
    // @Transactional
    // public void delete(String baseCardNumber) {
    //     BaseCard baseCard = baseCardRepository.findById(baseCardNumber).orElseThrow(() -> new EntityNotFoundException("Base card with card number " + baseCardNumber + " not found"));
    //     List<ExpenseTypeEntity> expensesList = new ArrayList<ExpenseTypeEntity>(baseCard.getExpenseTypeToPayFor());
    //     List<Long> expensesIdsList = new ArrayList<Long>();
    //     for (ExpenseTypeEntity expenseTypeEntity : expensesList) {
    //         expensesIdsList.add(expenseTypeEntity.getId());
    //     }
    //     expenseService.removeEligibleCard(baseCard, expensesIdsList);
    //     expenseTypeEntityService.removeBaseCard(baseCard, expensesIdsList);
    //     baseCardRepository.deleteById(baseCardNumber);

        
    // }
    @Override
    public void delete(String baseCardNumber){
        BaseCard baseCard = baseCardRepository.findById(baseCardNumber).orElseThrow(() -> new EntityNotFoundException("Base card with card number " + baseCardNumber + " not found"));
        baseCard.setActive(false);
        baseCardRepository.save(baseCard);
    }
    @Override
    @Transactional
    public void updateBalance(String baseCardNumber, Double balance) {
        BaseCard baseCard = baseCardRepository.findByCardNumber(baseCardNumber);
        baseCard.setBalance(balance);
        baseCardRepository.save(baseCard);
    }
    @Override
    @Transactional
    public void addBaseFee(String baseCardNumber, String feeType, Double feeAmount) {
        BaseCard baseCard = baseCardRepository.findByCardNumber(baseCardNumber);
        baseCard.addFee(BaseCardFeesType.fromString(feeType), feeAmount);
        baseCardRepository.save(baseCard);
    }
    @Override
    @Transactional
    public void removeBaseFee(String baseCardNumber, String feeType) {
        try{
            BaseCard baseCard = baseCardRepository.findByCardNumber(baseCardNumber);    
            baseCard.removeFee(BaseCardFeesType.fromString(feeType));
            baseCardRepository.save(baseCard);
        }
        catch(NullPointerException e){
            throw new NullPointerException("fee type is not in the base card");
        }
    }

    @Override
    @Transactional
    public void addExpenseTypeToPayFor(BaseCard baseCard, List<Long> expenseTypeId) {

        for (Long id : expenseTypeId) {
            ExpenseTypeEntity expenseType =expenseTypeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + id + " not found"));
            baseCard.addExpenseTypeToPayFor(expenseType);
            addEligibleExpenses(baseCard, id);
        }
        expenseTypeEntityService.addBaseCards(baseCard, expenseTypeId);
        baseCardRepository.save(baseCard);
        
    }
        
        
    
    //Since we are updating the base card this means that eligible expenses have already been set
    //This means that we only need to add eligible expense if we have new expense categorys to pay for
    //Also we have other helper methods for frequent updates. Keeping this in mind lets create the helper methods first
    // will have to fix this method. well no maybe not this method but for other update methods
    // keep in mind that they might not actually maintain the relationship? Those other update methods 
    // should call helper methods.
    @Override
    @Transactional
    public BaseCard update(BaseCardDTO baseCardDTO, BaseCard baseCard) {
        
        if (baseCardDTO.getCardProvider() != null) {
            baseCard.setCardProvider(baseCardDTO.getCardProvider());
        }
        if (baseCardDTO.getCardNumber() != null) {
            baseCard.setBalance(baseCardDTO.getBalance());
        }
        if (baseCardDTO.getBaseFees() != null) {
            for (String baseFeeString : baseCardDTO.getBaseFees().keySet()) {
                BaseCardFeesType feeType = BaseCardFeesType.fromString(baseFeeString);
                baseCard.addFee(feeType, baseCardDTO.getBaseFees().get(baseFeeString));
            }
        }
        
        if (baseCardDTO.getExpenseTypeToPayForIds() != null) {
            addExpenseTypeToPayFor(baseCard, baseCardDTO.getExpenseTypeToPayForIds());
        }
        baseCardRepository.save(baseCard);
        
        return baseCard;
    }
    
    //Business Logic
    @Override
    @Transactional
    public void addEligibleExpenses(BaseCard baseCard, Long expenseTypeId) {
        //well since we have the list of expenses with the correct expense type here why not just add that as the argument

        List<Expense> expenses = expenseRepository.findByExpenseType_IdIn(List.of(expenseTypeId)); 
        for (Expense expense : expenses) {
            baseCard.addExpense(expense);
        }
        //in that case lets change the parameter
        expenseService.addEligibleCard(baseCard, expenses);
        baseCardRepository.save(baseCard);
    }
    // back in the removeExpenseTypeToPayFor function we only use baseCard variable here
    // this doesnt make sense? So lets refactor this and call this method not in the previously mentioned function
    // 
    @Transactional
    @Override
    public void removeEligibleExpenses(BaseCard baseCard, List<Long> expenseTypeId) {
        Set<Expense> originalList = baseCard.getEligibleExpenses();
        List<Expense> expensesToGetRidOf = expenseRepository.findByExpenseType_IdIn(expenseTypeId);
        originalList.removeAll(expensesToGetRidOf);
        // I plan to also call the removeEligibleCard function here
        baseCardRepository.save(baseCard);
        
    }
    @Override
    @Transactional
    public void addEligibleExpense(Expense expense, List<BaseCard> baseCards) {
        for (BaseCard baseCard : baseCards) {
            baseCard.addExpense(expense);
        }
        baseCardRepository.saveAll(baseCards);
    }

    @Override
    @Transactional
    public void removeEligibleExpense(List<BaseCard> baseCards, Long expenseId) {
        for (BaseCard baseCard : baseCards) {
            Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));
            baseCard.removeExpense(expense);
        }
        baseCardRepository.saveAll(baseCards);
    }


    //Let leave this here for now and work on the ExpenseServiceImpl file

    @Override
    public Expense chargeFee(String baseCardNumber, String feeType, LocalDate date) {
        BaseCard baseCard = baseCardRepository.findById(baseCardNumber).orElseThrow(() -> new EntityNotFoundException("Base card with card number " + baseCardNumber + " not found"));
        
        double expenseAmount = (baseCard.getFee(BaseCardFeesType.fromString(feeType)));
        ExpenseTypeEntity feeExpenseTypeEntity = expenseTypeRepository.findByExpenseType(feeType);
        ExpenseDTO feeExpense = new ExpenseDTO(null,feeType, expenseAmount, date, false, null, feeExpenseTypeEntity.getId());
        return expenseService.create(feeExpense);
    }

    

    @Transactional
    @Override
    public void removePaymentHistory(PaymentHistory paymentHistory, BaseCard baseCard){
        baseCard.removePaymentHistory(paymentHistory);
    }

    @Transactional 
    @Override
    public void addPaymentHistory(PaymentHistory paymentHistory, BaseCard baseCard){
        baseCard.addPaymentHistory(paymentHistory);
    }
    

    // probably not going to get much use out of this because we are now using soft delete
    @Override
    @Transactional
    public void removeExpenseTypeToPayFor(BaseCard baseCard, List<Long> expenseTypeId, Boolean expenseDeleted) {
        for (Long id : expenseTypeId) {
            ExpenseTypeEntity expenseType =expenseTypeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + id + " not found"));
            baseCard.removeExpenseTypeToPayFor(expenseType);
            List<BaseCard> baseCards = baseCardRepository.findByExpenseTypeToPayFor_IdIn(expenseTypeId);

            
            if (!expenseDeleted) {
                expenseTypeEntityService.removeBaseCards(baseCards, id);
            }
            
        }
        
        removeEligibleExpenses(baseCard, expenseTypeId);

        
    }
    //what should actually happen when i delete an expense type instead of removing.
    //this specifically concerns Expense objects since lets say an object has an expense type
    //and the expense type is deleted then the expense object should be deleted no?
    // i found out i could also make it null and thats the most common practice

    // ok going to actually need to create PaymentHistory service implementation here
 
}
