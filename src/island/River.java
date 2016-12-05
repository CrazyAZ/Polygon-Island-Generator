package island;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import generators.TerrainGenerator;
import structures.Border;
import structures.Vertex;

public class River {

	public Vertex startVertex;
	public ArrayList<Border> riverBorders;
	private TerrainGenerator tg;

	public River(Vertex v, TerrainGenerator tg) {
		this.tg = tg;
		startVertex = v;
		Vertex currentVertex = startVertex;
		riverBorders = new ArrayList<>();

		while (currentVertex.steepestBorder != null) {
			Border nextBorder = currentVertex.steepestBorder;
			riverBorders.add(nextBorder);
			currentVertex = nextBorder.v2;
		}

		new Lake(currentVertex, tg);
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 225));

		// g.fillOval((int) Math.round(startVertex.x - 2), (int)
		// Math.round(startVertex.y - 2), 4, 4);

		double distance = 0;
		for (int i = 0; i < riverBorders.size(); i++) {
			Border currentBorder = riverBorders.get(i);
			distance += currentBorder.distance;
			Stroke s = new BasicStroke((float) Math.sqrt(distance / 6));
			g2.setStroke(s);
			currentBorder.draw(g2);

		}
	}

}
