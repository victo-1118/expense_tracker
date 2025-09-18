package io.github.victorjimenez.expense_tracker_api.listeners;

import java.util.concurrent.CompletableFuture;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import io.github.victorjimenez.expense_tracker_api.events.PersonalLimitWarningEvent;

// So this is where i need to start the completable future for the personal limit warning
@Component
public class PersonalLimitWarningEventListener {

    @EventListener
    public void onPersonalLimitWarningEvent(PersonalLimitWarningEvent event) {
        CompletableFuture<Boolean> SSE = new CompletableFuture<>();
        
    }
}
