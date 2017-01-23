package generators;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Random;

import structures.Edge;
import structures.Point;
import structures.Polygon;
import structures.Triangle;

public class PolygonGenerator {

	public int x, y, width, height, points, lloyds;
	public HashSet<Point> pointSet;
	private Random rand;
	public Delaunay dTriangles;
	public Voronoi voronoi;

	public PolygonGenerator(int x, int y, int w, int h, int p, int l, Random rand) {
		this.y = y;
		this.x = x;
		width = w;
		height = h;
		points = p;
		this.lloyds = l;
		this.rand = rand;

		System.out.println("Generating Points");
		generatePoints();

		dTriangles = new Delaunay(this);
		voronoi = new Voronoi(dTriangles, this);

		while (lloyds > 0)
			lloydRelaxation();

		System.out.println(voronoi.voronoiPolygons.size());

	}

	private void generatePoints() {
		pointSet = new HashSet<Point>();
		for (int i = 0; i < points; i++) {
			pointSet.add(new Point(rand.nextDouble() * width + x, rand.nextDouble() * height + y));
		}
	}

	public void lloydRelaxation() {
		System.out.println("Relaxing");
		lloyds--;
		pointSet = new HashSet<Point>();
		for (Polygon p : voronoi.voronoiPolygons)
			pointSet.add(p.trueCenter());
		dTriangles = new Delaunay(this);
		voronoi = new Voronoi(dTriangles, this);
	}

	public void draw(Graphics g) {

		// for (Triangle tri : dTriangles.delaunayTriangles) {
		// g.setColor(Color.black);
		// drawTriangle(tri, g);
		// g.setColor(Color.blue);
		// g.drawOval((int) Math.round(tri.circumcenter.x - tri.radius),
		// (int) Math.round(tri.circumcenter.y - tri.radius), (int) Math.round(2
		// * tri.radius),
		// (int) Math.round(2 * tri.radius));
		// }

		g.setColor(Color.RED);
		for (Point p : pointSet)
			drawPoint(p, g);

		g.setColor(Color.BLACK);
		for (Edge e : voronoi.polygonEdges)
			drawEdge(e, g);
	}

	private void drawEdge(Edge e, Graphics g) {
		g.drawLine((int) Math.round(e.p1.x), (int) Math.round(e.p1.y), (int) Math.round(e.p2.x),
				(int) Math.round(e.p2.y));
	}

	private void drawPoint(Point p, Graphics g) {
		g.fillOval((int) Math.round(p.x - 2), (int) Math.round(p.y - 2), 4, 4);
	}

	private void drawTriangle(Triangle tri, Graphics g) {
		g.drawLine((int) Math.round(tri.p1.x), (int) Math.round(tri.p1.y), (int) Math.round(tri.p2.x),
				(int) Math.round(tri.p2.y));
		g.drawLine((int) Math.round(tri.p1.x), (int) Math.round(tri.p1.y), (int) Math.round(tri.p3.x),
				(int) Math.round(tri.p3.y));
		g.drawLine((int) Math.round(tri.p3.x), (int) Math.round(tri.p3.y), (int) Math.round(tri.p2.x),
				(int) Math.round(tri.p2.y));
	}

}
