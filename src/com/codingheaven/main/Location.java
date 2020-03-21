package com.codingheaven.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Location {

	private static final int pSIZE = 150;
	private int width, height;

	private Person[] population;

	private int[] xWalls;
	private int[] yWalls;

	public Location(int w, int h) {
		setSize(w, h);
		setWalls(w, h);

		population = new Person[pSIZE];

		for (int i = 0; i < population.length; i++)
			population[i] = new Person(w, h);

		population[0].setSick();
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

	private void setWalls(int w, int h) {
		xWalls = new int[2];
		xWalls[0] = 0;
//		xWalls[1] = width / 4;
//		xWalls[2] = width / 3;
//		xWalls[3] = width / 2;
		xWalls[1] = width;

		yWalls = new int[2];
		yWalls[0] = 0;
//		yWalls[1] = height / 4;
//		yWalls[2] = height / 3;
//		yWalls[3] = height / 2;
		yWalls[1] = height;
	}

	public void draw(Graphics g) {
		drawWalls(g);

		for (int i = 0; i < population.length; i++)
			population[i].draw(g);

	}

	private void drawWalls(Graphics g) {
		// Dotted line in the middle
		g.setColor(Color.white);
		Graphics2D g2d = (Graphics2D) g; // a more complex Graphics class used to draw Objects (as in give in an Object
		// in parameter and not dimensions or coordinates)
		// How to make a dotted line:
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
		g2d.setStroke(dashed);

		for (int i = 0; i < xWalls.length; i++)
			g.drawLine(xWalls[i], 0, xWalls[i], height);

		for (int i = 0; i < yWalls.length; i++)
			g.drawLine(0, yWalls[i], width, yWalls[i]);
	}

	public void update() {
		for (int i = 0; i < population.length; i++) {
			population[i].update(xWalls, yWalls, width, height);

			for (int j = 0; j < population.length; j++)
				if (i != j) {
					Person p1 = population[i];
					Person p2 = population[j];

					boolean collided = p1.collided(p2);

					if (collided) {
						float xv1 = p1.getxVel();
						float yv1 = p1.getyVel();

						float xv2 = p2.getxVel();
						float yv2 = p2.getyVel();

						p1.setxVel(xv2);
						p1.setyVel(yv2);

						p2.setxVel(xv1);
						p2.setyVel(yv1);

//						int size1 = p1.getSize();
//						int size2 = p2.getSize();
//
//						int sumSize = size1 + size2;
//						int diffSize = size1 - size2;
//
//						float finalVelx1 = diffSize / sumSize * xv1 + 2 * size2 / sumSize * xv2;
//						float finalVely1 = diffSize / sumSize * yv1 + 2 * size2 / sumSize * yv2;
//						
//						float finalVelx2 = 2 * size1 / sumSize * xv1 - diffSize / sumSize * xv2;
//						float finalVely2 = 2 * size1 / sumSize * yv1 - diffSize / sumSize * yv2;
//						
//						float finalVelx1 = diffSize / sumSize * xv1;
//						float finalVely1 = diffSize / sumSize * yv1;
//						
//						float finalVelx2 = 2 * size1 / sumSize * xv1;
//						float finalVely2 = 2 * size1 / sumSize * yv1;
//						
//						p1.setxVel(finalVelx1);
//						p1.setyVel(finalVely1);
//						
//						p2.setxVel(finalVelx2);
//						p2.setyVel(finalVely2);

					}

				}

		}
	}

}