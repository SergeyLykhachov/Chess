package com.yahoo.slykhachov.chess.model;

import com.yahoo.slykhachov.chess.util.PositionTranslator;

abstract class AbstractPieceModel implements PieceModel {
	private int row;
	private int col;
	private boolean isCaptured;
	private final Class<? extends AdversaryModel> adversary;
	protected AbstractPieceModel(String s, Class<? extends AdversaryModel> adversary) {
		this.isCaptured = false;
		this.adversary = adversary;
		String theRealPosition = PositionTranslator.translate(s);
		this.row = Integer.parseInt(String.valueOf(theRealPosition.charAt(0)));
		this.col = Integer.parseInt(String.valueOf(theRealPosition.charAt(1)));
	}
	@Override
	public boolean isCaptured() {
		return isCaptured;
	}
	@Override
	public void setCaptured(boolean b) {
		this.isCaptured = b;
	}
	@Override
	public void setRow(int row) {
		this.row = row;
	}
	@Override
	public void setCol(int col) {
		this.col = col;
	}
	@Override
	public int getRow() {
		return this.row;
	}
	@Override
	public int getCol() {
		return this.col;
	}
	@Override
	public Class<? extends AdversaryModel> getAdversary() {
		return this.adversary;
	}
}
