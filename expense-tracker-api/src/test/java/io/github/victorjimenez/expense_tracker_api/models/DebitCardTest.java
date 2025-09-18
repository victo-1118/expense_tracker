package io.github.victorjimenez.expense_tracker_api.models;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import io.github.victorjimenez.expense_tracker_api.repository.DebitCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.repository.PaymentHistoryRepository;
import io.github.victorjimenez.expense_tracker_api.snapshots.DebitCardSnapshot;
import io.github.victorjimenez.expense_tracker_api.snapshots.ExpenseSnapshot;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
/**
 * Unit tests for the DebitCard class and for the BaseCard tests
 * Test unique constraints and size constraints as well as all 
 * the methods you would expect to see in a normal class
 */
@DataJpaTest
@ActiveProfiles("test")
public class DebitCardTest {
    @Autowired
    private DebitCardRepository debitCardRepository;
    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;
    private DebitCard myDebitCard;
    private DebitCardSnapshot myDebitCardSnapshot;
    private DebitCard myOtherDebitCard;
    private Set<ExpenseTypeEntity> expenseTypeEntities;
    private ExpenseTypeEntity expenseTypeEntity;
    private ExpenseTypeEntity expenseTypeEntity2;
    private ExpenseTypeEntity expenseTypeEntity3;
    private BaseCardFeesType baseCardFeesType;
    private DebitCardFeesType debitCardFeesType;
    private PaymentHistory paymentHistory;
    private Expense expense1;
    private ExpenseSnapshot expense1Snapshot;
    @BeforeEach
    void setUp(){
        entityManager.clear();
        baseCardFeesType = BaseCardFeesType.CARD_REPLACEMENT_FEE;

        debitCardFeesType = DebitCardFeesType.OVERDRAFT_FEE;

        
        expenseTypeEntity = expenseTypeRepository.saveAndFlush(new ExpenseTypeEntity("Groceries", 500.));
        expenseTypeEntity2 = expenseTypeRepository.saveAndFlush(new ExpenseTypeEntity("Clothing", 100.));
        expenseTypeEntity3 = expenseTypeRepository.saveAndFlush(new ExpenseTypeEntity("Entertainment", 500.));
        
        expenseTypeEntities = new HashSet<>();
        expenseTypeEntities.add(expenseTypeEntity);
        expenseTypeEntities.add(expenseTypeEntity2);

        myDebitCard = new DebitCard("1234", "Visa", 100., 50.);
        myDebitCard.addExpenseTypeToPayFor(expenseTypeEntity);
        myDebitCard.addExpenseTypeToPayFor(expenseTypeEntity2);
        expense1 = new Expense(expenseTypeEntity, 50., LocalDate.now(), "Bread", false, null);
        myOtherDebitCard = new DebitCard("5678", "Mastercard", 200., 100.);
        myOtherDebitCard.addExpenseTypeToPayFor(expenseTypeEntity3);

        myDebitCard = debitCardRepository.saveAndFlush(myDebitCard);
        myOtherDebitCard = debitCardRepository.saveAndFlush(myOtherDebitCard);
        myDebitCardSnapshot = new DebitCardSnapshot(myDebitCard);
        expense1Snapshot = new ExpenseSnapshot(expense1);

        paymentHistory = new PaymentHistory(expense1, 10, LocalDate.now(), myDebitCard, myDebitCardSnapshot, expense1Snapshot);
        paymentHistory = paymentHistoryRepository.saveAndFlush(paymentHistory);


        

    }
    @Test
    void testGettersAndSettersDebitCard() {
        assertEquals("1234", myDebitCard.getCardNumber(), "cardNumber getter failed");
        assertEquals("Visa", myDebitCard.getCardProvider(), "cardProvider getter failed");
        assertEquals(100., myDebitCard.getBalance(), "balance getter failed");
        assertEquals(50., myDebitCard.getBudget(), "budget getter failed");
        assertEquals(expenseTypeEntities, myDebitCard.getExpenseTypeToPayFor(), String.format("expenseTypes getter failed expected %s but got %s", expenseTypeEntities, myDebitCard.getExpenseTypeToPayFor()));   
    }

