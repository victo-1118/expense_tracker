package projection;

public interface AverageTransactionSummary {

    String getCardNumber();
    double getAverageTransactionAmount();
    long getTransactionCount();
    
}