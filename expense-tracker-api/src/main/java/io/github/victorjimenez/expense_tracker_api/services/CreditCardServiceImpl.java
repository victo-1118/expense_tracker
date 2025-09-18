package io.github.victorjimenez.expense_tracker_api.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.victorjimenez.expense_tracker_api.dto.CreditCardDTO;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.CreditCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.services.BaseCardService;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;
import io.github.victorjimenez.expense_tracker_api.warnings.PersonalLimitWarningException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.victorjimenez.expense_tracker_api.repository.CreditCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
@Service
public class CreditCardServiceImpl implements CreditCardService{
    private final CreditCardRepository creditCardRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final BaseCardService baseCardService;
    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;
    @Autowired
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, BaseCardService baseCardService
    ,ExpenseService expenseService, ExpenseTypeRepository expenseTypeRepository, ExpenseRepository expenseRepository){ 
        this.creditCardRepository = creditCardRepository;
        this.baseCardService = baseCardService;
        this.expenseService = expenseService;
        this.expenseTypeRepository = expenseTypeRepository;
        this.expenseRepository = expenseRepository;
    }    

    @Override
    @Transactional
    public CreditCard create(CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard();
        baseCardService.create(creditCardDTO, creditCard);
        creditCard.setCreditLimit(creditCardDTO.getCreditLimit());
        creditCard.setPersonaLimit(creditCardDTO.getPersonalLimit());
        creditCard.setInterestRate(creditCardDTO.getInterestRate());
        creditCard.setMinimumPayment(creditCardDTO.getMinimumPayment());
        creditCard.setPeriodDueDateAfterCycle(creditCardDTO.getPeriodDueDateAfterCycle());
        creditCard.setStartOfBillingCycle(creditCardDTO.getStartOfBillingCycle());
        creditCard.setBillingCyclePeriod(creditCardDTO.getBillingCyclePeriod());
        creditCard.setMinimumPaymentDynamic(creditCardDTO.getMinimumPaymentDynamic());

        for (String creditFeeString : creditCardDTO.getCreditFees().keySet()) {
            CreditCardFeesType creditCardFeesType =  CreditCardFeesType.fromString(creditFeeString);
            creditCard.addCreditFee(creditCardFeesType, creditCardDTO.getCreditFees().get(creditFeeString));
        }
        return creditCardRepository.save(creditCard);
    }

    //Read Operations
    @Override
    public List<CreditCard> findAll(Sort sort) {
        return creditCardRepository.findAll();
    }

    @Override
    public List<CreditCard> findByCardProvider(String cardProvider) {
        return creditCardRepository.findByCardProvider(cardProvider);
    }

    @Override
    public List<CreditCard> findByBalance(double balance) {
        return creditCardRepository.findByBalance(balance);
    }

    @Override
    public List<CreditCard> findByBalanceBetween(double minBalance, double maxBalance) {
        return creditCardRepository.findByBalanceBetween(minBalance, maxBalance);
    }

    @Override
    public List<CreditCard> findByCreditLimit(double creditLimit) {
        return creditCardRepository.findByCreditLimit(creditLimit);
    }

    @Override
    public List<CreditCard> findByCreditLimitBetween(double minCreditLimit, double maxCreditLimit) {
        return creditCardRepository.findByCreditLimitBetween(minCreditLimit, maxCreditLimit);
    }

    @Override
    public List<CreditCard> findByInterestRate(double interestRate) {
        return creditCardRepository.findByInterestRate(interestRate);
    }

    @Override
    public List<CreditCard> findByInterestRateBetween(double minInterestRate, double maxInterestRate) {
        return creditCardRepository.findByInterestRateBetween(minInterestRate, maxInterestRate);
    }

    @Override
    public List<CreditCard> findByMinimumPayment(double minimumPayment) {
        return creditCardRepository.findByMinimumPayment(minimumPayment);
    }

    @Override
    public List<CreditCard> findByMinimumPaymentBetween(double minMinimumPayment, double maxMinimumPayment) {
        return creditCardRepository.findByMinimumPaymentBetween(minMinimumPayment, maxMinimumPayment);
    }

    @Override
    public List<CreditCard> findByCreditFeeType(String feeType) {
        return creditCardRepository.findByCreditFeeType(feeType);
    }

    @Override
    public List<CreditCard> findByCreditFeeAmount(double feeAmount) {
        return creditCardRepository.findByCreditFeeAmount(feeAmount);
    }

    @Override
    public List<CreditCard> findByCreditFeeAmountBetween(double minFeeAmount, double maxFeeAmount) {
        return creditCardRepository.findByCreditFeeAmountBetween(minFeeAmount, maxFeeAmount);
    }

    @Override
    public List<CreditCard> findByAllExpenseTypes(List<Long> expenseTypeIds) {
        return creditCardRepository.findByAllExpenseTypes(expenseTypeIds.size(), expenseTypeIds);
    }

    @Override
    public List<CreditCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds) {
        return creditCardRepository.findByExpenseTypeToPayFor_IdIn(expenseTypeIds);
    }

    @Override
    public List<CreditCard> findByEligibleExpenses_IdIn(List<Long> expenseIds) {
        return creditCardRepository.findByEligibleExpenses_IdIn(expenseIds);
    }

    @Override
    public List<CreditCard> findByAllExpenses(List<Long> expenseIds) {
        return creditCardRepository.findByAllExpenses(expenseIds.size(), expenseIds);
    }

    @Override
    public List<CreditCard> findByStartOfBillingCycleAfter(LocalDate startOfBillingCycle) {
        return creditCardRepository.findByStartOfBillingCycleAfter(startOfBillingCycle);
    }

    //Write operation
    @Override
    @Transactional
    public CreditCard update(CreditCardDTO creditCardDTO) {
        CreditCard creditCard = creditCardRepository.findByCardNumber(creditCardDTO.getCardNumber());
        baseCardService.update(creditCardDTO, creditCard);
        if (creditCardDTO.getCreditLimit() != null){
            creditCard.setCreditLimit(creditCardDTO.getCreditLimit());
        }
        if (creditCardDTO.getPersonalLimit() != null){
            creditCard.setPersonaLimit(creditCardDTO.getPersonalLimit());
        }
        if (creditCardDTO.getInterestRate() != null){
            creditCard.setInterestRate(creditCardDTO.getInterestRate());
        }
        if (creditCardDTO.getMinimumPayment() != null){
            creditCard.setMinimumPayment(creditCardDTO.getMinimumPayment());
        }
        if (creditCardDTO.getPeriodDueDateAfterCycle() != null){
            creditCard.setPeriodDueDateAfterCycle(creditCardDTO.getPeriodDueDateAfterCycle());
        }
        if (creditCardDTO.getMinimumPaymentDynamic() != null){
            creditCard.setMinimumPaymentDynamic(creditCardDTO.getMinimumPaymentDynamic());
        }
        if (creditCardDTO.getStartOfBillingCycle() != null){
            creditCard.setStartOfBillingCycle(creditCardDTO.getStartOfBillingCycle());
        }
        if (creditCardDTO.getBillingCyclePeriod() != null){
            creditCard.setBillingCyclePeriod(creditCardDTO.getBillingCyclePeriod());
        }
        for (String creditFeeString : creditCardDTO.getCreditFees().keySet()) {
            CreditCardFeesType creditCardFeesType =  CreditCardFeesType.fromString(creditFeeString);
            creditCard.addCreditFee(creditCardFeesType, creditCardDTO.getCreditFees().get(creditFeeString));
        }
        return creditCardRepository.save(creditCard);
    }
    // @Override
    // public void delete(String cardNumber) {
    //     creditCardRepository.deleteById(cardNumber);
    // }
    @Override
    public void delete(String cardNumber) {
        CreditCard creditCard = creditCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        creditCard.setActive(false);
        creditCardRepository.save(creditCard);
    }

    @Override
    @Transactional
    public void addCreditFee(String cardNumber, String feeType, Double fee) {
        CreditCard creditCard = creditCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        creditCard.addCreditFee(CreditCardFeesType.fromString(feeType), fee);
        creditCardRepository.save(creditCard);
    }

    @Override
    @Transactional
    public void removeCreditFee(String cardNumber, String feeType) {
        CreditCard creditCard = creditCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        creditCard.removeCreditFee(CreditCardFeesType.fromString(feeType));
        creditCardRepository.save(creditCard);
    }
    // how are we gonna charge credit fees?
    // obviously its not going to be here
    // this should be an event listener. whenever user recieves warning (so reactive not proactive).
    @Override
    @Transactional
    public Expense chargeCreditFee(String cardNumber, String feeType, LocalDate date) {
        CreditCard creditCard = creditCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        double expenseAmount = creditCard.getCreditFee(CreditCardFeesType.fromString(feeType));
        ExpenseTypeEntity feeExpenseTypeEntity = expenseTypeRepository.findByExpenseType(feeType);
        ExpenseDTO feeExpense = new ExpenseDTO(null, feeType, expenseAmount, date, false, null, feeExpenseTypeEntity.getId());
        return expenseService.create(feeExpense);
    }
    




}
