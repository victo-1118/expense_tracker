package io.github.victorjimenez.expense_tracker_api.dto;

public class ExpenseTypeEntityDTO {
    private String expenseType;
    private double budgetAmount;
    private Long id;

    public ExpenseTypeEntityDTO(Long id, String expenseType, double budgetAmount) {
        this.expenseType = expenseType;
        this.budgetAmount = budgetAmount;
        this.id = id;
    }

    public String getExpenseType() {
        return expenseType;
    }



    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public Long getId() {
        return id;
    }
}
