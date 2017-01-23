package generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import structures.Edge;
import structures.Point;
import structures.Polygon;
import structures.Triangle;

public class Voronoi {

	private Delaunay delaunay;
	private PolygonGenerator pg;
	private HashSet<Triangle> dTriangles;
	public HashSet<Point> polygonNodes, polygonCenters;
	public HashSet<Edge> polygonEdges;
	public HashSet<Polygon> voronoiPolygons;
	private HashMap<Point, Polygon> centersOfPolygons;

	public Voronoi(Delaunay delaunay, PolygonGenerator pg) {
		System.out.println("Calculating Polygons");
		this.delaunay = delaunay;
		this.pg = pg;
		dTriangles = (HashSet<Triangle>) delaunay.delaunayTriangles.clone();
		polygonNodes = new HashSet<Point>();
		polygonCenters = new HashSet<>();
		polygonEdges = new HashSet<Edge>();
		voronoiPolygons = new HashSet<Polygon>();
		centersOfPolygons = new HashMap<>();

		Iterator<Triangle> dTIter = dTriangles.iterator();

		while (dTIter.hasNext()) {
			Triangle tri = dTIter.next();
			if (pg.lloyds == 0)
				for (Triangle otherTri : dTriangles) {
					if (tri.sharesEdge(otherTri)) {
						polygonNodes.add(tri.circumcenter);
						polygonNodes.add(otherTri.circumcenter);
						polygonEdges.add(new Edge(tri.circumcenter, otherTri.circumcenter));
					}
				}

			dTIter.remove();

			for (Point p : tri.points)
				polygonCenters.add(p);

		}

		determinePolygons();

	}

	private void determinePolygons() {
		for (Point p : polygonCenters) {
			dTriangles = delaunay.delaunayTriangles;
			ArrayList<Point> polygonPoints = new ArrayList<>();
			HashSet<Point> neighborCenters = new HashSet<>();
			for (Triangle tri : dTriangles) {
				if (tri.hasPoint(p)) {
					polygonPoints.add(tri.circumcenter);
					if (pg.lloyds == 0)
						for (Point triP : tri.points)
							neighborCenters.add(triP);
				}
			}
			Polygon newPoly = new Polygon(polygonPoints, p);
			voronoiPolygons.add(newPoly);
			if (pg.lloyds == 0) {
				centersOfPolygons.put(p, newPoly);
				neighborCenters.remove(p);
				newPoly.neighborCenters = neighborCenters;
			}
		}

		if (pg.lloyds == 0)
			for (Polygon poly : voronoiPolygons) {
				poly.setNeighbors(centersOfPolygons);
			}

	}

}
