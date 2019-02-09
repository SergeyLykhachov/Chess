package com.yahoo.slykhachov.chess.ai;

import java.util.List;
import com.yahoo.slykhachov.chess.model.AdversaryModel;
import com.yahoo.slykhachov.chess.model.BoardModel;
import com.yahoo.slykhachov.chess.Best;
import com.yahoo.slykhachov.chess.Move;

public class MoveChooser {
	public static final boolean MAXIMIZER = true;
	public static final boolean MINIMIZER = false;
	public static Best miniMaxAlphaBeta(boolean side, AdversaryModel adversary,
			AdversaryModel alternatingAdversary, BoardModel board, int alpha, int beta,
			int depth) {
		if (alternatingAdversary.getPieces()[15].isCaptured()) {
			Best finale = new Best();
			if (alternatingAdversary == adversary) {
				finale.setScore(Integer.MIN_VALUE);
				return finale;
			} else {
				finale.setScore(Integer.MAX_VALUE);
				return finale;
			}
		} else {
			if (depth == 0) {
				//Best finale = new Best();
				//finale.setScore(StaticEvaluator.evaluate(board, adversary));
				//return finale;
				Best finale = new Best();
				if (alternatingAdversary.isChecked(board)) {
					if (alternatingAdversary == adversary) {
						finale.setScore(Integer.MAX_VALUE);
						return finale;
					} else {
						finale.setScore(Integer.MIN_VALUE);
						return finale;
					}
				} else {
					finale.setScore(StaticEvaluator.evaluate(board, adversary));
				    return finale;
				}
			}
			//Possibly check for stale mate here
		} 
		Best myBest = new Best(); 
		Best reply;               
		if (side == MAXIMIZER) {
			myBest.setScore(alpha);
		} else {
			myBest.setScore(beta);
		}	
		List<Move> moves = alternatingAdversary.generateAllPossibleMoves(board);
		presortMoves(moves, board, alternatingAdversary);
		for (Move move : moves) {
			board.performMove(move);
			reply = miniMaxAlphaBeta(
				!side,
				adversary,
				alternatingAdversary.getOpponent(),
				board,
				alpha,
				beta,
				depth - 1
			);
			board.undoMove();
			if ((side == MAXIMIZER) //COMPUTER
			    	&& (reply.getScore() > myBest.getScore())) {
				myBest.setMove(move);
				myBest.setScore(reply.getScore());
				alpha = reply.getScore();
			} else {
				if ((side == MINIMIZER) //HUMAN
						&& (reply.getScore() < myBest.getScore())) {
					myBest.setMove(move);
					myBest.setScore(reply.getScore());
					beta = reply.getScore();
				}
			}
			if (alpha >= beta) {
				return myBest;
			}
		}
		return myBest;
	}
	private static void presortMoves(List<Move> list, BoardModel bModel,
			AdversaryModel adv) {
		int[] scores = new int[list.size()];
		for (int i = 0; i < scores.length; i++) {
			bModel.performMove(list.get(i));
			scores[i] = StaticEvaluator.evaluate(bModel, adv);
			bModel.undoMove();
		}
		int indexOfMax = 0;
		for (int i = 0; i < Math.min(5, scores.length); i++) {
			int max = Integer.MIN_VALUE;
			int currentIterationIndexOfMax = 0;
			for (int j = indexOfMax; j < scores.length; j++) {
				int temp = scores[j];
				if (temp > max) {
					max = temp;
					currentIterationIndexOfMax = j;
				}
			}
			Move maxMove = list.get(currentIterationIndexOfMax);
			list.set(
				currentIterationIndexOfMax,
				list.get(indexOfMax)
			);
			list.set(
				indexOfMax,
				maxMove
			);
			indexOfMax++;
		}
	}
}
