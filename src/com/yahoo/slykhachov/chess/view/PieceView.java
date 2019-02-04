package com.yahoo.slykhachov.chess.view;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class PieceView {
	private BufferedImage image;
	private Point2D.Double point;
	protected PieceView(Point2D.Double point, BufferedImage image) {
		this.point = point;
		this.image = image;
	}
	public BufferedImage getPieceImage() {
		return this.image;
	}
	public Point2D.Double getPoint() {
		return this.point;
	}
	public void setPoint(Point2D.Double point) {
		this.point = point;
	}
}
