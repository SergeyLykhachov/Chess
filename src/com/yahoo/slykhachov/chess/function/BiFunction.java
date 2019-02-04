package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface BiFunction<A> {
	int apply(A a, byte i);
}
