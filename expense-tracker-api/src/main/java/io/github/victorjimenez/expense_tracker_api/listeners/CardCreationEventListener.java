package io.github.victorjimenez.expense_tracker_api.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
//This will listen for events related to card creation. Especiallywith different types of cards are created
//what might this look like

public class CardCreationEventListener {
    // The following function should make it so that the user hast to create an expense if 
    //they create a credit card with an annual fee so that the the program knows when they have to pay for it
    // and make it reoccuring too.
    // actually lets just make the frontend enforce this i think?????
    // If frontend enforces then we dont have to worry about creating the expense i think?
    // Well we still need a way to check if the credit card has the annual fee so that it can then create the expense
    // lets put a pin on this but now we know what to do
    // to fix issues of not knowing when the user has to pay for an annual fee. we just make the user do it themself
    // How does user know what corresponding creditcard?
    // Fortunately my Expense class has a field for expense description we will put the card number there at the end
    // and we will probably have to create a query for that (for looing for that last 4 chars of that stringe)
    @EventListener
    
}
