package control;

import java.util.Random;

import generators.IslandGenerator;

public class Main {

	public static int width, height;

	public static void main(String[] args) {
		width = 704;
		height = 704;
		new Window(width, height, new IslandGenerator(0, 0, width, height, new Random().nextLong()));

	}

}
