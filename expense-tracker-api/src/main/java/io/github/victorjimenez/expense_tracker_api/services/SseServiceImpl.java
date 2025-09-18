package io.github.victorjimenez.expense_tracker_api.services;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//This service will be used to communicate with frontend. This will make it ppssible to know when to send
// notifications and etc. 
@Service
public class SseServiceImpl {
    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final AtomicLong emitterIdCounter = new AtomicLong(0);

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingConfirmations = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(){
        Long emitterId = emitterIdCounter.incrementAndGet();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> emitters.remove(emitterId));
        emitter.onTimeout(() -> emitters.remove(emitterId));
        emitter.onError(Throwable -> emitters.remove(emitterId) );

        emitters.put(emitterId, emitter);

        try{
            emitter.send(SseEmitter.event().name("Connect").data("Connected Sse Stream"));

        }
        catch(IOException e){
            emitters.remove(emitterId);
        }

        return emitter;


    }

    public void sendToAll(String eventName, String message){
        emitters.entrySet().removeIf(Entry -> {
            try{
                Entry.getValue().send(SseEmitter.event().name(eventName).data(message));
                return false;
            }

            catch(IOException e){
                return true;
            }
        });
    }
    // I dont think I need the userId yet
    public CompletableFuture<Boolean> requestUserConfirmation(String message){
        CompletableFuture<Boolean> confirmation = new CompletableFuture<>();
        // the key which is 1 in this case will be replaced with userId in the future
        pendingConfirmations.put("1", confirmation);

        sendToAll("Confirmation request", String.format("{\"userId\":\"%s\", \"message\":\"%s\"}", 1, message)); 
        CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
            if (!confirmation.isDone()){
                confirmation.complete(false);
                pendingConfirmations.remove("1");
            }
        });
        return confirmation;
    }
    
}
