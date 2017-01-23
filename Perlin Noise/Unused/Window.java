package perlin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JPanel {

	int gridSize1, gridSize2, gridSize3;
	PerlinNoise pn1, pn2, pn3;

	public Window(PerlinNoise pn1, PerlinNoise pn2, PerlinNoise pn3, int gridSize1, int gridSize2, int gridSize3) {
		this.gridSize1 = gridSize1;
		this.gridSize2 = gridSize2;
		this.gridSize3 = gridSize3;
		this.pn1 = pn1;
		this.pn2 = pn2;
		this.pn3 = pn3;

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setResizable(false);
		setPreferredSize(new Dimension(pn1.getWidth() * gridSize1, pn1.getHeight() * gridSize1));
		frame.add(this);
		frame.pack();
		frame.setVisible(true);

	}

	@Override
	public void paintComponent(Graphics g) {
		BufferedImage image = new BufferedImage(pn1.getWidth() * gridSize1, pn1.getHeight() * gridSize1,
				BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < pn1.getWidth() * gridSize1; x++)
			for (int y = 0; y < pn1.getHeight() * gridSize1; y++) {
				double c = (0.5 * (pn1.octavePerlin(x / (double) gridSize1, y / (double) gridSize1, 6)) + 0.5);
				// double c2 = (.5 * (pn2.perlin(x / (double) gridSize2 + 2, y /
				// (double) gridSize2 + 2)) + 0.5);
				// double c3 = (.5 * (pn3.perlin(x / (double) gridSize3 + 4, y /
				// (double) gridSize3 + 4)) + 0.5);
				// double c = (c1 * gridSize1 + c2 * gridSize2 + c3 * gridSize3)
				// / (gridSize1 + gridSize2 + gridSize3);

				// double d = Math
				// .sqrt((x - pn1.getWidth() * gridSize1 / 2) * (x -
				// pn1.getWidth() * gridSize1 / 2)
				// + (y - pn1.getHeight() * gridSize1 / 2) * (y -
				// pn1.getHeight() * gridSize1 / 2))
				// / (pn1.getHeight() * gridSize1 / 2);
				// c = (c + 0.05) * (1 - .7 * Math.pow(d, 2));

				c *= 255;
				// image.setRGB(x, y, new Color(0, (int) c, 255 - (int)
				// c).getRGB());
				image.setRGB(x, y, new Color((int) c, (int) c, (int) c).getRGB());

				// if (c > 0.35) {
				// image.setRGB(x, y, new Color(0, (int) (255 * Math.pow(c,
				// 1.5)), 0).getRGB());
				// } else {
				// image.setRGB(x, y, new Color(0, 0, 130).getRGB());
				// }
			}

		g.drawImage(image, 0, 0, null);
	}

	public static void main(String[] args) {
		Random rand = new Random();
		PerlinNoise perlin1 = new PerlinNoise(5, 5, rand);
		PerlinNoise perlin2 = new PerlinNoise(8, 8, rand);
		PerlinNoise perlin3 = new PerlinNoise(16, 16, rand);
		new Window(perlin1, perlin2, perlin3, 128, 64, 32);
	}

}
