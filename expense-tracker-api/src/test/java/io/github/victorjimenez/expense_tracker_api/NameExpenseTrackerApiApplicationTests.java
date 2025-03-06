package io.github.victorjimenez.expense_tracker_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EntityScan("io.github.victorjimenez.expense_tracker_api.models")
class NameExpenseTrackerApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
