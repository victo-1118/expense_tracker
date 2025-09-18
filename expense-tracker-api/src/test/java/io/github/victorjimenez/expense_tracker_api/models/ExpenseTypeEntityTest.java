package io.github.victorjimenez.expense_tracker_api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;

@DataJpaTest
@ActiveProfiles("test")
public class ExpenseTypeEntityTest {
    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;
    private  ExpenseTypeEntity expenseTypeEntity;
    private  ExpenseTypeEntity expenseTypeEntity3;
    @BeforeEach
    void setUp() {
        expenseTypeEntity = new ExpenseTypeEntity("Groceries", 500.);
        expenseTypeEntity3 = new ExpenseTypeEntity("Entertainment", 500.);
        expenseTypeRepository.save(expenseTypeEntity);

    }
    @Test
    public void testGettersAndSettersExpenseTypeEntity() {

        assertEquals("Groceries", expenseTypeEntity.getExpenseType(), String.format("expenseType getter failed"));
        assertNotEquals(null, expenseTypeEntity.getId(), "id generator failed");
        expenseTypeEntity.setExpenseType("Clothing");
        assertEquals("Clothing", expenseTypeEntity.getExpenseType(), "expenseType setter failed");
    }
    @Test
    public void testToStringExpenseTypeEntity() {
        String actual = expenseTypeEntity.toString();
        String id = actual.substring(21, 23);
        assertEquals("ExpenseTypeEntity[id=" + id + ", expenseType='Groceries', budgetAmount=500.0, baseCards=[]]", expenseTypeEntity.toString(), "toString failed");
    }



    @Test
    public void testHashCode() {
        // Test 1: Same object should have consistent hash code
        int hashCode1 = expenseTypeEntity.hashCode();
        int hashCode2 = expenseTypeEntity.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent for same object");

        // Test 2: Different objects should have different hash code
        int hashCode3 = expenseTypeEntity3.hashCode();
        assertNotEquals(hashCode1, hashCode3, "Hash code should be different for different objects");
    
    }

    @Test
    void testUniqueConstraintViolation() {

        // When & Then
        ExpenseTypeEntity expenseType2 = new ExpenseTypeEntity("Groceries", 500.);
        assertThrows(DataIntegrityViolationException.class, () -> {
            expenseTypeRepository.save(expenseType2);
            expenseTypeRepository.flush(); // Force the persistence
        }, "Should throw DataIntegrityViolationException for duplicate expense type");
    }
}

