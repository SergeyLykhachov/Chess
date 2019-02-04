package com.yahoo.slykhachov.chess.ai;

import java.util.List;
import com.yahoo.slykhachov.chess.model.AdversaryModel;
import com.yahoo.slykhachov.chess.model.BoardModel;
import com.yahoo.slykhachov.chess.Best;
import com.yahoo.slykhachov.chess.Move;

public class MoveChooser {
	public static final boolean MAXIMIZER = true;
	public static final boolean MINIMIZER = false;
	/*
	MINI_MAX_ALPHA_BETA PSEUDO CODE (for tic-tac-toe).
	public static Best miniMaxAlphaBeta(boolean side, int alpha, int beta, int depth) {
		Best myBest = new Best(); //My best move
		Best reply;               //Opponent's best reply
		if (the current grid is full or 
				has a win ? or depth == 0) { // a static method in Board class @param Adversary side
			//return a Best with the Grid's score no move
		}
		if (side == MAXIMIZER) {
			myBest = alpha;
		} else {
			myBest = beta;
		}
		for (each legal move m) {
			perform move m; //modifies "this" Grid
			reply = miniMaxWithAlphaBetaProoning(!side, alpha, beta, depth - 1);
			undo move m;    //restores "this" Grid
			if ((side == COMPUTER) &&
			    	(reply.getScore() > myBest.getScore())
				
			) {
				myBest.setMove(move);
				myBest.setScore(reply.getScore());
				alpha = reply.getScore();
			} else {
				if ((side == HUMAN) && 
					(reply.getScore() < myBest.getScore())				
				) {
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
*/
/*
TOP LEVEL METHOD INVOCATION
	miniMaxAlphaBeta(
			MoveChooser.MAXIMIZER,
			computerAdversary,
			computerAdversary,
			boardModel,
			Integer.MIN_VALUE, 
			Integer.MAX_VALUE,
			depth
	);
*/
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
	/*public static Best miniMaxAlphaBeta(boolean side, Adversary adversary,
			Adversary alternatingAdversary, Board board, int alpha, int beta,
			int depth) {
		if (alternatingAdversary.getPieces()[0].isCaptured()) {
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
		} 
		Best myBest = new Best(); 
		Best reply;               
		if (side == MAXIMIZER) {
			myBest.setScore(alpha);
		} else {
			myBest.setScore(beta);
		}	
		List<Move> moves = alternatingAdversary.generateAllPossibleMoves(board);
		//moves.sort(
		//	
		//);
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
	}*/
	/*
	private static boolean isEndGame(Adversary adversary,
			Adversary alternatingAdversary, Board board) {
		return isEndGameByStallMateWithSideEffect(alternatingAdversary, board)
			|| isEndGameByCheckMateWithSideEffect(
					adversary,
					alternatingAdversary,
					board
		); 
	}
	private static boolean isEndGameByCheckMateWithSideEffect(Adversary adversary,
			Adversary alternatingAdversary, Board board) {
		if (alternatingAdversary.isCheckMated(board)) {
			if (alternatingAdversary == adversary) {
				board.setFinalScore(Integer.MIN_VALUE);
			    return true;
			} else {
				board.setFinalScore(Integer.MAX_VALUE);
				return true;
			}
		}
		return false;	
	}
	private static boolean isEndGameByStallMateWithSideEffect(Adversary adversary,
			Board board) {
		if (!adversary.isChecked(board)
				&& adversary.generateAllPossibleLegalMoves(board).size() == 0) {
			board.setFinalScore(0);
			return true;
		}
		return false;	
	}
	*/
}
