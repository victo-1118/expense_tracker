package io.github.victorjimenez.expense_tracker_api.models;

public enum CreditCardFeesType {
    ANNUAL_FEE("Annual Fee"), //This fee is proactive
    // yap session:
    // almost every card has an annual fee but not all of them. Suggested solution to be proactive and charge this
    // is to create another class with a relationship with the credit card that has its due date? Might be the most simple
    // Feel like there is something better though might be wrong. Annual fee starts the month you get the card next year
    // do we make the user tell us when they got the card? User might not even remember so that doesnt slide.
    // lowkey i think its better for user to tell us the first due date of the card. They just mark it down as a expense
    // Kind of ruins the purpose of having enums saying what kind of fee it is though I guess.
    // I guess it is better to just have a scheduled fee class even though its more work. Im just worried that itll cause errors


    
    LATE_FEE("Late Fee"), // These other fees are reactive
    OVERLIMIT_FEE("Over Limit Fee");

    private final String displayName;

    CreditCardFeesType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static CreditCardFeesType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null; // Or throw an exception, depending on your needs.
        }
        for (CreditCardFeesType feeType : CreditCardFeesType.values()) {
            if (feeType.getDisplayName().equalsIgnoreCase(text)) {
                return feeType;
            }
        }
        
        throw new IllegalArgumentException("Invalid CreditCardFeesType: " + text + ""); // Or throw new IllegalArgumentException("Invalid BaseCardFeesType: " + text);
    }
}
