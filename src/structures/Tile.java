package structures;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.HashSet;

import island.Biome;

public class Tile {

	public double elevation;
	public Biome biome;
	public Vertex[] vertices;
	public Border[] borders;
	public HashSet<Tile> neighbors;

	public Tile(Polygon poly, HashMap<Point, Vertex> pointsAndVertices) {

		vertices = new Vertex[poly.points.length];
		for (int i = 0; i < poly.points.length; i++) {
			Vertex newVertex = pointsAndVertices.get(poly.points[i]);
			vertices[i] = newVertex;
			newVertex.tiles.add(this);
		}

		neighbors = new HashSet<>();
		borders = new Border[poly.edges.length];

	}

	public void draw(Graphics g) {
		switch (biome) {
		case OCEAN:
			g.setColor(new Color(0, 60, (int) (150 * Math.pow(2, elevation))));
			break;
		case LAND:
			g.setColor(new Color(0, (int) (255 * elevation), 0));
			break;
		case SNOW:
			g.setColor(new Color(255, 255, 255));
			break;
		case BEACH:
			g.setColor(new Color(238, 214, 175));
			break;
		case LAKE:
			g.setColor(new Color(0, 0, 225));
			break;

		}
		/*
		 * if (biome == Biome.OCEAN)
		 * g.setColor(new Color(0, 60, (int) (150 * Math.pow(2, elevation))));
		 * else if (biome == Biome.LAND)
		 * g.setColor(new Color(0, (int) (255 * elevation), 0));
		 * else if (biome == Biome.BEACH)
		 * g.setColor(new Color(238, 214, 175));
		 */
		g.fillPolygon(getJavaPolygon());
	}

	public java.awt.Polygon getJavaPolygon() {
		int[] xpoints = new int[vertices.length];
		int[] ypoints = new int[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			xpoints[i] = (int) Math.round(vertices[i].x);
			ypoints[i] = (int) Math.round(vertices[i].y);
		}

		return new java.awt.Polygon(xpoints, ypoints, vertices.length);
	}

}
