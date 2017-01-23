package structures;

import java.util.ArrayList;
import java.util.HashSet;

public class Point {

	public double x, y;
	public Polygon[] polygons;

	public Point(double px, double py) {
		x = px;
		y = py;
	}

	public Polygon[] getPolygons(HashSet<Polygon> polygonSet) {
		if (polygons != null)
			return polygons;

		ArrayList<Polygon> polygonList = new ArrayList<>();
		for (Polygon poly : polygonSet)
			if (poly.hasPoint(this))
				polygonList.add(poly);

		polygons = new Polygon[polygonList.size()];
		for (int i = 0; i < polygons.length; i++)
			polygons[i] = polygonList.get(i);

		return polygons;
	}

}
