package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface MyBiFunction<A, R> {
	R apply(A a, byte val);
}
