package com.yahoo.slykhachov.chess.ai;

import java.util.function.Predicate;
import com.yahoo.slykhachov.chess.model.*;

class StaticEvaluator {
	/*
	https://en.wikipedia.org/wiki/Evaluation_function#In_chess
	One popular strategy for constructing evaluation functions is as a weighted 
	sum of various factors that are thought to influence the value of a position. 
	For instance, an evaluation function for chess might take the form
	c1 * material + c2 * mobility + c3 * king safety + c4 * center control + ...
	Such as
	f(P) = 9(Q-Q') + 5(R-R') + 3(B-B'+N-N') + (P-P') - 0.5(D-D'+S-S'+I-I') + 0.1(M-M') + ...
	in which:
	Q, R, B, N, P are the number of white queens, rooks, bishops, knights and pawns 
	on the board.
	D, S, I are doubled, backward and isolated white pawns.
	M represents white mobility (measured, say, as the number of legal moves available to White).[1]
	*/
	static int evaluate(BoardModel board, AdversaryModel adversary) {
		return materialFactor(board, adversary)
			   + mobilityFactor(board, adversary);
			   //- 50 * ();
	}
	private static int mobilityFactor(BoardModel board, AdversaryModel adversary) {
		return 10 * (adversary.generateAllPossibleMoves(board).size()
			- adversary.getOpponent().generateAllPossibleMoves(board).size());
	}
	private static int materialFactor(BoardModel board, AdversaryModel adversary) {
		PieceModel[] maximizerPieces = adversary.getPieces();
		PieceModel[] minimizerPieces = adversary.getOpponent().getPieces();
		int maximizerQueenCount 
			= maximizerPieces[maximizerPieces.length - 1].isCaptured() == true ? 0 : 1;
		int minimizerQueenCount 
			= minimizerPieces[minimizerPieces.length - 1].isCaptured() == true ? 0 : 1;
		int maximizerRookCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(RookModel.class),
			2,
			maximizerPieces
		);
		int minimizerRookCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(RookModel.class),
			2,
			minimizerPieces
		);
		int maximizerBishopCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(BishopModel.class),
			2,
			maximizerPieces
		);
		int minimizerBishopCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(BishopModel.class),
			2,
			minimizerPieces
		);
		int maximizerKnightCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(KnightModel.class),
			2,
			maximizerPieces
		);
		int minimizerKnightCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(KnightModel.class),
			2,
			minimizerPieces
		);
		int maximizerPawnCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(PawnModel.class),
			8,
			maximizerPieces
		);
		int minimizerPawnCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(PawnModel.class),
			8,
			minimizerPieces
		);
		///////////////////////////////////////////////////////////////////
		int maximizerPromotedPawnCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(PawnModel.class) 
				&& ((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED),
			8,
			maximizerPieces
		);
		int minimizerPromotedPawnCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(PawnModel.class)
				&& ((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED),
			8,
			minimizerPieces
		);
		///////////////////////////////////////////////////////////////////
		return 900 * (maximizerQueenCount - minimizerQueenCount)
			   ////////////////////////////////////////////////////////////
			   + 800 * (maximizerPromotedPawnCount - minimizerPromotedPawnCount)
			   ////////////////////////////////////////////////////////////
			   + 500 * (maximizerRookCount - minimizerRookCount)
			   + 325 * (maximizerBishopCount - minimizerBishopCount)
			   + 300 * (maximizerKnightCount - minimizerKnightCount)
			   + 100 * (maximizerPawnCount - minimizerPawnCount);
	}
	private static int findNonCapturedPieceCount(Predicate<PieceModel> condition,
			 int maxCount, PieceModel[] pieces) {
		int remainingInPlayCount = 0;
		int encounteredCount = 0;
		for (PieceModel piece : pieces) {
			if (condition.test(piece)) {
				encounteredCount++;
				if (!piece.isCaptured()) {
					remainingInPlayCount++;
				}	
			}
			if (encounteredCount == maxCount) {
				break;
			}
		}
		return remainingInPlayCount;
	}
}
