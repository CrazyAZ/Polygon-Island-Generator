package structures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import generators.TerrainGenerator;
import island.Biome;
import perlin.PerlinNoise;

public class Tile {

	public double elevation, area;
	public Biome biome;
	public Vertex[] vertices;
	public Border[] borders;
	public HashSet<Tile> neighbors;
	private BufferedImage texture;

	public Tile(Polygon poly, HashMap<Point, Vertex> pointsAndVertices) {

		vertices = new Vertex[poly.points.length];
		for (int i = 0; i < poly.points.length; i++) {
			Vertex newVertex = pointsAndVertices.get(poly.points[i]);
			vertices[i] = newVertex;
			newVertex.tiles.add(this);
		}

		neighbors = new HashSet<>();
		borders = new Border[poly.edges.length];
		area = poly.getArea();

	}

	public void draw(Graphics g, PerlinNoise perlin) {

		Graphics2D g2d = (Graphics2D) g;

		// g2d.setPaint(null);

		switch (biome) {
		case OCEAN:
			if (area < 10000)
				g2d.setPaint(new TexturePaint(getTextureImage(perlin), getJavaPolygon().getBounds2D()));
			else
				g.setColor(new Color(0, 60, (int) (180 - 200 * (TerrainGenerator.SEA_LEVEL - elevation))));
			break;
		case LAND:
			// g.setColor(new Color(0, (int) (255 * elevation), 0));
			g2d.setPaint(new TexturePaint(getTextureImage(perlin), getJavaPolygon().getBounds2D()));
			break;
		case SNOW:
			// float c = (float) Math.min(elevation + 0.3, 1);
			// g.setColor(new Color(c, c, c));
			g2d.setPaint(new TexturePaint(getTextureImage(perlin), getJavaPolygon().getBounds2D()));
			break;
		case BEACH:
			// g.setColor(new Color(238, 214, 175));
			g2d.setPaint(new TexturePaint(getTextureImage(perlin), getJavaPolygon().getBounds2D()));
			break;
		case LAKE:
			g.setColor(new Color(0, 0, 225));
			break;

		}
		/*
		 * if (biome == Biome.OCEAN) g.setColor(new Color(0, 60, (int) (150 *
		 * Math.pow(2, elevation)))); else if (biome == Biome.LAND)
		 * g.setColor(new Color(0, (int) (255 * elevation), 0)); else if (biome
		 * == Biome.BEACH) g.setColor(new Color(238, 214, 175));
		 */

		g2d.fill(getJavaPolygon());

		// g.fillPolygon(getJavaPolygon());

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

	private BufferedImage getTextureImage(PerlinNoise perlin) {

		if (texture != null)
			return texture;

		Rectangle bound = (Rectangle) getJavaPolygon().getBounds2D();

		texture = new BufferedImage(bound.width, bound.height, BufferedImage.TYPE_INT_ARGB);

		switch (biome) {

		case OCEAN:
			for (int x = 0; x < texture.getWidth(); x++)
				for (int y = 0; y < texture.getHeight(); y++) {
					texture.setRGB(x, y, new Color(0, 60, (int) ((180 - 200 * (TerrainGenerator.SEA_LEVEL - elevation))
							* (1 + 0.1 * perlin.octavePerlin(bound.getX() + x / 32.0, bound.getY() + y / 32.0, 3))))
									.getRGB());
				}
			break;

		case LAND:
			for (int x = 0; x < texture.getWidth(); x++)
				for (int y = 0; y < texture.getHeight(); y++) {
					texture.setRGB(x, y, new Color(0, (int) (255 * elevation
							* (1 + 0.07 * perlin.octavePerlin(bound.getX() + x / 4.0, bound.getY() + y / 4.0, 3))), 0)
									.getRGB());
				}
			break;

		case BEACH:
			for (int x = 0; x < texture.getWidth(); x++)
				for (int y = 0; y < texture.getHeight(); y++) {
					texture.setRGB(x, y, new Color((int) (238
							* (1 + 0.05 * perlin.octavePerlin(bound.getX() + x / 2.0, bound.getY() + y / 2.0, 3))),
							(int) (214 * (1
									+ 0.05 * perlin.octavePerlin(bound.getX() + x / 2.0, bound.getY() + y / 2.0, 3))),
							(int) (175 * (1
									+ 0.05 * perlin.octavePerlin(bound.getX() + x / 2.0, bound.getY() + y / 2.0, 3))))
											.getRGB());
				}
			break;

		case SNOW:
			for (int x = 0; x < texture.getWidth(); x++)
				for (int y = 0; y < texture.getHeight(); y++) {
					float c = (float) Math.min((elevation + 0.3)
							* (1 + 0.03 * perlin.octavePerlin(bound.getX() + x / 4.0, bound.getY() + y / 4.0, 3)), 1);
					texture.setRGB(x, y, new Color(c, c, c).getRGB());
				}
			break;
		}

		return texture;
	}

}
