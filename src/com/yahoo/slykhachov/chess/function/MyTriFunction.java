package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface MyTriFunction<A, P, R> {
	R apply(A a, P p, byte val);
}
