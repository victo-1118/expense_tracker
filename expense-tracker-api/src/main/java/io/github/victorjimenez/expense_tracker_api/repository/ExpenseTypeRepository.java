package io.github.victorjimenez.expense_tracker_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import java.util.List;



public interface ExpenseTypeRepository extends JpaRepository<ExpenseTypeEntity, Long> {
    ExpenseTypeEntity findByExpenseType(String expenseType);
    List<ExpenseTypeEntity> findByIdIn(List<Long> ids);
    List<ExpenseTypeEntity> findByExpenseTypeIn(List<String> expenseTypes);
    List<ExpenseTypeEntity> findByBaseCards_cardNumber(String cardNumber);

    
}