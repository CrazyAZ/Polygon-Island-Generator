package generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import island.Biome;
import island.Lake;
import island.River;
import structures.Border;
import structures.Tile;
import structures.Vertex;

public class TerrainGenerator {
	private HashSet<Tile> tiles, edgeTiles, oceanTiles, landTiles;
	private HashSet<Border> borders;
	public HashSet<River> rivers;
	public HashSet<Lake> lakes;
	private Random rand;
	private double SEA_LEVEL, SNOW_LINE;

	public TerrainGenerator(HashSet<Tile> tiles, HashSet<Border> borders, Random rand) {
		this.tiles = tiles;
		this.borders = borders;
		this.rand = rand;
		SEA_LEVEL = 0.3;
		SNOW_LINE = 0.6;
		oceanTiles = new HashSet<>();
		landTiles = new HashSet<>();

		edgeTiles = new HashSet<>();
		for (Tile t : tiles)
			if (t.neighbors.size() < t.vertices.length)
				edgeTiles.add(t);

		System.out.println("Filling Ocean");
		setOcean();
		System.out.println("Raising Land");
		setLand();
		System.out.println("Carving Rivers");
		generateRivers();

	}

	private void setOcean() {
		HashSet<Tile> frontier = edgeTiles;

		while (!frontier.isEmpty()) {
			HashSet<Tile> nextFrontier = new HashSet<>();
			for (Tile t : frontier) {
				double elevation = t.elevation;
				if (elevation < SEA_LEVEL) {
					t.biome = Biome.OCEAN;
					for (Vertex v : t.vertices)
						if (v.biome == null)
							v.biome = Biome.OCEAN;
						else if (v.biome == Biome.BEACH)
							v.biome = Biome.COAST;
					oceanTiles.add(t);
					for (Tile neighbor : t.neighbors)
						if (neighbor.biome == null)
							nextFrontier.add(neighbor);
				} else {
					t.biome = Biome.BEACH;
					for (Vertex v : t.vertices)
						if (v.biome == null)
							v.biome = Biome.BEACH;
						else if (v.biome == Biome.OCEAN)
							v.biome = Biome.COAST;
					landTiles.add(t);
				}

			}
			frontier = nextFrontier;
		}

	}

	private void setLand() {
		for (Tile t : tiles)
			if (t.biome == null) {
				if (t.elevation > SNOW_LINE)
					t.biome = Biome.SNOW;
				else
					t.biome = Biome.LAND;
				for (Vertex v : t.vertices)
					// if (v.biome == null)
					v.biome = t.biome;
				landTiles.add(t);
			}
	}

	private void generateRivers() {

		ArrayList<Vertex> landVertices = new ArrayList<>();
		ArrayList<Vertex> highVertices = new ArrayList<>();
		double snowArea = 0;
		for (Tile t : landTiles) {
			for (Vertex v : t.vertices) {
				if (v.biome != Biome.COAST) {
					landVertices.add(v);
					if (v.biome == Biome.SNOW)
						highVertices.add(v);

				}
			}

			if (t.biome == Biome.SNOW)
				snowArea += t.area;
		}

		for (Vertex v : landVertices) {
			for (Border b : borders) {
				if (v == b.v1 && (v.steepestBorder == null || b.slope > v.steepestBorder.slope)) {
					v.steepestBorder = b;
				}
			}
		}

		rivers = new HashSet<>();
		lakes = new HashSet<>();
		for (int i = 0; i < snowArea / 1700; i++) {
			Vertex startVertex = highVertices.get(rand.nextInt(highVertices.size()));
			if (startVertex.steepestBorder != null)
				rivers.add(new River(startVertex, this));
			else {
				landVertices.remove(startVertex);
				i--;
			}

			if (landVertices.isEmpty())
				break;

		}
	}

}
