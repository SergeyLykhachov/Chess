package com.yahoo.slykhachov.chess.model;

import java.util.Stack;
import java.util.function.BiFunction;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.Black;

public final class PawnModel extends AbstractPieceModel {
	public static final BiFunction<BoardModel, PieceModel, Move[]> QUEEN_MOOVE_GENERATOR = MoveGenerators::queenMoves;
	public static final BiFunction<BoardModel, PieceModel, Move[]> ROOK_MOOVE_GENERATOR = MoveGenerators::rookMoves;
	public static final BiFunction<BoardModel, PieceModel, Move[]> BISHOP_MOOVE_GENERATOR = MoveGenerators::bishopMoves;
	public static final BiFunction<BoardModel, PieceModel, Move[]> KNIGHT_MOOVE_GENERATOR = MoveGenerators::knightMoves;
	private BiFunction<BoardModel, PieceModel, Move[]> promotedStateMoveGenerator;
	private PawnState currentState;
	private final Stack<PawnState> stack; 
	public PawnModel(String s, Class<? extends AdversaryModel> adversary) {
		super(s, adversary);
		currentState = PawnState.YET_TO_BE_MOVED;
		stack = new Stack<>();
		promotedStateMoveGenerator = QUEEN_MOOVE_GENERATOR;
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel b) {
		return currentState.equals(PawnState.PROMOTED) 
			? promotedStateMoveGenerator.apply(b, this) 
			: currentState.generateAllPossibleMoves(b, this);
	}
	public void setPromotedStateMoveGenerator(BiFunction<BoardModel, PieceModel, Move[]> newMoveGenerator) {
		this.promotedStateMoveGenerator = newMoveGenerator;
	}
	public BiFunction<BoardModel, PieceModel, Move[]> getPromotedStateMoveGenerator() {
		return this.promotedStateMoveGenerator;
	}
	public PawnState getPreviousState() {
		return stack.peek();
	}
	public void reinstatePreviousState() {
		this.currentState = stack.pop();
	}
	public void setCurrentState(PawnState state) {
		stack.push(currentState);
		this.currentState = state;
	}
	public PawnState getCurrentState() {
		return this.currentState;
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Black.class) ? "B" : "W";
		return !this.currentState.equals(PawnState.PROMOTED) 
			? s + "P" : promotedStateMoveGenerator.equals(QUEEN_MOOVE_GENERATOR) ? s + "QP"
			: promotedStateMoveGenerator.equals(ROOK_MOOVE_GENERATOR) ? s + "RP"
			: promotedStateMoveGenerator.equals(BISHOP_MOOVE_GENERATOR) ? s + "BP"
			: s + "NP";
	}
}
