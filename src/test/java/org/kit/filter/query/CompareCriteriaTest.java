package org.kit.filter.query;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompareCriteriaTest {

	private final BiFunction<Entity, String, ?> resolver = (e, f) -> switch (f) {
		case "id" -> e.id();
		case "name" -> e.name();
		case "createdAt" -> e.createdAt();
		default -> e.attributes().get(f);
	};

	private final BiFunction<String, String, ?> coarser = (f, v) -> switch (f) {
		case "id" -> Long.parseLong(v);
		case "name" -> v;
		case "createdAt" -> LocalDateTime.parse(v, CoarseUtil.LAX_DATE_TIME_FORMATTER);
		default -> v;
	};

	@ParameterizedTest
	@ValueSource(strings = {
			// equals
			"id = 3",
			"name = 'name'",
			"createdAt = '2026-03-24T00:00'",
			"createdAt = '2026-03-24'",
			"key = 'value'",
			"3 = id",
			"id == 3",
			"not id = 1",
			"NOT id = 1",
			"NOT name = 'not name'",
			"NOT createdAt = '1975-03-24T00:00'",
			"NOT createdAt = '1975-03-24'",
			"NOT key = 'key'",
			// greater
			"id > 2",
			"createdAt > '2026-03-23T00:00'",
			"createdAt > '2026-03-23'",
			"4 > id",
			"not id > 4",
			"NOT id > 4",
			"NOT createdAt > '2026-03-24T00:00'",
			"NOT createdAt > '2026-03-25'",
			// lesser
			"id < 4",
			"createdAt < '2026-03-26T00:00'",
			"createdAt < '2026-03-26'",
			"2 < id",
			"not id < 3",
			"NOT id < 3",
			"NOT createdAt < '2026-03-24T00:00'",
			"NOT createdAt < '2026-03-24'",
			// contains
			"name contains 'na'",
			"name CONTAINS 'na'",
			"key CONTAINS 'val'",
			"'name a' CONTAINS name",
			"not name CONTAINS '1'",
			"NOT name CONTAINS '1'",
			// between
			"id between 1 and 4",
			"id BETWEEN 1 AND 4",
			"createdAt BETWEEN '2026-03-23T00:00' AND '2026-03-25T00:00'",
			"createdAt BETWEEN '2026-03-24' AND '2026-03-25'",
			"not id BETWEEN 10 AND 14",
			"NOT id BETWEEN 10 AND 14",
			"NOT createdAt BETWEEN '2026-03-25T00:00' AND '2026-03-26T00:00'",
			"NOT createdAt BETWEEN '2026-03-20' AND '2026-03-21'",
			// in
			"id in (1,2,3)",
			"id IN (1,2,3)",
			"key IN ('value')",
			"NOT id IN (1,2)",
			"NOT key IN ('another')",
	})
	public void test(String query) throws ParseException {
		FilterQuery parser = new FilterQuery(new StringReader(query))
				.withFieldResolver(resolver)
				.withValueCoarser(coarser);
		Expression e = parser.parse();
		e.dump("    ");
		Entity entity = new Entity(3, "name", LocalDateTime.of(2026, 3, 24, 0, 0), Map.of("key", "value"));
		assertTrue(e.criterion().test(entity),  () -> "Expect positive assertion of '" + query + "' on " + entity);
	}

	record Entity(long id, String name, LocalDateTime createdAt, Map<String, String> attributes) {}
}
