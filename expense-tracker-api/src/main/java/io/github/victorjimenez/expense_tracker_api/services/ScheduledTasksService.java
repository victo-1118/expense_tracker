package io.github.victorjimenez.expense_tracker_api.services;

import java.time.LocalDate;

import io.github.victorjimenez.expense_tracker_api.models.Expense;

public interface ScheduledTasksService {
    void checkForCreditCardEndOfBillingCycle();
    Expense chargeAnnualCreditFee();
    // yes i think expense works. Why create scheduled fee if Expense kind of does the job of that?

}
