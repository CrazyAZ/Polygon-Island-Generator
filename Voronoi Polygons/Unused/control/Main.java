package control;

import java.util.Random;

import generators.PolygonGenerator;

public class Main {

	public static int width, height;

	public static void main(String[] args) {
		width = 1280;
		height = 704;
		new Window(width, height, new PolygonGenerator(0, 0, 1280, 704, 4000, 0, new Random()));

	}

}