    @Test
    void testAddAndRemove() {
        myDebitCard.addDebitFee(debitCardFeesType, 5.);
        assertEquals(5., myDebitCard.getDebitFee(debitCardFeesType), "addDebitFee failed");
        
        myDebitCard.removeDebitFee(debitCardFeesType);
        assertThrows(NullPointerException.class, () -> {
            myDebitCard.getDebitFee(debitCardFeesType);
        }, "removeDebitFee failed");
        myDebitCard.addFee(baseCardFeesType, 10.);
        assertEquals(10., myDebitCard.getFee(baseCardFeesType), "addFee failed");
        myDebitCard.removeFee(baseCardFeesType);
        assertThrows(NullPointerException.class, () -> {
            myDebitCard.getFee(baseCardFeesType);
        }, "removeFee failed");
        myDebitCard.addPaymentHistory(paymentHistory);
        assertEquals(true, myDebitCard.getPaymentHistories().contains(paymentHistory), "addPaymentHistory failed");
        myDebitCard.removePaymentHistory(paymentHistory);
        assertTrue(myDebitCard.getPaymentHistories().isEmpty(), "removePaymentHistory failed");
    }
    //This test is kind of iffy because it's testing the toString method but when adding ids to the string it doesn't
    //add the ids in the same order as the expected string so it will fail if not added correctly
    // in this case it has been working so the order is the same but for the future i should keep this in mind
    // and maybe change the way the ids are added to the string
    @Test
    void testToStringDebitCard() {
        StringBuilder expenseTypeToPayForSet = new StringBuilder();
        for (ExpenseTypeEntity expenseType : myDebitCard.getExpenseTypeToPayFor()) {
            expenseTypeToPayForSet.append(expenseType.getId()).append(", ");
        }
        expenseTypeToPayForSet.delete(expenseTypeToPayForSet.length() - 2, expenseTypeToPayForSet.length()); //removes the last comma (2)
        String expenseString = expenseTypeToPayForSet.toString();
        String expected = String.format("DebitCard[cardNumber='1234', cardProvider='Visa', balance=100.0, budget=50.0, " +
                "expenseTypeToPayFor=[%s], eligibleExpenses=[], paymentHistories=[], baseFees={}, debitFees={}]", 
                expenseString);
        String actual = myDebitCard.toString();
        assertEquals(expected, actual, 
                String.format("toString failed expected %s but got %s", expected, actual));
    }
    @Test
    void testEqualsDebitCard() {
        assertNotEquals(myDebitCard, myOtherDebitCard, "Equals failed. should have returned false due to same id"); 
    }
    @Test
    void testHashCodeDebitCard() {
        int hashCode1 = myDebitCard.hashCode();
        int hashCode2 = myDebitCard.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent for same object");
        int hashCode3 = myOtherDebitCard.hashCode();
        assertNotEquals(hashCode1, hashCode3, "Hash code should be different for different objects");

    }
    @Test
    void testUniqueConstraintViolation() {
        // First verify the original card exists
        assertTrue(debitCardRepository.existsById("1234"), 
            "Original card should exist before testing duplicate");

        // Create a duplicate card with different data to ensure it's not just updating
        DebitCard duplicateCard = new DebitCard("1234", "MasterCard", 200., 75.);
        duplicateCard.addExpenseTypeToPayFor(expenseTypeEntity3);

        // Use EntityManager directly to force an insert
        assertThrows(PersistenceException.class, () -> {
            entityManager.getEntityManager().persist(duplicateCard);
            entityManager.getEntityManager().flush();
        }, "Should not be able to insert a duplicate card number");

        // Verify the original card wasn't modified
        DebitCard originalCard = debitCardRepository.findById("1234")
            .orElseThrow(() -> new AssertionError("Original card should still exist"));
        
        // Verify none of the duplicate card's data made it in
        assertEquals("Visa", originalCard.getCardProvider(), 
            "Original card provider should be unchanged");
        assertEquals(100., originalCard.getBalance(), 
            "Original card balance should be unchanged");
        assertEquals(50., originalCard.getBudget(), 
            "Original card budget should be unchanged");
    }
    
    @Test
    void testSizeConstraintViolation() {
        DebitCard card = new DebitCard("12345" , "Visa", 100., 50.);
        assertThrows(DataIntegrityViolationException.class, () -> {
            debitCardRepository.saveAndFlush(card);
        }, "Should throw DataIntegrityViolationException for duplicate card number");
    }
}
