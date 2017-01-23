package control;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import generators.IslandGenerator;

public class Window extends JPanel {

	private IslandGenerator ig;

	public Window(int w, int h, IslandGenerator ig) {
		this.ig = ig;
		JFrame f = new JFrame("Island Generator");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		setPreferredSize(new Dimension(w, h));
		f.pack();
		f.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		ig.draw(g);
	}

}
