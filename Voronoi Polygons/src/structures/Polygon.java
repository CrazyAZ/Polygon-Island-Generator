package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Polygon {

	public Point[] points;
	public Edge[] edges;
	public Point center;
	private Point trueCenter;
	private double area;
	public HashSet<Point> neighborCenters;
	public HashSet<Polygon> neighbors;

	public Polygon(ArrayList<Point> polygonPoints, Point centerPoint) {
		points = new Point[polygonPoints.size()];

		for (int i = 0; i < points.length; i++) {
			double angle = Math.atan2(polygonPoints.get(0).y - centerPoint.y, polygonPoints.get(0).x - centerPoint.x);
			int nextPoint = 0;
			for (int j = 1; j < polygonPoints.size(); j++) {
				double jangle = Math.atan2(polygonPoints.get(j).y - centerPoint.y,
						polygonPoints.get(j).x - centerPoint.x);
				if (jangle < angle) {
					angle = jangle;
					nextPoint = j;
				}

			}
			points[i] = polygonPoints.get(nextPoint);
			polygonPoints.remove(nextPoint);
		}

		area = -1;
		center = centerPoint;

	}

	public Point trueCenter() {
		if (trueCenter != null)
			return trueCenter;

		double x = 0;
		double y = 0;
		for (Point p : points) {
			// if (p.x < 0) {
			// x += 0;
			// } else if (p.x > 1280) {
			// x += 1280;
			// } else {
			// x += p.x;
			// }
			//
			// if (p.y < 0) {
			// y += 0;
			// } else if (p.y > 704) {
			// y += 704;
			// } else {
			// y += p.y;
			// }

			x += p.x;
			y += p.y;

		}

		return new Point(x / points.length, y / points.length);
	}

	public java.awt.Polygon getJavaPolygon() {
		int[] xpoints = new int[points.length];
		int[] ypoints = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			xpoints[i] = (int) Math.round(points[i].x);
			ypoints[i] = (int) Math.round(points[i].y);
		}

		return new java.awt.Polygon(xpoints, ypoints, points.length);
	}

	public boolean hasPoint(Point p) {
		for (Point myP : points)
			if ((myP.x == p.x) && (myP.y == p.y))
				return true;
		return false;
	}

	public boolean hasEdge(Edge e) {
		return (hasPoint(e.p1) && hasPoint(e.p2));
	}

	public boolean sharesEdge(Polygon poly) {
		int count = 0;
		for (Point pP : poly.points)
			if (hasPoint(pP))
				count++;

		return count == 2;
	}

	public Edge[] getEdges(HashSet<Edge> edgeSet) {
		if (edges != null)
			return edges;

		ArrayList<Edge> edgeList = new ArrayList<>();
		for (Edge e : edgeSet)
			if (hasEdge(e))
				edgeList.add(e);

		edges = new Edge[edgeList.size()];
		for (int i = 0; i < edges.length; i++)
			edges[i] = edgeList.get(i);

		return edges;
	}

	public void setNeighbors(HashMap<Point, Polygon> centersOfPolygons) {
		neighbors = new HashSet<>();
		for (Point p : neighborCenters)
			neighbors.add(centersOfPolygons.get(p));

		Iterator<Polygon> neighborIter = neighbors.iterator();
		while (neighborIter.hasNext()) {
			Polygon poly = neighborIter.next();
			if (!this.sharesEdge(poly))
				neighborIter.remove();
		}

	}

	public double getArea() {
		if (area != -1)
			return area;

		Point p, np;
		for (int i = 0; i < points.length; i++) {
			p = points[i];
			np = points[i < points.length - 1 ? i + 1 : 0];
			area += p.x * np.y - p.y * np.x;
		}
		area /= 2;
		return area;
	}

}
