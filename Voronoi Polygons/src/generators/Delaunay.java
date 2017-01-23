package generators;

import java.util.HashSet;

import structures.Edge;
import structures.Point;
import structures.Triangle;

public class Delaunay {

	public HashSet<Triangle> delaunayTriangles;
	private Point[] initialPoints;

	public Delaunay(PolygonGenerator pg) {
		System.out.println("Calculating Triangles");
		delaunayTriangles = new HashSet<Triangle>();

		addSuperTrianlges(pg);

		for (Point p : pg.pointSet) {

			if (p.x > pg.x && p.y > pg.y && p.x < pg.x + pg.width && p.y < pg.y + pg.height) {

				HashSet<Triangle> badTriangles = new HashSet<Triangle>();

				for (Triangle tri : delaunayTriangles) {
					if (tri.inCircumcircle(p)) {
						badTriangles.add(tri);
					}
				}

				HashSet<Edge> holeEdges = new HashSet<Edge>();
				for (Triangle tri : badTriangles) {
					Edge e1 = new Edge(tri.p1, tri.p2);
					Edge e2 = new Edge(tri.p2, tri.p3);
					Edge e3 = new Edge(tri.p3, tri.p1);
					if (!holeEdges.add(e1))
						holeEdges.remove(e1);
					if (!holeEdges.add(e2))
						holeEdges.remove(e2);
					if (!holeEdges.add(e3))
						holeEdges.remove(e3);
					delaunayTriangles.remove(tri);
				}

				for (Edge e : holeEdges) {
					// System.out.println(delaunayTriangles.size());
					delaunayTriangles.add(new Triangle(e.p1, e.p2, p));
				}
			}
		}

		// removeSuperTriangles();

	}

	private void addSuperTrianlges(PolygonGenerator pg) {
		initialPoints = new Point[4];
		Point a = new Point(pg.x, pg.y);
		Point b = new Point(pg.x, pg.y + pg.height);
		Point c = new Point(pg.x + pg.width, pg.y + pg.height);
		Point d = new Point(pg.x + pg.width, pg.y);
		delaunayTriangles.add(new Triangle(a, b, c));
		delaunayTriangles.add(new Triangle(a, c, d));
		initialPoints[0] = a;
		initialPoints[1] = b;
		initialPoints[2] = c;
		initialPoints[3] = d;
	}

	private void removeSuperTriangles() {
		HashSet<Triangle> toRemove = new HashSet<>();
		for (Triangle tri : delaunayTriangles) {
			// tri.print();
			for (Point p : initialPoints) {
				if (tri.hasPoint(p))
					toRemove.add(tri);
			}
		}
		// System.out.println(toRemove.size());
		for (Triangle tri : toRemove)
			delaunayTriangles.remove(tri);
	}

}
