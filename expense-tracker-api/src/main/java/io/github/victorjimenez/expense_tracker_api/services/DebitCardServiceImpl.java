package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.repository.DebitCardRepository;
import io.github.victorjimenez.expense_tracker_api.dto.DebitCardDTO;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
import io.github.victorjimenez.expense_tracker_api.repository.CreditCardRepository;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;
import io.github.victorjimenez.expense_tracker_api.warnings.BudgetWarningException;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.services.BaseCardService;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
@Service
public class DebitCardServiceImpl implements DebitCardService {
    private final DebitCardRepository debitCardRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final BaseCardService baseCardService;
    private final ExpenseService expenseService;
    private final CreditCardRepository creditCardRepository;
    

    @Autowired
    public DebitCardServiceImpl (DebitCardRepository debitCardRepository, BaseCardService baseCardService,
     ExpenseRepository expenseRepository, ExpenseService expenseService, ExpenseTypeRepository expenseTypeRepository,
     CreditCardRepository creditCardRepository){
        this.debitCardRepository = debitCardRepository;
        this.baseCardService = baseCardService;
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
        this.expenseTypeRepository = expenseTypeRepository;
        this.creditCardRepository = creditCardRepository;
    }
    //Create operation
    @Override
    @Transactional
    public DebitCard create(DebitCardDTO debitCardDTO){
        DebitCard debitCard = new DebitCard();
        baseCardService.create(debitCardDTO, debitCard);
        debitCard.setBudget(debitCardDTO.getBudget());
        for (String debitFeeString : debitCardDTO.getDebitFees().keySet()){
            DebitCardFeesType debitFeeType = DebitCardFeesType.fromString(debitFeeString);
            debitCard.addDebitFee(debitFeeType, debitCardDTO.getDebitFees().get(debitFeeString));
        }

        return debitCardRepository.save(debitCard);

    }

    //Read operation
    @Override
    public List<DebitCard> findAll(Sort sort){
        return debitCardRepository.findAll(sort);
    }
    @Override
    public List<DebitCard> findByCardProvider(String cardProvider){
        return debitCardRepository.findByCardProvider(cardProvider);
    }    

    @Override
    public List<DebitCard> findByBalance(double balance){
        return debitCardRepository.findByBalance(balance);
    }

    @Override
    public List<DebitCard> findByBalanceBetween(double minBalance, double maxBalance){
        return debitCardRepository.findByBalanceBetween(minBalance, maxBalance);
    }

    @Override
    public List<DebitCard> findByBudget(double budget){
        return debitCardRepository.findByBudget(budget);
    }

    @Override
    public List<DebitCard> findByBudgetBetween(double minBudget, double maxBudget){
        return debitCardRepository.findByBudgetBetween(minBudget, maxBudget);
    }

    @Override
    public List<DebitCard> findByBaseFeeType(String feeType){
        return debitCardRepository.findByBaseFeeType(BaseCardFeesType.fromString(feeType));
    }
    
    @Override
    public List<DebitCard> findByDebitFeeType(String feeType){
        return debitCardRepository.findByDebitFeeType(feeType);
    }

    @Override
    public List<DebitCard> findByBaseFeeAmount(double feeAmount){
        return debitCardRepository.findByBaseFeeAmount(feeAmount);
    }

    @Override
    public List<DebitCard> findByDebitFeeAmount(double feeAmount){
        return debitCardRepository.findByDebitFeeAmount(feeAmount);
    }

    @Override
    public List<DebitCard> findByDebitFeeAmountBetween(double minFeeAmount, double maxFeeAmount){
        return debitCardRepository.findByDebitFeeAmountBetween(minFeeAmount, maxFeeAmount);
    }

    @Override
    public List<DebitCard> findByBaseFeeAmountBetween(double minFeeAmount, double maxFeeAmount){
        return debitCardRepository.findByBaseFeeAmountBetween(minFeeAmount, maxFeeAmount);
    }

