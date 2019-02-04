package com.yahoo.slykhachov.chess;

import com.yahoo.slykhachov.chess.model.*;

public final class Black extends AbstractAdversaryModel {
	public Black() {
		super(
			new PieceModel[] {
				new QueenModel("d8", Black.class),
				new BishopModel("c8", Black.class), 
				new BishopModel("f8", Black.class),
				new KnightModel("b8", Black.class), 
				new KnightModel("g8", Black.class),
				new RookModel("a8", Black.class), 
				new RookModel("h8", Black.class),
				new PawnModel("a7", Black.class), 
				new PawnModel("b7", Black.class),
				new PawnModel("c7", Black.class), 
				new PawnModel("d7", Black.class),
				new PawnModel("e7", Black.class), 
				new PawnModel("f7", Black.class),
				new PawnModel("g7", Black.class), 
				new PawnModel("h7", Black.class),
				new KingModel("e8", Black.class)
			}
		);
	}
	@Override
	public String toString() {
		return "Black";
	}
}
