package structures;

import java.awt.Graphics;
import java.util.HashSet;

import island.Biome;

public class Vertex {

	public double x, y, elevation;
	public Border steepestBorder;
	public HashSet<Tile> tiles;
	public Biome biome;

	public Vertex(Point p) {
		x = p.x;
		y = p.y;
		tiles = new HashSet<>();
	}

	public void draw(Graphics g) {

		g.fillOval((int) Math.round(x - 2), (int) Math.round(y - 2), 4, 4);

	}

}
