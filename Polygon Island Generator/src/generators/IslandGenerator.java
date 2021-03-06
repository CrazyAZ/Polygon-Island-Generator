package generators;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import island.River;
import perlin.PerlinNoise;
import structures.Border;
import structures.Edge;
import structures.Point;
import structures.Polygon;
import structures.Tile;
import structures.Vertex;

public class IslandGenerator {

	public int x, y, width, height, TEXTURE_FREQUENCY;
	private long seed;
	private PolygonGenerator polygonGenerator;
	private ElevationGenerator elevationGenerator;
	private TerrainGenerator terrainGenerator;
	private HashSet<Tile> tiles;
	private HashSet<Border> borders;
	private HashSet<Vertex> vertices;
	private HashMap<Point, Vertex> pointsAndVertices;
	private PerlinNoise perlin;
	// private HashMap<Edge, Border> edgesAndBorders;
	// private HashMap<Polygon, Tile> polygonsAndTiles;

	public IslandGenerator(int x, int y, int w, int h, long seed) {
		this.y = y;
		this.x = x;
		width = w;
		height = h;
		this.seed = seed;
		polygonGenerator = new PolygonGenerator(x, y, w, h, 4000, 2, new Random(seed));
		convertPolygons();
		elevationGenerator = new ElevationGenerator(tiles, x, y, w, h, this, new Random(seed));
		convertEdges();
		terrainGenerator = new TerrainGenerator(tiles, borders, new Random(seed));

		TEXTURE_FREQUENCY = 4;
		perlin = new PerlinNoise(width / TEXTURE_FREQUENCY, height / TEXTURE_FREQUENCY, new Random(seed));
	}

	private void convertPolygons() {
		System.out.println("Converting Polygons");
		HashSet<Polygon> polygons = polygonGenerator.voronoi.voronoiPolygons;
		HashSet<Point> points = new HashSet<>();
		for (Polygon poly : polygons)
			for (Point p : poly.points)
				points.add(p);

		vertices = new HashSet<>();
		pointsAndVertices = new HashMap<>();
		for (Point p : points) {
			Vertex newVertex = new Vertex(p);
			vertices.add(newVertex);
			pointsAndVertices.put(p, newVertex);
		}

		tiles = new HashSet<>();
		HashMap<Polygon, Tile> polygonsAndTiles = new HashMap<>();
		for (Polygon poly : polygons) {
			poly.getEdges(polygonGenerator.voronoi.polygonEdges);
			Tile newTile = new Tile(poly, pointsAndVertices);
			tiles.add(newTile);
			polygonsAndTiles.put(poly, newTile);
		}

		for (Polygon poly : polygons) {
			Tile tile = polygonsAndTiles.get(poly);
			for (Polygon n : poly.neighbors)
				tile.neighbors.add(polygonsAndTiles.get(n));
		}

	}

	private void convertEdges() {
		borders = new HashSet<>();
		HashMap<Edge, Border> edgesAndBorders = new HashMap<>();
		for (Edge e : polygonGenerator.voronoi.polygonEdges) {
			Border newBorder = new Border(e, pointsAndVertices);
			borders.add(newBorder);
			edgesAndBorders.put(e, newBorder);
		}
	}

	public void draw(Graphics g) {

		// g.setColor(Color.RED);
		// for (Point p : polygonGenerator.pointList)
		// drawPoint(p, g);
		//
		// g.setColor(Color.BLACK);
		// for (Edge e : polygonGenerator.voronoi.polygonEdges)
		// drawEdge(e, g);

		for (Tile t : tiles) {
			t.draw(g, perlin);
		}

		// for (Lake l : terrainGenerator.lakes) {
		// g.setColor(new Color((float) Math.random(), (float) Math.random(),
		// (float) Math.random()));
		// for (Tile t : l.lakeTiles)
		// g.fillPolygon(t.getJavaPolygon());
		// }

		for (River r : terrainGenerator.rivers) {
			r.draw(g);
		}

		// for (Vertex v : vertices) {
		// v.draw(g);
		// }

	}

	private void drawEdge(Edge e, Graphics g) {
		g.drawLine((int) Math.round(e.p1.x), (int) Math.round(e.p1.y), (int) Math.round(e.p2.x),
				(int) Math.round(e.p2.y));
	}

	private void drawPoint(Point p, Graphics g) {
		g.fillOval((int) Math.round(p.x - 2), (int) Math.round(p.y - 2), 4, 4);
	}

}