    @Override
    public List<DebitCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds){
        return debitCardRepository.findByExpenseTypeToPayFor_IdIn(expenseTypeIds);
    }
    @Override
    public List<DebitCard> findByAllExpenseTypes(List<Long> expenseTypeIds){
        return debitCardRepository.findByAllExpenseTypes(expenseTypeIds, expenseTypeIds.size());
    }

    @Override
    public List<DebitCard> findByAllExpenses(List<Long> expenseTypeIds){
        return debitCardRepository.findByAllExpenseTypes(expenseTypeIds, expenseTypeIds.size());
    }

    @Override
    public List<DebitCard> findByEligibleExpenses_IdIn(List<Long> expenseIds){
        return debitCardRepository.findByEligibleExpenses_IdIn(expenseIds);
    }



    //Write operation
    @Override
    @Transactional
    public DebitCard update(DebitCardDTO debitCardDTO){
        DebitCard debitCard = debitCardRepository.findById(debitCardDTO.getCardNumber()).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        baseCardService.update(debitCardDTO, debitCard);
        if (debitCardDTO.getBudget() != null){
             debitCard.setBudget(debitCardDTO.getBudget());
        }
        if (debitCardDTO.getDebitFees() != null){
            for (String debitFeeString : debitCardDTO.getDebitFees().keySet()){
                DebitCardFeesType debitFeeType = DebitCardFeesType.fromString(debitFeeString);
                debitCard.addDebitFee(debitFeeType, debitCardDTO.getBaseFees().get(debitFeeString));
            }
        }
        return debitCardRepository.save(debitCard);
    }

    // @Override
    // public void delete(String cardNumber){
    //     debitCardRepository.deleteById(cardNumber);
    // }
    @Override
    public void delete(String cardNumber){
        DebitCard debitCard = debitCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        debitCard.setActive(false);
        debitCardRepository.save(debitCard);
    }

    @Override
    @Transactional
    public void addDebitFee(String cardNumber, String feeType, Double fee){
        DebitCard debitCard = debitCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        debitCard.addDebitFee(DebitCardFeesType.fromString(feeType), fee);
        debitCardRepository.save(debitCard);
    }

    @Override
    @Transactional
    public void removeDebitFee(String cardNumber, String feeType){
        DebitCard debitCard = debitCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        debitCard.removeDebitFee(DebitCardFeesType.fromString(feeType));
        debitCardRepository.save(debitCard);
    }

    @Override
    public Expense chargeDebitFee(String cardNumber, String feeType, LocalDate date){
        DebitCard debitCard = debitCardRepository.findById(cardNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        
        double expenseAmount = (debitCard.getDebitFee(DebitCardFeesType.fromString(feeType)));
        ExpenseTypeEntity feeExpenseTypeEntity = expenseTypeRepository.findByExpenseType(feeType);
        ExpenseDTO feeExpense = new ExpenseDTO(null,feeType, expenseAmount, date, false, null, feeExpenseTypeEntity.getId());
        return expenseService.create(feeExpense);
    }

    @Override
    public void transferFundsDebit(String debitCardSenderNumber, String debitCardReceiverNumber, double amount, Boolean ignoreWarning){
        DebitCard debitCardSender = debitCardRepository.findById(debitCardSenderNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        DebitCard debitCardReceiver = debitCardRepository.findById(debitCardReceiverNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        if (debitCardSender.getBalance() < amount){
            throw new IllegalArgumentException(debitCardSenderNumber + " does not have enough money to transfer to " + debitCardReceiverNumber);
        }
        BigDecimal expenseAmount = BigDecimal.valueOf(amount);
        BigDecimal debitCardSenderBalance = BigDecimal.valueOf(debitCardSender.getBalance());
        BigDecimal debitCardReceiverBalance = BigDecimal.valueOf(debitCardReceiver.getBalance());

        BigDecimal newDebitCardSenderBalance = debitCardSenderBalance.subtract(expenseAmount);
        BigDecimal newDebitCardReceiverBalance = debitCardReceiverBalance.add(expenseAmount);
        if (newDebitCardSenderBalance.compareTo(BigDecimal.valueOf(debitCardSender.getBudget())) < 0 && !ignoreWarning){
            throw new BudgetWarningException(debitCardReceiverNumber + " will exceed budget.");
        }

        debitCardSender.setBalance(newDebitCardSenderBalance.doubleValue());
        debitCardReceiver.setBalance(newDebitCardReceiverBalance.doubleValue());
        debitCardRepository.save(debitCardSender);
        debitCardRepository.save(debitCardReceiver);
    }

    @Override
    public void transferFundsCredit(String debitCardSenderNumber, String creditCardReceiverNumber, double amount, Boolean ignoreWarning){
        DebitCard debitCardSender = debitCardRepository.findById(debitCardSenderNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        CreditCard creditCardReceiver = creditCardRepository.findById(creditCardReceiverNumber).orElseThrow(() -> new EntityNotFoundException("card could not be found"));
        if (debitCardSender.getBalance() < amount){
            throw new IllegalArgumentException(debitCardSenderNumber + " does not have enough money to transfer to " + creditCardReceiverNumber);
        }
        BigDecimal expenseAmount = BigDecimal.valueOf(amount);
        BigDecimal debitCardSenderBalance = BigDecimal.valueOf(debitCardSender.getBalance());
        BigDecimal creditCardReceiverBalance = BigDecimal.valueOf(creditCardReceiver.getBalance());

        BigDecimal newDebitCardSenderBalance = debitCardSenderBalance.subtract(expenseAmount);
        BigDecimal newCreditCardReceiverBalance = creditCardReceiverBalance.add(expenseAmount);
        if (newDebitCardSenderBalance.compareTo(BigDecimal.valueOf(debitCardSender.getBudget())) < 0 && !ignoreWarning){
            throw new BudgetWarningException(debitCardSenderNumber + " will exceed budget.");
        }
        if (newCreditCardReceiverBalance.compareTo(BigDecimal.valueOf(0)) > 0){
            throw new IllegalArgumentException("Credit card has been overpaid.");
        }        
        debitCardSender.setBalance(newDebitCardSenderBalance.doubleValue());
        creditCardReceiver.setBalance(newCreditCardReceiverBalance.doubleValue());
        debitCardRepository.save(debitCardSender);
        creditCardRepository.save(creditCardReceiver);
    }

    @Override
    public Boolean approveExpensePaymentBalance(DebitCard debitCard, BigDecimal expenseAmount, Boolean ignoreWarning)  {
        if (BigDecimal.valueOf(debitCard.getBudget()).compareTo(expenseAmount) < 0) {
            throw new BudgetWarningException("Budget Warning");
        }

        if (BigDecimal.valueOf(debitCard.getBalance()).compareTo(expenseAmount) > 0) {
            return true;
        }
        return false;
    }

    


    
}
