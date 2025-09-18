package io.github.victorjimenez.expense_tracker_api.listeners;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.events.EndOfBillingCycleEvent;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.repository.CreditCardRepository;
import io.github.victorjimenez.expense_tracker_api.repository.ExpenseTypeRepository;
import io.github.victorjimenez.expense_tracker_api.services.ExpenseService;
import jakarta.persistence.EntityNotFoundException;

@Component
public class NotificationListeners {
    


    // what do we want from this well we want it so that when listener is activated we say to the user hey uh
    // so you went over your personal limit. Do you want to continue? Thats the most basic idea of what I want?
    // how to accomplish this. In addition to asking them that we should how much they have exceed the personal limit 
    // that way they can make a better decision i guess.
    // ok so for notification to send messages/data to frontend SSE is the way to go. 
    @EventListener
    public String
}
