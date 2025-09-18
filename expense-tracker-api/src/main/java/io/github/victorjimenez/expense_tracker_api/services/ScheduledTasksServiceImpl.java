package io.github.victorjimenez.expense_tracker_api.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import io.github.victorjimenez.expense_tracker_api.events.EndOfBillingCycleEvent;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.repository.CreditCardRepository;

public class ScheduledTasksServiceImpl implements ScheduledTasksService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public ScheduledTasksServiceImpl(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    // definetly could be optimized
    // could subtract less days and get less credit cards
    // fix this only if you find users have a lot of cards.
    // currently i have it so that when the user first enters their credit card info it doesnt actually charge the last
    //  paymentDueDate as the program has no knowledge of that. Could calculate that but that would have to only run once
    // i think im going to make the user create that. 
    
    // yes i think this makes sense for now. This would also just be a one time thing for the user
    // most importantly though is where would i put this checkForPaymentDueDate?
    // I think this should be in notification service.
    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void checkForCreditCardEndOfBillingCycle(){ //this should publish an event instead of calling chargeInterest and chargeMinimumPayment
        List<CreditCard> creditCards = creditCardRepository.findByStartOfBillingCycleAfter(LocalDate.now().minus(30, ChronoUnit.DAYS));
        for (CreditCard creditCard : creditCards){
            if (creditCard.getStartOfBillingCycle().plus(creditCard.getBillingCyclePeriod()) == LocalDate.now()){
                String cardNumber = creditCard.getCardNumber();
                creditCard.setStartOfBillingCycle(creditCard.getStartOfBillingCycle().plus(creditCard.getBillingCyclePeriod()));
                //should publish an event
                applicationEventPublisher.publishEvent(new EndOfBillingCycleEvent(cardNumber));
            }     
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    //so this is kind of an issue. If user is importing cards then we dont know what month they first got it in
    // an therefore we dont know when they should be charged.
    //ok so we make it so that if user tries to add annual credit fee we also make them input their next due date
    // so that we can make an expense and we can check here
    // so here we are actually loo0king for an expense not an actual credit card
    // the expense can show us when to expect annual credit fee. ExpenseType will be annual credit fee
    // how are we supposed to know what credit card the fee is for? This is becoming way more complicated
    // maybe this indicates we might need to refactor something. How do i fix this? think!
    // could we create some kind of relationship? Sure but that might be too much of a hassle and not all credit cards
    // have an annual credit fee.
    // What else? This might be weird but for expense type we do AnnualCreditFee + last 4 digits of card number
    public Expense chargeAnnualCreditFee(){
        
        
    }
    
}
