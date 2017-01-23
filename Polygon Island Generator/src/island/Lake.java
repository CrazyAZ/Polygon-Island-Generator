package island;

import java.util.HashSet;
import java.util.Iterator;

import generators.TerrainGenerator;
import structures.Tile;
import structures.Vertex;

public class Lake {

	public Tile firstTile;
	public HashSet<Tile> lakeTiles;
	public HashSet<Vertex> lakeVertices;
	private TerrainGenerator tg;

	public Lake(Vertex v, TerrainGenerator tg) {

		this.tg = tg;
		lakeTiles = new HashSet<>();

		for (Tile t : v.tiles) {
			if (firstTile == null || t.elevation < firstTile.elevation)
				firstTile = t;
		}
		if (firstTile.biome == Biome.OCEAN || firstTile.biome == Biome.LAKE) {
			firstTile = null;
		} else {
			generateLake();
		}

	}

	private void generateLake() {
		tg.lakes.add(this);
		lakeVertices = new HashSet<>();
		firstTile.biome = Biome.LAKE;
		lakeTiles.add(firstTile);
		for (Vertex v : firstTile.vertices)
			lakeVertices.add(v);
		HashSet<Tile> surroundingTiles = new HashSet<>();
		// surroundingTiles.addAll(firstTile.neighbors);
		for (Tile t : firstTile.neighbors)
			if (t.biome != Biome.LAKE)
				surroundingTiles.add(t);

		lakeGeneration: while (true) {

			Tile nextTile = null;
			for (Tile t : surroundingTiles) {
				if (t.biome == Biome.LAKE) {
					Iterator<Lake> lakeIter = tg.lakes.iterator();
					while (lakeIter.hasNext()) {
						Lake l = lakeIter.next();
						if (l.lakeTiles.contains(t)) {
							lakeTiles.addAll(l.lakeTiles);
							lakeVertices.addAll(l.lakeVertices);
							surroundingTiles = new HashSet<>();
							for (Tile nt : lakeTiles)
								for (Tile n : nt.neighbors)
									if (!lakeTiles.contains(n))
										surroundingTiles.add(n);
							lakeIter.remove();
							break;
						}
					}
					nextTile = null;
					break;
				} else if (nextTile == null || t.elevation < nextTile.elevation)
					nextTile = t;
			}

			if (nextTile != null) {

				for (Vertex v : lakeVertices)
					if (v.steepestBorder != null && !lakeVertices.contains(v.steepestBorder.v2)) {
						tg.rivers.add(new River(v, tg));
						break lakeGeneration;
					}

				if (nextTile.biome == Biome.OCEAN)
					break lakeGeneration;

				lakeTiles.add(nextTile);
				nextTile.biome = Biome.LAKE;
				for (Vertex v : nextTile.vertices)
					lakeVertices.add(v);
				surroundingTiles.remove(nextTile);
				for (Tile t : nextTile.neighbors)
					if (!lakeTiles.contains(t))
						surroundingTiles.add(t);
			}

		}

	}

}
