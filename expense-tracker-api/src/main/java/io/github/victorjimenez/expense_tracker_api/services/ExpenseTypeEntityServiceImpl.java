package io.github.victorjimenez.expense_tracker_api.services;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.victorjimenez.expense_tracker_api.dto.BaseCardDTO;
import io.github.victorjimenez.expense_tracker_api.dto.DebitCardDTO;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseTypeEntityDTO;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.warnings.BudgetWarningException;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.repository.BaseCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseRepository;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;



@Service
public class ExpenseTypeEntityServiceImpl implements ExpenseTypeEntityService {
    private final ExpenseTypeRepository expenseTypeRepository;
    private final PaymentHistoryService paymentHistoryService;
    private final BaseCardService baseCardService;
    private final DebitCardService debitCardService;
    private final ExpenseRepository expenseRepository;
    private final BaseCardRepository baseCardRepository;
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseTypeEntityServiceImpl(ExpenseTypeRepository expenseTypeRepository, PaymentHistoryService paymentHistoryService,
            BaseCardService baseCardService, DebitCardService debitCardService, ExpenseRepository expenseRepository,
            BaseCardRepository baseCardRepository, ExpenseService expenseService) {
        this.expenseTypeRepository = expenseTypeRepository;
        this.paymentHistoryService = paymentHistoryService;
        this.baseCardService = baseCardService;
        this.debitCardService = debitCardService;
        this.expenseRepository = expenseRepository;
        this.baseCardRepository = baseCardRepository;
        this.expenseService = expenseService;
    }
    //Create Operation
    public ExpenseTypeEntity create(ExpenseTypeEntityDTO expenseTypeEntityDTO) {
        ExpenseTypeEntity expenseTypeEntity = new ExpenseTypeEntity();
        expenseTypeEntity.setBudgetAmount(expenseTypeEntityDTO.getBudgetAmount());
        expenseTypeEntity.setExpenseType(expenseTypeEntityDTO.getExpenseType());


        return expenseTypeRepository.save(expenseTypeEntity);
    
    }

    //Read Operations
    @Override
    public List<ExpenseTypeEntity> findAll(Sort sort) {
        return expenseTypeRepository.findAll();
    }

    @Override
    public ExpenseTypeEntity findByExpenseType(String expenseType) {
        return expenseTypeRepository.findByExpenseType(expenseType);
    }

