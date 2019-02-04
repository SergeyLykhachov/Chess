package com.yahoo.slykhachov.chess;

import com.yahoo.slykhachov.chess.model.PawnModel;
import com.yahoo.slykhachov.chess.model.PieceModel;

@SuppressWarnings("overrides")
public class Move {
	private int initRow;
	private int initCol;
	private int finalRow;
	private int finalCol;
	private PieceModel piece;
	private PieceModel specialPiece;
	public Move(PieceModel piece, int initRow, int initCol,
			int finalRow, int finalCol, PieceModel specialPiece) {
		this.piece = piece;
		this.initRow = initRow;
		this.initCol = initCol;
		this.finalRow = finalRow;
		this.finalCol = finalCol;
		this.specialPiece = specialPiece;
	}
	public PieceModel getPiece() {
		return this.piece;
	}
	public int getInitRow() {
		return this.initRow;
	}
	public int getInitColumn() {
		return this.initCol;
	}
	public int getFinalRow() {
		return this.finalRow;
	}
	public int getFinalColumn() {
		return this.finalCol;
	}
	public PieceModel getSpecialPiece() {
		return this.specialPiece;
	}
	public boolean partiallyEquals(Move other) {
        if (other == null) {
            return false;
        }
        if (this.initRow != other.initRow) {
            return false;
        } else {
        	if (this.initCol != other.initCol) {
        		return false;
        	} else {
        		if (this.finalRow != other.finalRow) {
        			return false;
        		} else {
        			if (this.finalCol != other.finalCol) {
        				return false; 
        			} else {
        				if (this.piece != other.piece) {
        					return false;
        				}
        			}
        		}
        	}
        }
        return true;
	}
	@Override 
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Move other = (Move) obj;
        if (this.initRow != other.initRow) {
            return false;
        } else {
        	if (this.initCol != other.initCol) {
        		return false;
        	} else {
        		if (this.finalRow != other.finalRow) {
        			return false;
        		} else {
        			if (this.finalCol != other.finalCol) {
        				return false; 
        			} else {
        				if (this.piece != other.piece) {
        					return false;
        				} else {
        					if (this.specialPiece != other.specialPiece) {
        						return false;
        					}
        				}
        			}
        		}
        	}
        }
        return true;
	}
	public static String staticToString(Move move, String pieceInfo, String state) {
		String enPassantCapturedPawn = move.specialPiece == null ? "null"
			: move.specialPiece.getClass().equals(PawnModel.class) ? move.specialPiece.toString() : "null";
		return pieceInfo + " (" + move.initRow + " " + move.initCol + ")->("
			+ move.finalRow + " " + move.finalCol + "), " + "move's pawn state: " + state + ", move's en passant captured pawn: " + enPassantCapturedPawn;
	}
	@Override
	public String toString() {
		String pawnState = this.piece.getClass().equals(PawnModel.class) ? ((PawnModel) piece).getCurrentState().toString() : "null";
		String enPassantCapturedPawn = this.specialPiece == null ? "null"
			: this.specialPiece.getClass().equals(PawnModel.class) ? this.specialPiece.toString() : "null";
		return piece.toString() + " (" + initRow + " " + initCol + ")->("
			+ finalRow + " " + finalCol + "), " + "move's pawn state: " + pawnState + ", move's en passant captured pawn: " + enPassantCapturedPawn;
	}
}
