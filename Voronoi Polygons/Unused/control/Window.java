package control;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import generators.PolygonGenerator;

public class Window extends JPanel implements KeyListener {

	private PolygonGenerator pg;
	private boolean relax;

	public Window(int w, int h, PolygonGenerator pg) {
		this.pg = pg;
		JFrame f = new JFrame("Voronoi Polygons");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		setPreferredSize(new Dimension(w, h));
		f.pack();
		f.setVisible(true);
		f.addKeyListener(this);

		// relax = true;
		// while (relax) {
		// pg.lloydRelaxation();
		// repaint();
		// try {
		// Thread.sleep(10);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	@Override
	public void paintComponent(Graphics g) {
		pg.draw(g);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// relax = !relax;

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