    @Override
    public ExpenseTypeEntity findById(Long id) {
        return expenseTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + id + " not found"));
    }

    

    //Write Operations
    //Ok i think ive done all necessary relationship management functions for now. now i just need to finish the
    // rest of the services. Will probably have to add PaymentHistory service too though now that I think about it
    // since when pay for expenses I need to create a payment history. For now lets just program service and work
    // on that when needed
    @Override
    public ExpenseTypeEntity update(ExpenseTypeEntityDTO expenseTypeEntityDTO) {
        if (expenseTypeEntityDTO.getId() == null) {
            throw new IllegalArgumentException("Expense type id cannot be null");
        }
        ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(expenseTypeEntityDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + expenseTypeEntityDTO.getId() + " not found"));
        if (expenseTypeEntityDTO.getExpenseType() != null) {
            expenseTypeEntity.setExpenseType(expenseTypeEntityDTO.getExpenseType());
        }
        if (expenseTypeEntityDTO.getBudgetAmount() != null) {
            expenseTypeEntity.setBudgetAmount(expenseTypeEntityDTO.getBudgetAmount());
        }
        return expenseTypeRepository.save(expenseTypeEntity);
    }

    @Override
    public void addBaseCards(BaseCard baseCard, List<Long> expenseTypeEntityIds) {
        for (Long id : expenseTypeEntityIds) {
            ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + id + " not found"));
            expenseTypeEntity.addBaseCard(baseCard);
        }
    }
    @Override
    @Transactional
    public void removeBaseCards(List<BaseCard> baseCards, Long expenseTypeEntityId) {
        ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(expenseTypeEntityId).orElseThrow(() -> new EntityNotFoundException("Expense type with id " + expenseTypeEntityId + " not found"));
        expenseTypeEntity.getBaseCards().removeAll(baseCards);
        expenseTypeRepository.save(expenseTypeEntity);
    }
    @Override
    @Transactional
    public void removeBaseCard(BaseCard baseCard, List<Long> expenseTypeEntityIds) {
        for (Long id : expenseTypeEntityIds) {
            ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + id + " not found"));
            expenseTypeEntity.removeBaseCard(baseCard);
            expenseTypeRepository.save(expenseTypeEntity);
        }
    }

    // @Override
    // @Transactional
    // public void delete(Long id) {
    //     List<BaseCard> baseCards = baseCardRepository.findByExpenseTypeToPayFor_IdIn(List.of(id));
    //     for (BaseCard baseCard : baseCards) {
    //         baseCardService.removeExpenseTypeToPayFor(baseCard, List.of(id), true);
    //     }
    //     //now create logic for Expense objects
    //     expenseService.nullifyExpenseType(id);


    //     expenseTypeRepository.deleteById(id);
    // }

    @Override 
    public void delete(Long id) {
        ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Expense type with id " + id + " not found"));
        expenseTypeEntity.setActive(false);
        expenseTypeRepository.save(expenseTypeEntity);
    }

    @Override
    public double getRemainingBudget(Long expenseTypeId, double totalExpenseAmount) {
        ExpenseTypeEntity expenseTypeEntity = expenseTypeRepository.findById(expenseTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Expense type with id " + expenseTypeId + " not found"));
        BigDecimal totalExpenseAmountBD = BigDecimal.valueOf(totalExpenseAmount);
        BigDecimal budgetAmountBD = BigDecimal.valueOf(expenseTypeEntity.getBudgetAmount());
        BigDecimal remainingBudget = budgetAmountBD.subtract(totalExpenseAmountBD);
        if (remainingBudget.compareTo(BigDecimal.ZERO) < 0) {
            throw new BudgetWarningException("category will exceed budget.");
        }
        return remainingBudget.doubleValue();

    }

    @Override
    @Transactional
    public void mergeCategories(Long expenseTypeId1, Long expenseTypeId2, String newTypeName, double newBudgetAmount) {
        
        ExpenseTypeEntityDTO expenseTypeDTO = new ExpenseTypeEntityDTO(null, newTypeName, newBudgetAmount);
        ExpenseTypeEntity newExpenseTypeEntity =create(expenseTypeDTO);
        List<Expense> expenses = expenseRepository.findByExpenseDateGreaterThanEqualAndExpenseType_IdIn(LocalDate.now(),List.of(expenseTypeId1, expenseTypeId2));
        List<BaseCard> baseCards = baseCardRepository.findByExpenseTypeToPayFor_IdIn(List.of(expenseTypeId1, expenseTypeId2));

        delete(expenseTypeId2); 
        delete(expenseTypeId1);
    
        for (Expense expense : expenses) {
            Long expenseId = expense.getId();
            ExpenseDTO expenseDTO = new ExpenseDTO(expenseId, null, null, null, null, null, newExpenseTypeEntity.getId());

            expenseService.update(expenseDTO);
        }

        for (BaseCard baseCard : baseCards){
            baseCardService.addExpenseTypeToPayFor(baseCard, List.of(newExpenseTypeEntity.getId()));
            baseCardRepository.save(baseCard);
        }


    }

    @Override
    @Transactional
    // we also have to know which of these expense types we are assigning to these expenses and basecards.
    public void splitCategory(Long expenseTypeIdToSplit, String newTypeName1, String newTypeName2, double newBudgetAmount1, double newBudgetAmount2) {
        ExpenseTypeEntity expenseTypeEntityToSplit = expenseTypeRepository.findById(expenseTypeIdToSplit).orElseThrow(()-> new EntityNotFoundException("Expense type with id " + expenseTypeIdToSplit + " not found"));

        ExpenseTypeEntityDTO expenseTypeDTO1 = new ExpenseTypeEntityDTO(null, newTypeName1, newBudgetAmount1);
        ExpenseTypeEntityDTO expenseTypeDTO2 = new ExpenseTypeEntityDTO(null, newTypeName2, newBudgetAmount2);

        ExpenseTypeEntity expenseTypeEntity1 = create(expenseTypeDTO1);
        ExpenseTypeEntity expenseTypeEntity2 = create(expenseTypeDTO2);


        
        delete(expenseTypeIdToSplit);
    }

    @Transactional
    public void assignExpenseTypeToExpenses(List<Expense> expenses, ExpenseTypeEntity expenseType){ 
        for (Expense expense : expenses) {
            Long expenseId = expense.getId();
            ExpenseDTO expenseDTO = new ExpenseDTO(expenseId, null, null, null, null, null, expenseType.getId());
            expenseService.update(expenseDTO);
        }
            
    } 
    @Transactional
    public void assignExpenseTypeToBaseCards(List<BaseCard> baseCards, ExpenseTypeEntity expenseType){
        for (BaseCard baseCard : baseCards){
            baseCardService.addExpenseTypeToPayFor(baseCard, List.of(expenseType.getId()));
            baseCardRepository.save(baseCard);
        }
    }

    


}
