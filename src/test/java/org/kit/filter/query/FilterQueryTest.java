package org.kit.filter.query;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilterQueryTest {

	@ParameterizedTest
	@ValueSource(strings = {
			"c > 4 AND (a == 1 OR b < +2) AND NOT d IN ('A','B','C') AND e BETWEEN -100.01 AND 2000.91 AND f CONTAINS 'text'",
			"c > 4 AND (a == 1 OR b < 2) OR NOT d IN ('A','B','C') AND e BETWEEN -100.01 AND 2000.91 AND f CONTAINS 'text'"
	})
	public void test(String query) throws ParseException {
		FilterQuery parser = new FilterQuery(new StringReader(query))
				.withValueCoarser((f, v) -> switch (f) {
					case "a", "b", "c" -> Long.parseLong(v);
					case "e" -> Double.parseDouble(v);
					default -> v;
				});
		Expression expression = parser.parse();
		expression.dump(" ");
		assertNotNull(expression, "Expression was not parsed");
		assertNotNull(expression.criterion(), "Criterion was not provided");
	}
}
