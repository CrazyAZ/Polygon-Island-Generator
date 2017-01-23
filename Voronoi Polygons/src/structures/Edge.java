package structures;

public class Edge {

	public Point p1, p2;
	public Point[] points;

	public Edge(Point ep1, Point ep2) {
		p1 = ep1;
		p2 = ep2;
		points = new Point[] { p1, p2 };
	}

	@Override
	public int hashCode() {
		return p1.hashCode() * p2.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}

}
