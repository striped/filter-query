package org.kit.filter.query;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

public class CoarseUtil {

	public static final DateTimeFormatter LAX_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
			.optionalStart().appendLiteral('-').appendValue(MONTH_OF_YEAR, 2)
			.optionalStart().appendLiteral('-').appendValue(DAY_OF_MONTH, 2)
			.optionalStart().appendLiteral('T').appendValue(HOUR_OF_DAY, 2)
			.appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2)
			.optionalStart().appendLiteral(':').appendValue(SECOND_OF_MINUTE, 2)
			.optionalStart().appendFraction(NANO_OF_SECOND, 0, 9, true)
			.optionalEnd().optionalEnd().optionalEnd().optionalEnd()
			.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
			.parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
			.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			.parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
			.toFormatter()
			.withResolverStyle(ResolverStyle.SMART);

	private CoarseUtil() {
	}
}
