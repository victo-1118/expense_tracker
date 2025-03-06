package io.github.victorjimenez.expense_tracker_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import java.util.List;
import java.util.Set;


public interface ExpenseTypeRepository extends JpaRepository<ExpenseTypeEntity, Long> {
    ExpenseTypeEntity findByExpenseType(String expenseType);
    Set<ExpenseTypeEntity> findByExpenseTypeIn(Set<String> expenseTypes);
    Set<ExpenseTypeEntity> findByBaseCards_cardNumber(String cardNumber);
    
}