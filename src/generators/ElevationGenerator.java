package generators;

import java.util.HashSet;
import java.util.Random;

import perlin.PerlinNoise;
import structures.Tile;
import structures.Vertex;

public class ElevationGenerator {

	public int x, y, width, height;
	private PerlinNoise perlin;
	private int MAJOR_CELL_SIZE;

	public ElevationGenerator(HashSet<Tile> tiles, int x, int y, int w, int h, Random rand) {
		System.out.println("Generating Elevation");
		this.y = y;
		this.x = x;
		width = w;
		height = h;
		MAJOR_CELL_SIZE = 128;
		perlin = new PerlinNoise(width / MAJOR_CELL_SIZE, height / MAJOR_CELL_SIZE, rand);

		HashSet<Vertex> vertices = new HashSet<>();
		for (Tile t : tiles)
			for (Vertex v : t.vertices)
				vertices.add(v);

		for (Vertex v : vertices)
			setPointElevation(v);

		for (Tile t : tiles)
			setPolygonElevation(t);

	}

	private void setPointElevation(Vertex v) {
		double d = Math
				.sqrt((v.x - perlin.getWidth() * MAJOR_CELL_SIZE / 2) * (v.x - perlin.getWidth() * MAJOR_CELL_SIZE / 2)
						+ (v.y - perlin.getHeight() * MAJOR_CELL_SIZE / 2)
								* (v.y - perlin.getHeight() * MAJOR_CELL_SIZE / 2))
				/ (perlin.getHeight() * MAJOR_CELL_SIZE / 2);
		double elevation = perlin.octavePerlin(v.x / MAJOR_CELL_SIZE, v.y / MAJOR_CELL_SIZE, 6);
		elevation = 0.5 * elevation + 0.5;
		elevation = (elevation + 0.05) * (1 - .7 * Math.pow(d, 2));
		v.elevation = elevation;
	}

	private void setPolygonElevation(Tile tile) {
		double elevation = 0;
		for (Vertex v : tile.vertices)
			if (v.x > x && v.y > y && v.x < x + width && v.y < y + height)
				elevation += v.elevation;

		tile.elevation = elevation / tile.vertices.length;

	}

}
