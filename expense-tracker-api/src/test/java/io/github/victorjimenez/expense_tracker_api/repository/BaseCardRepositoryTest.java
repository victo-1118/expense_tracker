package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.CreditCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import java.util.Set;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.repository.PaymentHistoryRepository;
import java.util.HashSet;
import java.util.List;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
public class BaseCardRepositoryTest {
    @Autowired
    private BaseCardRepository baseCardRepository;
    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    private BaseCardFeesType baseCardFeesType1;
    private BaseCardFeesType baseCardFeesType2;
    private DebitCardFeesType debitCardFeesType1;
    private DebitCardFeesType debitCardFeesType2;
    private CreditCardFeesType creditCardFeesType1;
    private CreditCardFeesType creditCardFeesType2;
    private DebitCard debitCard1;
    private DebitCard debitCard2;
    private DebitCard debitCard3;
    private CreditCard creditCard1;
    private CreditCard creditCard2;
    private CreditCard creditCard3;
    private ExpenseTypeEntity expenseTypeEntity1;
    private ExpenseTypeEntity expenseTypeEntity2;
    private ExpenseTypeEntity expenseTypeEntity3;
    private ExpenseTypeEntity expenseTypeEntity4;
    private Expense expense1;
    private Expense expense2;
    private Expense expense3;
    private Expense expense4;
    private LocalDate date1;
    private LocalDate date2;
    private LocalDate date3;



    @BeforeEach
    void setUp() {
        baseCardFeesType1 = BaseCardFeesType.CARD_REPLACEMENT_FEE;
        baseCardFeesType2 = BaseCardFeesType.FOREIGN_TRANSACTION_FEE;
        debitCardFeesType1 = DebitCardFeesType.OVERDRAFT_FEE;
        debitCardFeesType2 = DebitCardFeesType.ATM_FEE;
        creditCardFeesType1 = CreditCardFeesType.LATE_FEE;
        creditCardFeesType2 = CreditCardFeesType.OVERLIMIT_FEE;
        expenseTypeEntity1 = new ExpenseTypeEntity("Groceries", 500.);
        expenseTypeEntity2 = new ExpenseTypeEntity("Clothing", 100.);
        expenseTypeEntity3 = new ExpenseTypeEntity("Entertainment", 600.);
        expenseTypeEntity4 = new ExpenseTypeEntity("Travel", 500.);
        expenseTypeRepository.saveAll(Arrays.asList(expenseTypeEntity1, expenseTypeEntity2, expenseTypeEntity3, expenseTypeEntity4));        
        debitCard1 = new DebitCard("1234", "Visa", 100., 50.);
        baseCardRepository.save(debitCard1);
        debitCard1.addExpenseTypeToPayFor(expenseTypeEntity1);
        debitCard1.addExpenseTypeToPayFor(expenseTypeEntity2);
        debitCard1.addFee(baseCardFeesType1, 20.);
        debitCard1.addDebitFee(debitCardFeesType1, 30.);
        baseCardRepository.save(debitCard1);
        debitCard2 = new DebitCard("5678", "Mastercard", 200., 100.);
        baseCardRepository.save(debitCard2);
        debitCard2.addExpenseTypeToPayFor(expenseTypeEntity3);
        debitCard2.addExpenseTypeToPayFor(expenseTypeEntity2);
        debitCard2.addFee(baseCardFeesType2, 40.);
        debitCard2.addDebitFee(debitCardFeesType2, 50.);
        debitCard2.addDebitFee(debitCardFeesType1, 60.);
        baseCardRepository.save(debitCard2);
        
        debitCard3 = new DebitCard("9101", "Visa", 300., 150.);
        baseCardRepository.save(debitCard3);
        debitCard3.addExpenseTypeToPayFor(expenseTypeEntity4);
        debitCard3.addExpenseTypeToPayFor(expenseTypeEntity2);
        debitCard3.addFee(baseCardFeesType1, 70.);
        debitCard3.addDebitFee(debitCardFeesType2, 80.);
        baseCardRepository.save(debitCard3);
        creditCard1 = new CreditCard("1235", "Visa", -100., 50., 25., 0.05, 10., 0.02, Period.ofDays(25), Period.ofDays(30), LocalDate.of(2023, 6, 1));
        baseCardRepository.save(creditCard1);
        creditCard1.addExpenseTypeToPayFor(expenseTypeEntity1);
        creditCard1.addCreditFee(creditCardFeesType1, 20.);
        creditCard1.addCreditFee(creditCardFeesType2, 50.);
        baseCardRepository.save(creditCard1);
        creditCard2 = new CreditCard("5679", "Mastercard", -200., 100., 25., 0.05, 10., 0.03, Period.ofDays(24), Period.ofDays(30), LocalDate.of(2023, 6, 1));
        baseCardRepository.save(creditCard2);
        creditCard2.addExpenseTypeToPayFor(expenseTypeEntity2);
        creditCard2.addExpenseTypeToPayFor(expenseTypeEntity4);
        creditCard2.addCreditFee(creditCardFeesType1, 30.);
        creditCard2.addFee(baseCardFeesType1, 40.);
        baseCardRepository.save(creditCard2);
        creditCard3 = new CreditCard("9102", "Visa", -200., 150., 25., 0.05, 10., 0.02, Period.ofDays(25), Period.ofDays(30), LocalDate.of(2023, 6, 1));
        baseCardRepository.save(creditCard3);
        creditCard3.addExpenseTypeToPayFor(expenseTypeEntity4);
        creditCard3.addCreditFee(creditCardFeesType2, 60.);
        creditCard3.addFee(baseCardFeesType2, 70.);
        baseCardRepository.save(creditCard3);
        date1 = LocalDate.of(2025, 2, 1);
        date2 = LocalDate.of(2025, 2,  13);
        date3 = LocalDate.of(2025, 2, 26);
        expense1 = new Expense(expenseTypeEntity1, 25., date1, "Eggs", false, null);
        expense2 = new Expense(expenseTypeEntity2, 40., date2, "Pants", false, null);
        expense3 = new Expense(expenseTypeEntity3, 30., date3, "Netflix", false, null);
        expense4 = new Expense(expenseTypeEntity4, 30., date2, "Las Vegas", false, null);
        expenseRepository.saveAll(Arrays.asList(expense1, expense2, expense3, expense4));
        
        debitCard1.addExpense(expense1);
        debitCard1.addExpense(expense2);
        debitCard2.addExpense(expense3);
        debitCard2.addExpense(expense2);
        creditCard1.addExpense(expense1);
        creditCard1.addExpense(expense2);
        creditCard2.addExpense(expense3);
        creditCard2.addExpense(expense1);
        


        baseCardRepository.saveAll(Arrays.asList(debitCard1, debitCard2, debitCard3, creditCard1, creditCard2, creditCard3));

    }
    @Test
    void basicSearches(){
        List<BaseCard> cardsVisa = baseCardRepository.findByCardProvider("Visa");
        assertEquals(4, cardsVisa.size(), "base card repository getByCardProvider() does not work right ");
        List<BaseCard> cardsBalanceNegative200 = baseCardRepository.findByBalance(-200.);
        assertEquals(2, cardsBalanceNegative200.size(), "Base card repository getByBalance() does not work");
        List<BaseCard> cardsBalance500 = baseCardRepository.findByBalance(500.);
        assertEquals(0, cardsBalance500.size(), "Base card repository getByBalance() doesd not work");
        List<BaseCard> cardsBalanceBetweenNegative100To300 = baseCardRepository.findByBalanceBetween(-100., 300.);
        assertEquals(4,cardsBalanceBetweenNegative100To300.size(), "Base Card repository getByBalanceBetween() does not work right" ); 
    }
    @Test
    void oneToManySearches(){
        List<BaseCard> cardsWithBaseFee40 = baseCardRepository.findByBaseFeeAmount(40.);
        assertEquals(2, cardsWithBaseFee40.size(), "Base card repository getByBaseFeeAmount() does not work right");
        List<BaseCard> cardsWithBaseFeeBetween20And50 = baseCardRepository.findByBaseFeeAmountBetween(20., 50.);
        assertEquals(3, cardsWithBaseFeeBetween20And50.size(), "Base card repository getByBaseFeeAmountBetween() does not work right");
        
    }

