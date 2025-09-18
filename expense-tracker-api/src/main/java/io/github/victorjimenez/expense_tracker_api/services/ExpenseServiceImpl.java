package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
import io.github.victorjimenez.expense_tracker_api.repository.BaseCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.services.BaseCardService;
import io.github.victorjimenez.expense_tracker_api.warnings.OverPaymentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BaseCardRepository baseCardRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final BaseCardService baseCardService;
    @Autowired
    public ExpenseServiceImpl (ExpenseRepository expenseRepository, BaseCardRepository baseCardRepository, 
        ExpenseTypeRepository expenseTypeRepository, BaseCardService baseCardService) {
        this.expenseRepository = expenseRepository;
        this.baseCardRepository = baseCardRepository;
        this.expenseTypeRepository = expenseTypeRepository;
        this.baseCardService = baseCardService;
    }
    
    //Create Operation
    //Lets make sure that Create Operation is truly complete before making the other functions
    // seems like i currently have it so that I need to create a ExpenseTypeEntity before creating expense or cards
   @Override
   @Transactional
    public Expense create(ExpenseDTO expenseDTO) {
        // Find expense type and handle not-found case
        ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(expenseDTO.getExpenseTypeEntityId())
                .orElseThrow(() -> new EntityNotFoundException("ExpenseType not found with ID: " + expenseDTO.getExpenseTypeEntityId()));
        
        // Create and populate the expense
        Expense expense = new Expense();
        expense.setExpenseName(expenseDTO.getExpenseName());
        expense.setExpenseAmount(expenseDTO.getExpenseAmount());
        expense.setExpenseDate(expenseDTO.getExpenseDate());
        expense.setReoccurring(expenseDTO.isReoccurring());
        expense.setFrequency(expenseDTO.getFrequency());
        expense.setExpenseType(expenseTypeEntity);
        
        // Add eligible cards
        addEligibleCards(expense, expenseTypeEntity);
        // Add eligible expense to the baseCards
        // we can do this in addEligible cards since we have the list of baseCards there
        
        // Save and return
        return expenseRepository.save(expense);
    }

    //Read Operations
    @Override
    public List<Expense> findByExpenseType_Id(Long expenseTypeId) {
        return expenseRepository.findByExpenseType_Id(expenseTypeId);
    }

    @Override
    public List<Expense> findByExpenseAmount(double expenseAmount) {
        return expenseRepository.findByExpenseAmount(expenseAmount);
    }

    @Override
    public List<Expense> findByExpenseAmountBetween(double minAmount, double maxAmount) {
        return expenseRepository.findByExpenseAmountBetween(minAmount, maxAmount);
    }

    @Override
    public List<Expense> findByExpenseDate(LocalDate expenseDate) {
        return expenseRepository.findByExpenseDate(expenseDate);
    }

    @Override
    public List<Expense> findByExpenseDateBetween(LocalDate minDate, LocalDate maxDate) {
        return expenseRepository.findByExpenseDateBetween(minDate, maxDate);
    }

    @Override
    public List<Expense> findByExpenseName(String expenseName) {
        return expenseRepository.findByExpenseName(expenseName);
    }

    @Override
    public List<Expense> findByEligibleCards_CardNumber(List<String> cardNumber) {
        return expenseRepository.findByEligibleCards_CardNumber(cardNumber);
    }

    @Override
    public List<Expense> findByExpenseDateGreaterThanEqualAndExpenseType_IdIn(LocalDate startDate, List<Long> expenseTypeIds) {
        return expenseRepository.findByExpenseDateGreaterThanEqualAndExpenseType_IdIn(startDate, expenseTypeIds);
    }

    @Override
    public List<Expense> findByExpenseDateGreaterThanEqual(LocalDate startDate) {
        return expenseRepository.findByExpenseDateGreaterThanEqual(startDate);
    }

    //Write Operations
    // when would i want to delete an expense? This doesnt even make sense right?
    // ok so there are valid reasons to delete
    // how would we maintain the relationships then?
    // well this is a bit easier since we dont have to worry about ExpenseTypeEntity
    // since we can ignore that lets focus on the basecard relationship. All we would have to do then
    // is find a list of basecards witht he same type. Pass it through a function. Lets take a look at our remove
    // functions to see what kind of parameters they expect.
    // ok i see i dont have any function for that. in that case lets create on that accepts a list of base cards and
    // also accepts an expenseId
    // @Override
    // @Transactional
    // public void delete(Long expenseId) {
    //     List<BaseCard> baseCards = baseCardRepository.findByEligibleExpenses_IdIn(List.of(expenseId));
    //     baseCardService.removeEligibleExpense(baseCards, expenseId);
    //     expenseRepository.deleteById(expenseId);
    // }
    @Override
    public void delete(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));
        expense.setActive(false);
        expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void setExpenseAmount(Long expenseId, double expenseAmount) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));
        expense.setExpenseAmount(expenseAmount);
        expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void setExpenseDate(Long expenseId, LocalDate expenseDate) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));
        expense.setExpenseDate(expenseDate);
        expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void payingExpenseAmount(double amount, Expense expense) {
        BigDecimal decimalAmount = BigDecimal.valueOf(amount);
        if (decimalAmount.compareTo(BigDecimal.valueOf(expense.getExpenseAmount())) > 0) {
            throw new OverPaymentException("Cannot pay more than the expense amount.");
        }
        BigDecimal newAmount = BigDecimal.valueOf(expense.getExpenseAmount()).subtract(decimalAmount);
        expense.setExpenseAmount(newAmount.doubleValue());
        expenseRepository.save(expense);
    }
    @Override
    @Transactional
    public void checkForUpdateReoccurringExpense(LocalDate dateToday){
        List<Expense> expensesToUpdate = expenseRepository.findByExpenseDateAndReoccurring(dateToday, true);
        for (Expense expense : expensesToUpdate) {
            updateReoccuringExpense(expense);
            expenseRepository.save(expense);
        }
    }

    @Override
    @Transactional
    public void updateReoccuringExpense(Expense expense){
        expense.setExpenseDate(expense.getExpenseDate().plus(expense.getFrequency()));
    }


    @Override
    @Transactional
    public Expense update(ExpenseDTO expenseDTO){
        Expense expense = expenseRepository.findById(expenseDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseDTO.getId()));
        if (expenseDTO.getExpenseName() != null) {
            expense.setExpenseName(expenseDTO.getExpenseName());
        }
        if (expenseDTO.getExpenseAmount() != null) {
            expense.setExpenseAmount(expenseDTO.getExpenseAmount());
        }
        if (expenseDTO.getExpenseDate() != null) {
            expense.setExpenseDate(expenseDTO.getExpenseDate());
        }
        if (expenseDTO.isReoccurring() != null) {
            expense.setReoccurring(expenseDTO.isReoccurring());
        }
        if (expenseDTO.getFrequency() != null) {
            expense.setFrequency(expenseDTO.getFrequency());
        }
        if (expenseDTO.getExpenseTypeEntityId() != null) {
            ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(expenseDTO.getExpenseTypeEntityId())
                    .orElseThrow(() -> new EntityNotFoundException("ExpenseType not found with ID: " + expenseDTO.getExpenseTypeEntityId()));
            addEligibleCards(expense, expenseTypeEntity);
            expense.setExpenseType(expenseTypeEntity);
        }
        return expenseRepository.save(expense);
    }



    //addEligibleCard
    //now that i think about it i should have two functions
    //addEligibleCard and addEligibleCards
    //first one is for when a baseCard is created and the second one is for when an expense is created
    //in the first scenario we have created a baseCard and have added eligible expenses however we need to maintain both sides of the
    // relationship so we add the one baseCard to corresponding expenses.
    //in the second scenario we have created an expense and we need to add several baseCards to the expense
    //of course we would have corresponding functions but for adding eligible expense/s.

    //so what arguments should we pass in for addEligibleCard?
    // well since we know what baseCard we are adding we need to pass in the baseCard object.
    // what we dont know is to what expenses we are adding this baseCard too
    // so we need to pass in the baseCard object and then in the function determine what Expenses we are adding baseCard to
    
    //Busines Logic
    @Override
    @Transactional
    public void addEligibleCard(BaseCard baseCard, List<Expense> expensesWithCorrectTypes) {
        for (Expense expense : expensesWithCorrectTypes) {
            expense.addEligibleCard(baseCard);
        }
        // first lets find expenses with the expenseTypeId
        expenseRepository.saveAll(expensesWithCorrectTypes);
    }
    // what if we delete a baseCard?
    // we need to remove the baseCard from the corresponding expenses
    // this is much easier than deleting a expense type.
    
    @Override
    @Transactional
    public void addEligibleCards(Expense expense, ExpenseTypeEntity expenseTypeEntity) {
        List<BaseCard> baseCards = baseCardRepository.findByExpenseTypeToPayFor_IdIn(List.of(expenseTypeEntity.getId()));
        
        expense.setEligibleCards(new HashSet<>(baseCards));
        baseCardService.addEligibleExpense(expense, baseCards);
    }
    @Override
    @Transactional
    public void removeEligibleCard(BaseCard card, List<Long> expenseTypeId) {
        List<Expense> expenses = expenseRepository.findByExpenseType_IdIn(expenseTypeId);
        for (Expense expense : expenses) {
            expense.removeEligibleCard(card);
        }
        expenseRepository.saveAll(expenses);


    }

    @Override
    @Transactional
    public void nullifyExpenseType(Long expenseTypeId){
        List<Expense> expenses = expenseRepository.findByExpenseType_Id(expenseTypeId);
        for (Expense expense : expenses) {
            expense.setExpenseType(null);
        }
        expenseRepository.saveAll(expenses);
    }

    @Override 
    @Transactional
    public void removePaymentHistory(PaymentHistory paymentHistory, Expense expense) {
        expense.removePaymentHistory(paymentHistory);
    }

    @Override 
    @Transactional
    public void addPaymentHistory(PaymentHistory paymentHistory, Expense expense) {
        expense.addPaymentHistory(paymentHistory);
    }



    //Great i think we have correctly managed the relationship between these two but we are still missing something
    // We have relationships with ExpenseTypeEntity too. This serves as our category. However since the relationship
    // is only biderictional with BaseCard we dont have to do any more relationship management logic here.

    
    
}
