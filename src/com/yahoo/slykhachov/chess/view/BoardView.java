package com.yahoo.slykhachov.chess.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import com.yahoo.slykhachov.chess.ChessGame;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.White;
import com.yahoo.slykhachov.chess.model.AdversaryModel;
import com.yahoo.slykhachov.chess.model.BoardModel;
import com.yahoo.slykhachov.chess.model.BishopModel;
import com.yahoo.slykhachov.chess.model.KingModel;
import com.yahoo.slykhachov.chess.model.KnightModel;
import com.yahoo.slykhachov.chess.model.QueenModel;
import com.yahoo.slykhachov.chess.model.PawnModel;
import com.yahoo.slykhachov.chess.model.PawnState;
import com.yahoo.slykhachov.chess.model.PieceModel;
import com.yahoo.slykhachov.chess.model.RookModel;

import static java.util.stream.Collectors.toList;

public class BoardView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static BufferedImage pieces;
	private static BufferedImage black_pawn_image;  
	private static BufferedImage white_pawn_image;  
	private static BufferedImage black_rook_image;  
	private static BufferedImage white_rook_image;  
	private static BufferedImage black_knight_image;
	private static BufferedImage white_knight_image;
	private static BufferedImage black_bishop_image;
	private static BufferedImage white_bishop_image;
	private static BufferedImage black_queen_image; 
	private static BufferedImage white_queen_image; 
	private static BufferedImage black_king_image;  
	private static BufferedImage white_king_image;  
	static {
		initImage();
		int imageViewWidth = pieces.getWidth() / 8;
		int imageViewHeight = pieces.getHeight() / 8;
		black_pawn_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 5),
				image.getHeight() - ((imageViewHeight * 7) - 5),
				imageViewWidth,
				imageViewHeight
		));
		white_pawn_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				5,
				image.getHeight() - (imageViewWidth * 2),
				imageViewWidth, 
				imageViewHeight
		));
		black_rook_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
			    5,
				5,
				imageViewWidth,
				imageViewHeight
		));
		white_rook_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
			    5,
				image.getHeight() - (imageViewHeight + 5),
				imageViewWidth, 
				imageViewHeight
		));
		black_knight_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 2),
				5,
				imageViewWidth,
				imageViewHeight
		));
		white_knight_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - ((imageViewWidth * 7) - 5),
				image.getHeight() - (imageViewHeight + 5),
				imageViewWidth, 
				imageViewHeight
		));
		black_bishop_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 6),
				5,
				imageViewWidth,
				imageViewHeight
		));
		white_bishop_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - ((imageViewWidth * 3)),
				image.getHeight() - (imageViewHeight + 5),
				imageViewWidth, 
				imageViewHeight
		));
		black_queen_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 5),
				5,
				imageViewWidth,
				imageViewHeight
		));
		white_queen_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 4),
				image.getHeight() - (imageViewHeight + 5),
				imageViewWidth, 
				imageViewHeight
		));
		black_king_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 4),
				5,
				imageViewWidth,
				imageViewHeight
		));
		white_king_image = initPieceImage(
			pieces,
			image -> image.getSubimage(
				image.getWidth() - (imageViewWidth * 5),
				image.getHeight() - (imageViewHeight + 5),
				imageViewWidth, 
				imageViewHeight
		));
	}
	private ChessGame chessGame;
	private BoardModel boardModel;
	private HashMap<String, PieceView> views;
	private PieceView actedUponPieceView = null;
	private DefaultListModel<String> listModel;
	public BoardView(ChessGame chessGame, BoardModel boardModel) {
		this.views = new HashMap<>();
		this.setChessGame(chessGame);
		this.setBoardModel(boardModel);
		List<PieceModel> models = getNonCapturedPieceModels(getBoardModel());
		this.setPreferredSize(new Dimension(500, 500));
		initViews(
			this.views,
			models,
			getPreferredSize()
		);	
		BoardViewMouseListener listener = new BoardViewMouseListener();
		this.addMouseMotionListener(listener);
		this.addMouseListener(listener);
		this.setBorder(
			new CompoundBorder(
				new BevelBorder(BevelBorder.RAISED),
				new EtchedBorder()
			)
		);
	}
	private static void initImage() {
		try {
			pieces = ImageIO.read(new File("./img/Pieces2.png"));
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	private static BufferedImage initPieceImage(BufferedImage pieceImage,
				Function<BufferedImage, BufferedImage> function) {
		return function.apply(pieceImage);
	}
	private static List<PieceModel> getNonCapturedPieceModels(BoardModel bm) {
		return Stream.of(bm.getAdversaries())
			.flatMap(adversary -> Stream.of(adversary.getPieces()))
			.filter(model -> !model.isCaptured())
			.collect(toList());
	}
	private static void initViews(HashMap<String, PieceView> pieceViews,
			List<PieceModel> models, Dimension dimension) {
		BufferedImage bi;
		boolean isWhite;
		String key;   
		for (PieceModel pModel : models) {
			isWhite = pModel.getAdversary().equals(White.class);
			key = String.valueOf(pModel.getRow()) + String.valueOf(pModel.getCol());
			if (pModel.getClass().equals(PawnModel.class)) {
				if (isWhite) {
					if (((PawnModel) pModel).getCurrentState().equals(PawnState.PROMOTED)) {
						bi = white_queen_image;
					} else {
						bi = white_pawn_image;
					}
				} else {
					if (((PawnModel) pModel).getCurrentState().equals(PawnState.PROMOTED)) {
						bi = black_queen_image;
					} else {
						bi = black_pawn_image;
					}
				}
				PieceView pawnView = new PieceView(
					convertToPoint2D(
						pModel.getRow(),
						pModel.getCol(), 
						dimension
					),
					bi
				);
				pieceViews.put(
					key,
					pawnView
				);
			} else {  
				if (pModel.getClass().equals(RookModel.class)) {
					if (isWhite) {
						bi = white_rook_image;
					} else {
						bi = black_rook_image;
					}
					PieceView rookView = new PieceView(
						convertToPoint2D(
							pModel.getRow(),
							pModel.getCol(),
							dimension
						),
						bi
					);
					pieceViews.put(
						key,
						rookView
					);
				} else {
					if (pModel.getClass().equals(KnightModel.class)) {
						if (isWhite) {
							bi = white_knight_image;
						} else {
							bi = black_knight_image;
						}
						PieceView knightView = new PieceView(
							convertToPoint2D(
								pModel.getRow(),
								pModel.getCol(),
								dimension
							),
							bi
						);
						pieceViews.put(
							key,
							knightView
						);
					} else {
						if (pModel.getClass().equals(BishopModel.class)) {
							if (isWhite) {
								bi = white_bishop_image;
							} else {
								bi = black_bishop_image;
							}
							PieceView bishopView = new PieceView(
								convertToPoint2D(
									pModel.getRow(),
									pModel.getCol(),
									dimension
								),
								bi
							);
							pieceViews.put(
								key,
								bishopView
							);
						} else {
							if (pModel.getClass().equals(QueenModel.class)) {
								if (isWhite) {
									bi = white_queen_image;
								} else {
									bi = black_queen_image;
								}
								PieceView queenView = new PieceView(
									convertToPoint2D(
										pModel.getRow(),
										pModel.getCol(),
										dimension
									),
									bi
								);
								pieceViews.put(
									key,
									queenView
								);
							} else {
								if (pModel.getClass().equals(KingModel.class)) {
									if (isWhite) {
										bi = white_king_image;
									} else {
										bi = black_king_image;
									}
									PieceView kingView = new PieceView(
										convertToPoint2D(
											pModel.getRow(),
											pModel.getCol(),
											dimension
										),
										bi
									);
									pieceViews.put(
										key,
										kingView
									);
								} else {
									throw new UnsupportedOperationException();
								}
							}
						}
					}
				}
			}
		}
	}
	private static Point2D.Double convertToPoint2D(int row ,int col, Dimension dimension) {
		return new Point2D.Double(
				col * (dimension.getWidth() / 8),
				row * (dimension.getHeight() / 8)
		);
	}
	public HashMap<String, PieceView> getViews() {
		return this.views;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		int squareSideLength = (int) (Math.min(getPreferredSize().getWidth(), getPreferredSize().getHeight()) / 8);
		Color light = new Color(255, 200, 100);
		Color dark = new Color(150, 50, 30);
		Color color = light;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				g2d.setColor(color);	
				g2d.fillRect(i * squareSideLength, j * squareSideLength, squareSideLength, squareSideLength);
				color = color == light ? dark : light;
			}
			color = color == light ? dark : light;
		}
		double heightRatio;
		double widthRatio;
		AffineTransform at = new AffineTransform();
		for (Entry<String, PieceView> e : views.entrySet()) {
			PieceView pv = e.getValue();
			heightRatio = (this.getPreferredSize().getHeight() / 8) / pv.getPieceImage().getHeight();
			widthRatio = (this.getPreferredSize().getWidth() / 8) / pv.getPieceImage().getWidth();
			at.translate(
				pv.getPoint().getX(),
				pv.getPoint().getY()
			);
			at.scale(widthRatio, heightRatio);
			g2d.drawRenderedImage(pv.getPieceImage(), at);
			at.setToIdentity();
		}
		if (actedUponPieceView != null) {
			at.translate(
				actedUponPieceView.getPoint().getX(),
				actedUponPieceView.getPoint().getY()
			);
			heightRatio = (this.getPreferredSize().getHeight() / 8) / actedUponPieceView.getPieceImage().getHeight();
			widthRatio = (this.getPreferredSize().getWidth() / 8) / actedUponPieceView.getPieceImage().getWidth();
			at.scale(widthRatio, heightRatio);
			g2d.drawRenderedImage(actedUponPieceView.getPieceImage(), at);
		}
		g2d.dispose();
	}
	public void updateBoardView() {
		this.getViews().clear();
		BoardView.initViews(
			getViews(),
			getNonCapturedPieceModels(this.getBoardModel()),
			getPreferredSize()
		);
		this.repaint();
	}
	public void setChessGame(ChessGame chessGame) {
		this.chessGame = chessGame;
	}
	public ChessGame getChessGame() {
		return this.chessGame;
	}
	public void setBoardModel(BoardModel boardModel) {
		this.boardModel = boardModel;
	}
	public BoardModel getBoardModel() {
		return this.boardModel;
	}
	public void setListModel(DefaultListModel<String> listModel) {
		this.listModel = listModel;
	}
	private class BoardViewMouseListener extends MouseAdapter {
		private PieceModel pieceModel;
		@Override
		public void mousePressed(MouseEvent me)	{
			if (me.getButton() == MouseEvent.BUTTON1) {
				double x = me.getX();
				double y = me.getY();
				int row = (int) ((y / getPreferredSize().getWidth()) * 8);
				int col = (int) ((x / getPreferredSize().getHeight()) * 8);
				this.pieceModel = getIfCorrectTurnAdversary(getChessGame().getAdversaryToMove(), row, col);
				if (pieceModel != null) {
					actedUponPieceView = views.remove(String.valueOf(row) + String.valueOf(col));
					actedUponPieceView.getPoint()
						.setLocation(
								x - getPreferredSize().getWidth() / 16,
								y - getPreferredSize().getHeight() / 16
					);
					repaint();
				}
			}
		}
		private PieceModel getIfCorrectTurnAdversary(AdversaryModel adv,
				int row, int col) {
			for (PieceModel pm : adv.getPieces()) {
				if (getChessGame().getComputerAdversary() != adv) {
					if (!pm.isCaptured()) {
						if (pm.getRow() == row) {
							if (pm.getCol() == col) {
								return pm;
							}
						}
					}
				}
			}
			return null;
		}
		@Override
		public void mouseReleased(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (actedUponPieceView != null) {
					AdversaryModel adversaryToMove = getChessGame().getAdversaryToMove();
					double x = me.getX();
					double y = me.getY();
					int row = (int) ((y / getPreferredSize().getWidth()) * 8);
					int col = (int) ((x / getPreferredSize().getHeight()) * 8);
					List<Move> listOfMoves = adversaryToMove.generateAllPossibleLegalMoves(getBoardModel());
					Move candidateMove = new Move(
							this.pieceModel,
							this.pieceModel.getRow(),
							this.pieceModel.getCol(),
							row,
							col,
							null
					);
					Move move = null;
					for (Move m : listOfMoves) {
						if (candidateMove.partiallyEquals(m)) {
							move = m;
							break;
						}
					}
					if (move != null) {
						getBoardModel().performMove(move);
						getChessGame().setAdversaryToMove(
								adversaryToMove.getOpponent()
						);
						actedUponPieceView = null;
						if (listModel != null) {
							String s = getBoardModel().getNumberOfMovesPerformed()
									+ ".  " + move.toDisplayableString();
							listModel.addElement(s);
						}
						System.out.println(boardModel);
						updateBoardView();
						getChessGame().doResponce();
					} else {
						actedUponPieceView.getPoint()
								.setLocation(
										pieceModel.getCol() * (getPreferredSize().getWidth() / 8),
										pieceModel.getRow() * (getPreferredSize().getHeight() / 8)
								);
						views.put(
								String.valueOf(pieceModel.getRow()) + String.valueOf(pieceModel.getCol()),
								actedUponPieceView
						);
						repaint();
						actedUponPieceView = null;
					}
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent me)	{
			if (actedUponPieceView != null) {
				int x = me.getX();
				int y = me.getY();
				actedUponPieceView.getPoint()
					.setLocation(
					    x - getPreferredSize().getWidth() / 16,
					    y - getPreferredSize().getHeight() / 16
				);
				repaint();
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override	
		public void mouseEntered(MouseEvent e)	{}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
}
