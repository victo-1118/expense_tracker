package io.github.victorjimenez.expense_tracker_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

@EntityScan(basePackages = {
    "io.github.victorjimenez.expense_tracker_api.models",
    "io.github.victorjimenez.expense_tracker_api.snapshots",
	"io.github.victorjimenez.expense_tracker_api.repository"
	,
	"io.github.victorjimenez.expense_tracker_api.services"
})
class NameExpenseTrackerApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
