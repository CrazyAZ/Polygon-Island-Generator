package structures;

import java.awt.Graphics2D;
import java.util.HashMap;

public class Border {

	public Vertex v1, v2;
	public double slope, distance;

	public Border(Edge e, HashMap<Point, Vertex> pointsAndVertices) {
		if (pointsAndVertices.get(e.p1).elevation > pointsAndVertices.get(e.p2).elevation) {
			v1 = pointsAndVertices.get(e.p1);
			v2 = pointsAndVertices.get(e.p2);
		} else {
			v1 = pointsAndVertices.get(e.p2);
			v2 = pointsAndVertices.get(e.p1);
		}
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		distance = Math.sqrt(dx * dx + dy * dy);
		slope = (v1.elevation - v2.elevation) / distance;

	}

	public void draw(Graphics2D g) {
		g.drawLine((int) Math.round(v1.x), (int) Math.round(v1.y), (int) Math.round(v2.x), (int) Math.round(v2.y));
	}

}
