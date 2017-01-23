package structures;

public class Triangle {

	public Point p1, p2, p3, circumcenter;
	public double radius;
	public Point[] points;

	public Triangle(Point tp1, Point tp2, Point tp3) {
		p1 = tp1;
		p2 = tp2;
		p3 = tp3;
		points = new Point[] { p1, p2, p3 };
		setCircumcenter();
		setRadius();
	}

	private void setCircumcenter() {
		double a = p2.x - p1.x;
		double b = p2.y - p1.y;
		double c = p3.x - p1.x;
		double d = p3.y - p1.y;
		double e = a * (p1.x + p2.x) + b * (p1.y + p2.y);
		double f = c * (p1.x + p3.x) + d * (p1.y + p3.y);
		double g = 2.0 * (a * (p3.y - p2.y) - b * (p3.x - p2.x));
		if (g == 0) {
			circumcenter = null;
		} else {
			double px = (d * e - b * f) / g;
			double py = (a * f - c * e) / g;
			circumcenter = new Point(px, py);
		}
	}

	private void setRadius() {
		radius = Math.sqrt(
				(p1.x - circumcenter.x) * (p1.x - circumcenter.x) + (p1.y - circumcenter.y) * (p1.y - circumcenter.y));
	}

	public boolean inCircumcircle(Point p) {
		return radius > Math.sqrt(
				(p.x - circumcenter.x) * (p.x - circumcenter.x) + (p.y - circumcenter.y) * (p.y - circumcenter.y));
	}

	public boolean hasPoint(Point p) {
		for (Point myP : points)
			if ((myP.x == p.x) && (myP.y == p.y))
				return true;
		return false;
	}

	public boolean sharesEdge(Triangle t) {

		int pmatch = 0;

		for (Point tP : t.points)
			if (hasPoint(tP))
				pmatch++;

		return pmatch == 2;
	}

	@Override
	public int hashCode() {
		return p1.hashCode() * p2.hashCode() * p3.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}

	public void print() {
		System.out.println((int) p1.x + "," + (int) p1.y + ";" + (int) p2.x + "," + (int) p2.y + ";" + (int) p3.x + ","
				+ (int) p3.y);
	}

}
