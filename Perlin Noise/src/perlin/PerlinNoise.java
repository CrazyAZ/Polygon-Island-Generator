package perlin;

import java.util.Random;

public class PerlinNoise {

	private double[][] gradient;

	public PerlinNoise(int width, int height, Random rand) {
		initGrid(width, height, rand);
	}

	private void initGrid(int width, int height, Random rand) {
		gradient = new double[width][height];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				gradient[x][y] = rand.nextFloat() * 2 * Math.PI;
	}

	public double octavePerlin(double x, double y, int octaves) {
		double total = 0;
		double frequency = 1;
		double amplitude = 1;
		double maxValue = 0; // Used for normalizing result to 0.0 - 1.0
		for (int i = 0; i < octaves; i++) {
			total += perlin(x * frequency, y * frequency) * amplitude;

			maxValue += amplitude;

			amplitude *= 1 / 2;
			frequency *= 2;
		}

		return total / maxValue;
	}

	public double perlin(double x, double y) {
		while (x < 0)
			x += getWidth();
		while (y < 0)
			y += getHeight();
		int x0 = (int) x;
		int x1 = x0 + 1;
		int y0 = (int) y;
		int y1 = y0 + 1;

		double n0, n1, n2, n3;
		n0 = dotGridGradient(x, y, x0, y0);
		n1 = dotGridGradient(x, y, x1, y0);
		n2 = dotGridGradient(x, y, x0, y1);
		n3 = dotGridGradient(x, y, x1, y1);

		return interpolate(n0, n1, n2, n3, x - x0, y - y0);

	}

	private double interpolate(double n0, double n1, double n2, double n3, double dx, double dy) {
		double smootherp1 = smootherp(n0, n1, dx);
		double smootherp2 = smootherp(n2, n3, dx);
		return lerp(smootherp1, smootherp2, dy);
	}

	private double lerp(double a0, double a1, double w) {
		return (1 - w) * a0 + w * a1;
	}

	private double smootherp(double a0, double a1, double x) {
		return a0 + (a1 - a0) * (x * x * (3 - 2 * x));
	}

	private double dotGridGradient(double x, double y, int ix, int iy) {
		double angle = gradient[ix % getWidth()][iy % getHeight()];
		double dx = x - ix;
		double dy = y - iy;
		// System.out.println(dx * Math.cos(angle) + dy * Math.sin(angle));
		return dx * Math.cos(angle) + dy * Math.sin(angle);

	}

	public int getWidth() {
		return gradient.length;
	}

	public int getHeight() {
		return gradient[0].length;
	}

}