    @Test
    void manyToManySearches(){
        List<BaseCard> cardsWithExpenseType1 = baseCardRepository.findByExpenseTypeToPayFor_IdIn(List.of(expenseTypeEntity1.getId()));
        assertEquals(2, cardsWithExpenseType1.size(), "Base card repository getByExpenseTypeToPayFor_IdIn() does not work right");
        List<BaseCard> cardsWithExpenseType2 = baseCardRepository.findByExpenseTypeToPayFor_IdIn(List.of(expenseTypeEntity2.getId(), expenseTypeEntity3.getId()));
        assertEquals(4, cardsWithExpenseType2.size(), "Base card repository getByExpenseTypeToPayFor_IdIn() does not work right");
        List<BaseCard> cardsWithAtLeastExpenseType2AndExpenseType4 = baseCardRepository.findByAllExpenseTypes(List.of(expenseTypeEntity2.getId(), expenseTypeEntity4.getId()), 2);
        assertEquals(2, cardsWithAtLeastExpenseType2AndExpenseType4.size(), "Base card repository getByAllExpenseTypes() does not work right");
        List<BaseCard> cardsWithAtLeastExpenseType4or3 = baseCardRepository.findByExpenseTypeToPayFor_IdIn(List.of(expenseTypeEntity4.getId(), expenseTypeEntity3.getId()));
        assertEquals(4, cardsWithAtLeastExpenseType4or3.size(), "Base card repository getByExpenseTypeToPayFor_IdIn() does not work right");
        
        List<BaseCard> cardsWithExpense1 = baseCardRepository.findByEligibleExpenses_IdIn(List.of(expense1.getId()));
        assertEquals(3, cardsWithExpense1.size(), "Base card repository getByEligibleExpenses_IdIn() does not work right");
        List<BaseCard> cardsWithExpense2or3 = baseCardRepository.findByEligibleExpenses_IdIn(List.of(expense2.getId(), expense3.getId()));
        assertEquals(4, cardsWithExpense2or3.size(), "Base card repository getByEligibleExpenses_IdIn() does not work right");
        List<BaseCard> cardsWithAtLeastExpense2AndExpense3 = baseCardRepository.findByAllExpenses(List.of(expense2.getId(), expense3.getId()), 2);
        assertEquals(1, cardsWithAtLeastExpense2AndExpense3.size(), "Base card repository getByAllExpenses() does not work right");
    }
    
}