package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseTypeEntityDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import java.util.List;

import org.springframework.data.domain.Sort;
public interface ExpenseTypeEntityService {
    //Create Operation
    ExpenseTypeEntity create(ExpenseTypeEntityDTO expenseTypeEntityDTO);
    // Read Operations
    List<ExpenseTypeEntity> findAll(Sort sort);
    ExpenseTypeEntity findByExpenseType(String expenseType);
    ExpenseTypeEntity findById(Long id);
    // Write Operations
    ExpenseTypeEntity update(ExpenseTypeEntityDTO expenseTypeEntityDTO);
    void delete(Long id);
    void addBaseCards(BaseCard baseCard, List<Long> expenseTypeEntityIds);
    void removeBaseCards(List<BaseCard> baseCards, Long expenseTypeEntityId);
    void removeBaseCard(BaseCard baseCard, List<Long> expenseTypeEntityIds);

    




    // Business Logic
    // how will i know if i go over the budget?
    // should i calculate this everytime an expense is paid for?
    // is there a way that the program can just know?
    double getRemainingBudget(Long expenseTypeId, double totalExpenseAmount);

    // Category Management
    // so im guessing the logic here would be to first get all the expenses and cards with both categories first.
    // then create a new ExpenseTypeEntity with the new category name and budget amount
    // then delete the old categories. After we assign all the expenses and cards to the new category
    void mergeCategories(Long expenseTypeId1, Long expenseTypeId2, String newTypeName, double newBudgetAmount);
    // splitting categorys is harder. we need to create two new categorys that have different budget amounts.
    // find expenses and cards with the old category.
    // how do we assign them to the new categorys?
    // sure we could add argumments for which ones should be assigned to the new categorys but surely there is a better way
    // maybe have a different function?
    void splitCategory(Long expenseTypeIdToSplit, String newTypeName1, String newTypeName2, double newBudgetAmount1, double newBudgetAmount2);


}
