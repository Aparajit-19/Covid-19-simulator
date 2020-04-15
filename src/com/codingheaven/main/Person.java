package com.codingheaven.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 * a person living at the location
 * 
 * @author Zayed
 *
 */
public class Person {

	/*
	 * Max and min sizes (diameter) for the circle representing a person
	 */
	private static final int MAX_SIZE = 20;
	private static final int MIN_SIZE = 10;

	/*
	 * Maximum speed, also minimum = -maximum = -3.5f
	 */
	private static final float MAX_SPEED = 4.5f;

	private int size; // diameter of the circle representing the person
	private int x, y; // position
	private float xVel = 0, yVel = 0; // velocity components

	private float probSick = 0.90f; // probability of getting sick after a collision with a sick person
	private float recoveryTime = 10000.0f; // time in milliseconds to recover from first sick
	private long sickTime = -1l; // store the time the person has been sick

	/*
	 * possible states of a person
	 */
	private enum State {
		HEALTHY, RECOVERED, SICK
	}

	private State state = State.HEALTHY; // state of the person, default healthy

	/**
	 * constructor
	 * 
	 * @param w - width of the city
	 * @param h - height of the city
	 */
	public Person(int w, int h) {
		Random rand = new Random();

		size = rand.nextInt((MAX_SIZE - MIN_SIZE) + 1) + MIN_SIZE;

		x = rand.nextInt(w - size);
		y = rand.nextInt(h - size);

		while ((int) xVel == 0)
			xVel = rand.nextFloat() * MAX_SPEED * 2 - MAX_SPEED;

		while ((int) yVel == 0)
			yVel = rand.nextFloat() * MAX_SPEED * 2 - MAX_SPEED;

	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * set the person to sick state, and start timer for recovery
	 */
	public void setSick() {
		state = State.SICK;
		sickTime = System.currentTimeMillis();
	}

	/**
	 * @return the xVel
	 */
	public float getxVel() {
		return xVel;
	}

	/**
	 * @param xVel - the xVel to set
	 */
	public void setxVel(float xVel) {
		this.xVel = xVel;
	}

	/**
	 * @return the yVel
	 */
	public float getyVel() {
		return yVel;
	}

	/**
	 * @param yVel - the yVel to set
	 */
	public void setyVel(float yVel) {
		this.yVel = yVel;
	}

	/**
	 * @return the x
	 */
	public float getNextX() {
		return x + xVel;
	}

	/**
	 * @return the y
	 */
	public float getNextY() {
		return y + yVel;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Draw the person
	 * 
	 * @param g - tool to draw with
	 */
	public void draw(Graphics g) {
		Color color;

		switch (state) {

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

	/**
	 * updates the persons state and tests for collisions with other people or walls
	 * 
	 * @param xWalls - walls with equation x=k
	 * @param yWalls - walls with equation y=k
	 * @param w      - width of city
	 * @param h      - height of city
	 */
	public void update(int[] xWalls, int[] yWalls, int w, int h) {

		x += xVel;
		y += yVel;

		Rectangle nextMe = new Rectangle((int) Math.ceil(getNextX()), (int) Math.ceil(getNextY()), size, size);

		for (int i = 0; i < xWalls.length; i++)
			if (nextMe.intersectsLine(xWalls[i], 0, xWalls[i], h))
				xVel = -xVel;

		for (int i = 0; i < yWalls.length; i++)
			if (nextMe.intersectsLine(0, yWalls[i], w, yWalls[i]))
				yVel = -yVel;

		// recovery testing
		if (System.currentTimeMillis() - sickTime >= recoveryTime && state == State.SICK && sickTime > 0)
			state = State.RECOVERED;

	}

	/**
	 * test if collided
	 * 
	 * @param p - the person to test the collision with
	 * @return true if collided with the other person, false if otherwise
	 */
	public boolean collided(Person p) {
		double dist = Math.sqrt(Math.pow(getNextX() - p.getNextX(), 2) + Math.pow(getNextY() - p.getNextY(), 2));

		boolean collided = (dist < size / 2.0 + p.getSize() / 2.0);

		if (collided && p.getState() == State.SICK && state == State.HEALTHY)
			if (Math.random() < probSick)
				setSick(); // make sick

		return collided;
	}

}