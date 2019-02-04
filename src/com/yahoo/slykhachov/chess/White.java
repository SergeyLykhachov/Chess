package com.yahoo.slykhachov.chess;

import com.yahoo.slykhachov.chess.model.*;

public final class White extends AbstractAdversaryModel {
	public White() {
		super(
			new PieceModel[] {
				new QueenModel("d1", White.class),
				new BishopModel("f1", White.class),
				new BishopModel("c1", White.class),
				new KnightModel("b1", White.class),
				new KnightModel("g1", White.class),
				new RookModel("a1", White.class), 
				new RookModel("h1", White.class),
				new PawnModel("a2", White.class), 
				new PawnModel("b2", White.class),
				new PawnModel("c2", White.class), 
				new PawnModel("d2", White.class),
				new PawnModel("e2", White.class),
				new PawnModel("f2", White.class),
				new PawnModel("g2", White.class),
				new PawnModel("h2", White.class),
				new KingModel("e1", White.class)		
			}
		);
	}
	@Override
	public String toString() {
		return "White";
	}
}
