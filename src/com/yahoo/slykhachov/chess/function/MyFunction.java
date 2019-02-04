package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface MyFunction<P> {
	int apply(P p);
}
