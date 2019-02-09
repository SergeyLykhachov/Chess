package com.yahoo.slykhachov.chess.model;

import java.util.Stack;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.White;

public class BoardModel {
	private PieceModel[][] board;
	private Stack<Tuple> stack;
	private AdversaryModel adversary1;
	private AdversaryModel adversary2;
	private int finalScore;
	public BoardModel(AdversaryModel adv1, AdversaryModel adv2) {
		this.init(adv1, adv2);
	}
	private void init(AdversaryModel adv1, AdversaryModel adv2) {
		this.board = new PieceModel[8][8];
		this.adversary1 = adv1;
		this.adversary2 = adv2;
		loadPieces(adversary1, this.board);
		loadPieces(adversary2, this.board);
		this.adversary1.setOpponent(this.adversary2);
		this.adversary2.setOpponent(this.adversary1);
		this.stack = new Stack<>();
	}
	private static void loadPieces(AdversaryModel adv, PieceModel[][] board) {
		for (PieceModel piece : adv.getPieces()) {
			if (!piece.isCaptured()) {
				board[piece.getRow()][piece.getCol()] = piece;
			}
		}
	}
	public void reset(AdversaryModel adv1, AdversaryModel adv2) {
		this.init(adv1, adv2);
	}
	public boolean isSafeToUndo() {
		return this.stack.size() >= 2;
	}
	public int getNumberOfMovesPerformed() {
		return this.stack.size();
	}
	public void performMove(Move move) {
		int finRow = move.getFinalRow();
		int finCol = move.getFinalColumn();
		Tuple tuple;
		PieceModel piece = move.getPiece();
		AdversaryModel opponent = 
			piece.getAdversary().equals(adversary1.getClass()) ? adversary2 : adversary1;
		if (this.board[finRow][finCol] == null) {
			if (move.getSpecialPiece() == null) {
				if (piece.getClass().equals(PawnModel.class)) {
					PawnModel pawn = (PawnModel) piece;
					AdversaryModel adv = pawn.getAdversary().equals(adversary1.getClass())
						? adversary1 : adversary2;
					switch (pawn.getCurrentState()) {
						case YET_TO_BE_MOVED:
							if (Math.abs(finRow - pawn.getRow()) > 1) {
								pawn.setCurrentState(PawnState.EN_PASSANT_VULNERABLE);
								adv.setEnPassantVulnerablePawn(pawn);
							} else {
								pawn.setCurrentState(PawnState.DEVELOPED);
							}
							break;
						case DEVELOPED:
							if (pawn.getAdversary().equals(White.class)) {
								if (finRow != 0) {
									pawn.setCurrentState(PawnState.DEVELOPED);
								} else {
									pawn.setCurrentState(PawnState.PROMOTED);
								}
							} else {
								if (finRow != 7) {
									pawn.setCurrentState(PawnState.DEVELOPED);
								} else {
									pawn.setCurrentState(PawnState.PROMOTED);
								}
							}
							break;
						case PROMOTED:
							pawn.setCurrentState(PawnState.PROMOTED);
							break;
						case EN_PASSANT_VULNERABLE:
							pawn.setCurrentState(PawnState.DEVELOPED);
							adv.setEnPassantVulnerablePawn(null);
							break;
						case CAPTURED_EN_PASSANT:
							throw new IllegalArgumentException();
						default:
							throw new IllegalArgumentException();
					}
				} else {
					if (piece.getClass().equals(RookModel.class)) {
						((RookModel) piece).incrementNumberOfMovesMade();
					} else {
						if (piece.getClass().equals(KingModel.class)) {
							((KingModel) piece).incrementNumberOfMovesMade();
						}
					}
				}	
				if (opponent.getEnPassantVulnerablePawn() != null) {
					opponent.getEnPassantVulnerablePawn()
					        .setCurrentState(PawnState.DEVELOPED);
					opponent.setEnPassantVulnerablePawn(null);
				}
				tuple = new Tuple(move, null);
			} else {
				PieceModel specialPiece = move.getSpecialPiece();
				if (specialPiece.getClass().equals(PawnModel.class)) {
					((PawnModel) piece).setCurrentState(PawnState.DEVELOPED);
					specialPiece.setCaptured(true);
					((PawnModel) specialPiece).setCurrentState(PawnState.CAPTURED_EN_PASSANT);
					board[specialPiece.getRow()][specialPiece.getCol()] = null;
					opponent.setEnPassantVulnerablePawn(null);	
				} else {
					((KingModel) piece).incrementNumberOfMovesMade();
					((RookModel) specialPiece).incrementNumberOfMovesMade();
					if (finCol > move.getInitColumn()) {
						specialPiece.setCol(finCol - 1);
						this.board[finRow][finCol - 1] = specialPiece;
						this.board[finRow][7] = null;
					} else {
						specialPiece.setCol(finCol + 1);
						this.board[finRow][finCol + 1] = specialPiece;
						this.board[finRow][0] = null;
					}
					if (opponent.getEnPassantVulnerablePawn() != null) {	
						opponent.getEnPassantVulnerablePawn()
						        .setCurrentState(PawnState.DEVELOPED);
						opponent.setEnPassantVulnerablePawn(null);
					}
				}
				tuple = new Tuple(move, null);
			}
		} else {
			if (piece.getClass().equals(PawnModel.class)) {
				if (piece.getAdversary().equals(White.class)) {
					PawnModel pawn = (PawnModel) piece;
					if (finRow != 0) {
						if (!pawn.getCurrentState().equals(PawnState.PROMOTED)) {
							pawn.setCurrentState(PawnState.DEVELOPED);
						} else {
							pawn.setCurrentState(PawnState.PROMOTED);
						}
					} else {
						pawn.setCurrentState(PawnState.PROMOTED);
					}
				} else {
					PawnModel pawn = (PawnModel) piece;
					if (finRow != 7) {
						if (!pawn.getCurrentState().equals(PawnState.PROMOTED)) {
							pawn.setCurrentState(PawnState.DEVELOPED);
						} else {
							pawn.setCurrentState(PawnState.PROMOTED);
						}
					} else {
						pawn.setCurrentState(PawnState.PROMOTED);
					}
				}
			} else {
				if (piece.getClass().equals(RookModel.class)) {
					((RookModel) piece).incrementNumberOfMovesMade();
				} else {
					if (piece.getClass().equals(KingModel.class)) {
						((KingModel) piece).incrementNumberOfMovesMade();
					}
				}
			}
			PieceModel p2 = this.board[finRow][finCol];
			if (p2.getClass().equals(PawnModel.class)) {
				((PawnModel) p2).setCurrentState(PawnState.DEVELOPED);
			}
			p2.setCaptured(true);
			if (opponent.getEnPassantVulnerablePawn() != null) {
				opponent.getEnPassantVulnerablePawn()
				        .setCurrentState(PawnState.DEVELOPED);
				opponent.setEnPassantVulnerablePawn(null);
			}
			tuple = new Tuple(move, p2);
		}
		stack.push(tuple);
		piece.setRow(finRow);
		piece.setCol(finCol);
		this.board[finRow][finCol] = piece;
		this.board[move.getInitRow()][move.getInitColumn()] = null;
	}
	public void undoMove() {
		Tuple tuple = stack.pop();
		int initRow = tuple.getCause().getInitRow();
		int initCol = tuple.getCause().getInitColumn();
		int finRow = tuple.getCause().getFinalRow();
		int finCol = tuple.getCause().getFinalColumn();
		PieceModel piece = tuple.getCause().getPiece();
		piece.setRow(initRow);
		piece.setCol(initCol);
		AdversaryModel thisMovesOpponentAdversary = piece.getAdversary().equals(adversary1.getClass())
			? adversary2 : adversary1;
		if (piece.getClass().equals(PawnModel.class)) {
			AdversaryModel thisMovesAdversary = thisMovesOpponentAdversary.getOpponent();
			((PawnModel) piece).reinstatePreviousState();
			switch (((PawnModel) piece).getCurrentState()) {
				case EN_PASSANT_VULNERABLE:
					thisMovesAdversary.setEnPassantVulnerablePawn((PawnModel) piece);
					break;
				case YET_TO_BE_MOVED:
					thisMovesAdversary.setEnPassantVulnerablePawn(null);
					break;
				case DEVELOPED:
					break;
				case PROMOTED:
					break;
				case CAPTURED_EN_PASSANT:
				default:
					throw new IllegalStateException();
			}
			PawnModel inPassingCapturedPawn = (PawnModel) tuple.getCause().getSpecialPiece();
			if (inPassingCapturedPawn != null) {
				inPassingCapturedPawn.reinstatePreviousState();
				inPassingCapturedPawn.setCaptured(false);
				thisMovesOpponentAdversary.setEnPassantVulnerablePawn(inPassingCapturedPawn);
				this.board[inPassingCapturedPawn.getRow()][inPassingCapturedPawn.getCol()]
					= inPassingCapturedPawn;
				this.board[initRow][initCol] = piece;
				this.board[finRow][finCol] = null;
				return;
			}			
		} else {
			if (piece.getClass().equals(RookModel.class)) {
				((RookModel) piece).decrementNumberOfMovesMade();
			} else {
				if (piece.getClass().equals(KingModel.class)) {
					((KingModel) piece).decrementNumberOfMovesMade();
					RookModel castlingRook = (RookModel) tuple.getCause().getSpecialPiece();
					if (castlingRook != null) {
						castlingRook.decrementNumberOfMovesMade();
						if (finCol == 6) {
							board[finRow][5] = null;
							board[finRow][7] = castlingRook;
							castlingRook.setCol(7);
						} else {
							board[finRow][3] = null;
							board[finRow][0] = castlingRook;
							castlingRook.setCol(0);
						}
					}
				}
			}
		}
		this.board[initRow][initCol] = piece;
		if (tuple.getEffect() == null) {
			this.board[finRow][finCol] = null;			
		} else {
			PieceModel capturedPiece = tuple.getEffect();
			this.board[capturedPiece.getRow()][capturedPiece.getCol()] = capturedPiece;
			capturedPiece.setCaptured(false);
			if (capturedPiece.getClass().equals(PawnModel.class)) {
				((PawnModel) capturedPiece).reinstatePreviousState();
				if (((PawnModel) capturedPiece).getCurrentState().equals(PawnState.EN_PASSANT_VULNERABLE)) {
					thisMovesOpponentAdversary.setEnPassantVulnerablePawn((PawnModel) capturedPiece);
					return;
				}
			}
		}
		if (!stack.empty()) {
			PieceModel p = stack.peek().getCause().getPiece();
			if (p.getClass().equals(PawnModel.class)) {
				if (((PawnModel) p).getPreviousState() == PawnState.EN_PASSANT_VULNERABLE) {
					((PawnModel) p).reinstatePreviousState();
					thisMovesOpponentAdversary.setEnPassantVulnerablePawn((PawnModel) p);
				}
			}
		}
	}
	public PieceModel[][] getBoard() {
		return this.board;
	}
	public AdversaryModel[] getAdversaries() {
		return new AdversaryModel[] {adversary1, adversary2};
	}
	public static String staticToString(Object[][] board) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t");
		for (int i = 0, row = 8; i < board.length; i++, row--) {
			if (i == 0) {
				for (int numOfLetters = 0, letter = 0x61; numOfLetters < 8; numOfLetters++, letter++) {
					sb.append("   " + (char) letter);
				}
				sb.append("\n\t  -------------------------------\n\t");
			}
			for (int k = 0; k < board[i].length; k++) {
				if (board[i][k] != null) {	
					if (k == 0) {
						sb.append(row + "|");
					}
					if (board[i][k].toString().length() == 3) {
						sb.append(board[i][k] + "|");
					} else {
						sb.append(board[i][k] + " |");
					}
				} else {
					if (k == 0) {
						sb.append(row + "|");
					}
					sb.append("   |");
				}	
			}
			sb.append(row);
			sb.append("\n");
			sb.append("\t");
			if (i < board.length - 1) {
				sb.append(" |---|---|---|---|---|---|---|---|\n\t");
			} else {
				sb.append("  -------------------------------\n\t");
				for (int numOfLetters = 0, letter = 0x61; numOfLetters < 8; numOfLetters++, letter++) {
					sb.append("   " + (char) letter);
				}
			}
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		return staticToString(this.board);
	}
	private static class Tuple {
		private Move cause;
		private PieceModel effect;
		Tuple(Move cause, PieceModel effect) {
			this.cause = cause;
			this.effect = effect;
		}
		Move getCause() {
			return this.cause;
		}
		PieceModel getEffect() {
			return this.effect;
		}
	}
}
