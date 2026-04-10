package org.kit.filter.query;

import java.util.function.Predicate;

public interface PredicateMaker {

	<T> Predicate<T> criterion();
}
