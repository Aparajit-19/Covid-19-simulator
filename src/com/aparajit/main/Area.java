package com.aparajit.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Area{

	private final int pSIZE = 75; 
	private int width, height; 
	private Human[] population; 
	private boolean quarantine = false; 
	private int[] xWalls; 
	private int[] yWalls; 

	public Area(int w, int h){
		setSize(w, h);
		setWalls(w, h);
		population = new Human[pSIZE];

		for(int i = 0; i < population.length; i++){
			population[i] = new Human(w, h);
		}

		population[0].setSick(); 
	}

	public void setSize(int w, int h){
		width = w;
		height = h;
	}

	private void setWalls(int w, int h){
		if(quarantine){
			xWalls = new int[6];
			xWalls[0] = 0;
			xWalls[1] = w / 5;
			xWalls[2] = w / 4;
			xWalls[3] = w / 3;
			xWalls[4] = w / 2;
			xWalls[5] = w;

			yWalls = new int[6];
			yWalls[0] = 0;
			yWalls[1] = h / 5;
			yWalls[2] = h / 4;
			yWalls[3] = h / 3;
			yWalls[4] = h / 2;
			yWalls[5] = h;
		}else{
			xWalls = new int[2];
			xWalls[0] = 0;
			xWalls[1] = w;

			yWalls = new int[2];
			yWalls[0] = 0;
			yWalls[1] = h;
		}
	}

	public void draw(Graphics g){
		drawWalls(g);

		for(int i = 0; i < population.length; i++){
			population[i].draw(g);
		}
	}

	private void drawWalls(Graphics g){
		g.setColor(Color.black);
		Graphics2D g2d = (Graphics2D) g; 
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
		g2d.setStroke(dashed);

		for(int i = 0; i < xWalls.length; i++){
			g.drawLine(xWalls[i], 0, xWalls[i], height);
		}

		for(int i = 0; i < yWalls.length; i++){
			g.drawLine(0, yWalls[i], width, yWalls[i]);
		}
	}

	public void update(){
		for(int i = 0; i < population.length; i++){
			population[i].update(xWalls, yWalls, width, height);

			for(int j = 0; j < population.length; j++)
				if(i != j){
					Human p1 = population[i];
					Human p2 = population[j];
					boolean collided = p1.collided(p2);

					if(collided){
						float xv1 = p1.getxVel();
						float yv1 = p1.getyVel();

						float xv2 = p2.getxVel();
						float yv2 = p2.getyVel();

						p1.setxVel(xv2);
						p1.setyVel(yv2);

						p2.setxVel(xv1);
						p2.setyVel(yv1);
					}
				}
		}
	}
}