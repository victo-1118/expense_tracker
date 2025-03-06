package io.github.victorjimenez.expense_tracker_api.models;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
public class ExpenseTest {

    private Expense expense1;
    private Expense expense2;
    private ExpenseTypeEntity expenseTypeEntity;
    private ExpenseTypeEntity expenseTypeEntity2;
    private LocalDate date1;
    private LocalDate date2;
    @BeforeEach
    void setUp() {
        expenseTypeEntity = new ExpenseTypeEntity("Grocery", 100., 50.);
        expenseTypeEntity2 = new ExpenseTypeEntity("Clothing", 100., 50.);
        
        date1 = LocalDate.of(2025,2, 20);
        date2 = LocalDate.of(2025,2, 21);
        expense1 = new Expense(expenseTypeEntity, 100., date1, "Eggs", false, null);
        expense2 = new Expense(expenseTypeEntity2, 200., date2, "Shirt", false, null);
    }
    @Test
    void testGettersAndSettersExpense() {
        ExpenseTypeEntity actual = expense1.getExpenseType();
        assertEquals(expenseTypeEntity, actual, "expenseType getter failed");
        assertEquals(100.0f, expense1.getExpenseAmount(), "expenseAmount getter failed");
        assertEquals(date1, expense1.getExpenseDate(), "expenseDate getter failed");
        assertEquals("Eggs", expense1.getExpenseName(), "expenseName getter failed");

        expense1.setExpenseType(expenseTypeEntity2);
        actual = expense1.getExpenseType();
        assertEquals(expenseTypeEntity2, actual, "expenseType setter failed");

        expense1.setExpenseAmount(200.0f);
        assertEquals(200.0f, expense1.getExpenseAmount(), "expenseAmount setter failed");

        expense1.setExpenseDate(date2);
        assertEquals(date2, expense1.getExpenseDate(), "expenseDate setter failed");

        expense1.setExpenseName("Shirt");
        assertEquals("Shirt", expense1.getExpenseName(), "expenseName setter failed");

    }
    @Test
    void testToStringExpense() {
        String actual = expense1.toString();
        assertEquals("Expense[id=" + null + ", expenseType='null', expenseAmount=100.0, expenseDate='2025-02-20', expenseName='Eggs', reoccurring=false, frequency=null]", actual, "toString failed");
    }
    //Expense[id=null, expenseType='ExpenseTypeEntity[id=null, expenseType='Grocery', budgetAmount=100.0, actualSpent=50.0]', expenseAmount=100.0, expenseDate='2025-02-20', expenseName='Eggs', reoccurring=false', frequency=null]&gt;" type="org.opentest4j.AssertionFailedError">org.opentest4j.AssertionFailedError: toString failed ==&gt; expected: &lt;Expense[id=null, expenseType='ExpenseTypeEntity[id=null, expenseType='Grocery', reoccurring=true, budgetAmount=100.0, actualSpent=50.0]', expenseAmount=100.0, expenseDate='2025-02-20', expenseName='Eggs']&gt; but was: &lt;Expense[id=null, expenseType='ExpenseTypeEntity[id=null, expenseType='Grocery', budgetAmount=100.0, actualSpent=50.0]', expenseAmount=100.0, expenseDate='2025-02-20', expenseName='Eggs', reoccurring=false', frequency=null]
    
    @Test
    void testEqualsExpense() {
        assertEquals(expense1, expense1, "Same object should be equal");
        assertNotEquals(expense1, expense2, "Different objects should not be equal");
    }

    @Test
    void testHashCodeExpense() {
        int hashCode1 = expense1.hashCode();
        int hashCode2 = expense1.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent for same object");
        int hashCode3 = expense2.hashCode();
        assertNotEquals(hashCode1, hashCode3, "Hash code should be different for different objects");
    }

}
