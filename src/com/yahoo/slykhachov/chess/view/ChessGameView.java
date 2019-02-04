package com.yahoo.slykhachov.chess.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.swing.JPanel;

public class ChessGameView extends JPanel {
	private BoardView boardView;
	public ChessGameView(BoardView boardView) {
		this.boardView = boardView;
		add(new BoardBorderView(boardView), BorderLayout.CENTER);
		//JPanel panel = new JPanel();
		//panel.setPreferredSize(new Dimension(300, 500));
		//add(panel, BorderLayout.EAST);	
	}
	public BoardView getBoardView() {
		return this.boardView;
	}
	private static class BoardBorderView extends JPanel {
		private Color color;
		BoardBorderView(BoardView boardView) {
			this.setLayout(new GridBagLayout());
			this.color = new Color(150, 50, 30);
			this.setPreferredSize(
				new Dimension(
					(int) (boardView.getPreferredSize().getWidth() + 2 * (boardView.getPreferredSize().getWidth() / 8)),
					(int) (boardView.getPreferredSize().getHeight() + 2 * (boardView.getPreferredSize().getHeight() / 8))
				)
			);
			int squareStepSize = (int) (boardView.getPreferredSize().getWidth() / 8);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
    		//c.weightx = 1;
    		c.gridx = 1;
    		c.gridy = 0;
			this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth(),
					(int) (boardView.getPreferredSize().getHeight() / 8),
					Color.ORANGE,
					SidePanel.CharType.LETTERS,
					xyi -> {
						xyi.setX(xyi.getX() + squareStepSize);
						xyi.setIndex(xyi.getIndex() + 1);
						return xyi;
					}
				),	
				c
			);
			//c.weightx = 0.5;
    		c.gridx = 0;
    		c.gridy = 1;
    		this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth() / 8,
					(int) (boardView.getPreferredSize().getHeight()),
					Color.ORANGE,
					SidePanel.CharType.NUMS,
					xyi -> {
						xyi.setY(xyi.getY() + squareStepSize);
						xyi.setIndex(xyi.getIndex() - 1);
						return xyi;
					}
				),	
				c
			);
			//c.weightx = 0;
    		c.gridx = 1;
    		c.gridy = 1;
			this.add(boardView, c);
			
			//c.weightx = 0.5;
    		c.gridx = 2;
    		c.gridy = 1;
    		this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth() / 8,
					(int) (boardView.getPreferredSize().getHeight()),
					Color.ORANGE,
					SidePanel.CharType.NUMS,
					xyi -> {
						xyi.setY(xyi.getY() + squareStepSize);
						xyi.setIndex(xyi.getIndex() - 1);
						return xyi;
					}
				),	
				c
			);
			//c.weightx = 1;
    		c.gridx = 1;
    		c.gridy = 2;
    		
			this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth(),
					(int) (boardView.getPreferredSize().getHeight() / 8),
					Color.ORANGE,
					SidePanel.CharType.LETTERS,
					xyi -> {
						xyi.setX(xyi.getX() + squareStepSize);
						xyi.setIndex(xyi.getIndex() + 1);
						return xyi;
					}
				),
				c
			);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(color);	
			g2d.fillRect(
				0,
				0,
				(int) getPreferredSize().getWidth(),
				(int) getPreferredSize().getHeight()
			);
			g2d.dispose();
		}
		private static class SidePanel extends JPanel {
			private Color color;
			private CharType charType;
			private UnaryOperator<XYIndexData> function;
			SidePanel(int width, int height, Color color, CharType charType,
					UnaryOperator<XYIndexData> function) {
				this.setPreferredSize(new Dimension(width, height));
				this.color = color;
				this.charType = charType;
				this.function = function;
			}
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(this.color);
				g2d.fillRect(
					0,
					0,
					(int) getPreferredSize().getWidth(),
					(int) getPreferredSize().getHeight()
				);
				g2d.setFont(new Font(Font.SERIF, Font.BOLD, 28).deriveFont(new AffineTransform()));
				g2d.setPaint(Color.BLACK);
				//int x = this.charType == CharType.NUMS ? (int) (this.getPreferredSize().getWidth() / 2) : (int) (this.getPreferredSize().getWidth() / 16);
				//int y = this.charType == CharType.NUMS ? (int) (this.getPreferredSize().getHeight() / 16) : (int) (this.getPreferredSize().getHeight() / 2);
				int x = 25;
				int y = 39;
				int index = this.charType == CharType.NUMS ? 7 : 0;
				Stream.iterate(new XYIndexData(x, y, index), function)
				      .limit(8)
				      .forEach(
				          xyi -> {
				              g2d.drawString(
						          this.charType.getChars()[xyi.getIndex()],
				                  xyi.getX(),
				                  xyi.getY()
				              );
				});
				g2d.dispose();
			}
			private static class XYIndexData {
				private int x;
				private int y;
				private int index;
				XYIndexData(int x, int y, int index) {
					this.x = x;
					this.y = y;
					this.index = index;
				}
				void setX(int x) {
					this.x = x;
				}
				int getX() {
					return this.x;
				}
				void setY(int y) {
					this.y = y;
				}
				int getY() {
					return this.y;
				}
				void setIndex(int index) {
					this.index = index;
				}
				int getIndex() {
					return this.index;
				}
			}
			private static enum CharType {
				NUMS(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}),
				LETTERS(new String[] {"a", "b", "c", "d", "e", "f", "g", "h"});
				private final String[] chars;
				CharType(String[] chars) {
					this.chars = chars;
				}
				String[] getChars() {
					return this.chars;
				}
			}
		}
	}
}