package io.github.victorjimenez.expense_tracker_api.models;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class CreditCardTest {
    private CreditCard myCreditCard;
    private CreditCard myOtherCreditCard;
    private CreditCardFeesType creditCardFeesType;
    
    private 
    @BeforeEach
    void setUp() {
        creditCardFeesType = CreditCardFeesType.LATE_FEE;
        myCreditCard = new CreditCard("1234", "Visa", -100., 50., 25., 0.05, 10.); 
        myOtherCreditCard = new CreditCard("5678", "Mastercard", -200., 100., 25., 0.05, 10.);
        
    }

    @Test
    void testGettersAndSettersCreditCard() {
        assertEquals(-100., myCreditCard.getBalance(), "balance getter failed");
        myCreditCard.setBalance(-200.);
        assertEquals(-200., myCreditCard.getBalance(), "balance setter failed");
        assertEquals(50., myCreditCard.getCreditLimit(), "creditLimit getter failed");
        myCreditCard.setCreditLimit(100.);
        assertEquals(100., myCreditCard.getCreditLimit(), "creditLimit setter failed");
        assertEquals(25., myCreditCard.getPersonalLimit(), "personalLimit getter failed");
        myCreditCard.setPersonaLimit(50.);
        assertEquals(50., myCreditCard.getPersonalLimit(), "personalLimit setter failed");
        assertEquals(0.05, myCreditCard.getInterestRate(), "interestRate getter failed");
        myCreditCard.setInterestRate(0.1);
        assertEquals(0.1, myCreditCard.getInterestRate(), "interestRate setter failed");
        assertEquals(10., myCreditCard.getMinimumPayment(), "minimumPayment getter failed");
        myCreditCard.setMinimumPayment(20.);
        assertEquals(20., myCreditCard.getMinimumPayment(), "minimumPayment setter failed");
    }

    @Test
    void addAndRemove() {

        myCreditCard.addCreditFee(creditCardFeesType, 10.);
        assertEquals(10., myCreditCard.getCreditFee(creditCardFeesType), "addCreditFee failed");
        myCreditCard.removeCreditFee(creditCardFeesType);
        assertThrows(NullPointerException.class, () -> myCreditCard.getCreditFee(creditCardFeesType), "removeCreditFee failed");
    }

    @Test
    void testToStringCreditCard() {
        String actual = myCreditCard.toString();
        assertEquals("DebitCard[cardNumber='1234', cardProvider='Visa', balance=-100.0, creditLimit=50.0, personalLimit=25.0, " +
                "interestRate=0.05, minimumPayment=10.0, expenseTypeToPayFor=[], eligibleExpenses=[], " +
                "paymentHistories=[], baseFees={}, creditFees={}]",
                actual, "toString failed");
    }

    @Test
    void testEqualsCreditCard() {
        assertEquals(myCreditCard, myCreditCard, "equals failed");
        assertNotEquals(myCreditCard, myOtherCreditCard, "equals failed");
    }
    @Test 
    void testHashCodeCreditCard() {
        assertEquals(myCreditCard.hashCode(), myCreditCard.hashCode(), "hashCode failed");
        assertNotEquals(myCreditCard.hashCode(), myOtherCreditCard.hashCode(), "hashCode failed");
    }
}
