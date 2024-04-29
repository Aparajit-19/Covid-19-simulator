package com.aparajit.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Human{
	
	private static final int MAX_SIZE = 20;
	private static final int MIN_SIZE = 10;
	private static final float MAX_SPEED = 4.5f;
	private int size; 
	private int x, y; 
	private float xVel = 0, yVel = 0; 
	private float probSick = 0.90f; 
	private float recoveryTime = 10000.0f; 
	private long sickTime = -1l; 

	private enum State{
		HEALTHY, RECOVERED, SICK
	}

	private State state = State.HEALTHY; 

	public Human(int w, int h){
		Random rand = new Random();
		size = rand.nextInt((MAX_SIZE - MIN_SIZE) + 1) + MIN_SIZE;
		x = rand.nextInt(w - size);
		y = rand.nextInt(h - size);

		while((int) xVel == 0)
			xVel = rand.nextFloat() * MAX_SPEED * 2 - MAX_SPEED;

		while((int) yVel == 0)
			yVel = rand.nextFloat() * MAX_SPEED * 2 - MAX_SPEED;
	}

	public State getState(){
		return state;
	}

	public void setSick(){
		state = State.SICK;
		sickTime = System.currentTimeMillis();
	}

	public float getxVel(){
		return xVel;
	}

	public void setxVel(float xVel){
		this.xVel = xVel;
	}

	public float getyVel(){
		return yVel;
	}

	public void setyVel(float yVel){
		this.yVel = yVel;
	}

	public float getNextX(){
		return x + xVel;
	}

	public float getNextY(){
		return y + yVel;
	}

	public int getSize(){
		return size;
	}

	public void draw(Graphics g){
		Color color;

		switch(state){
			case HEALTHY:
				color = Color.GREEN;
				break;
			case RECOVERED:
				color = Color.BLUE;
				break;
			case SICK:
				color = Color.RED;
				break;
			default:
				color = Color.WHITE;
		}

		g.setColor(color);
		g.fillOval(x, y, size, size);
	}

	public void update(int[] xWalls, int[] yWalls, int w, int h){
		x += xVel;
		y += yVel;
		Rectangle nextMe = new Rectangle((int) Math.ceil(getNextX()), (int) Math.ceil(getNextY()), size, size);

		for(int i = 0; i < xWalls.length; i++)
			if(nextMe.intersectsLine(xWalls[i], 0, xWalls[i], h))
				xVel = -xVel;

		for(int i = 0; i < yWalls.length; i++)
			if(nextMe.intersectsLine(0, yWalls[i], w, yWalls[i]))
				yVel = -yVel;
		
		if(System.currentTimeMillis() - sickTime >= recoveryTime && state == State.SICK && sickTime > 0)
			state = State.RECOVERED;
	}

	public boolean collided(Human p){
		double dist = Math.sqrt(Math.pow(getNextX() - p.getNextX(), 2) + Math.pow(getNextY() - p.getNextY(), 2));
		boolean collided = (dist < size / 2.0 + p.getSize() / 2.0);

		if(collided && p.getState() == State.SICK && state == State.HEALTHY)
			if(Math.random() < probSick)
				setSick(); 

		return collided;
	}
}