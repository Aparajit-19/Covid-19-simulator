package com.aparajit.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Simulator extends Canvas implements Runnable{

	public final static int WIDTH = 1500;
	public final static int HEIGHT = WIDTH * 9 / 16; 
	public boolean running = false; 
	private Thread gameThread; 
	Area city; 

	public Simulator(){
		canvasSetup();
		initialize();
		newWindow();
	}

	private void newWindow(){
		JFrame frame = new JFrame("Covid-19 Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		start();
	}

	private void initialize(){
		city = new Area(getWidth(), getHeight());
	}

	private void canvasSetup(){
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent e){
				int code = e.getKeyCode();

				if(code == KeyEvent.VK_R)
					initialize();
			}
		});

		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent componentEvent){
				initialize();
			}
		});

		this.setFocusable(true);
	}

	@Override
	public void run(){
		this.requestFocus();
		final double MAX_FRAMES_PER_SECOND = 60.0;
		final double MAX_UPDATES_PER_SECOND = 60.0;
		long startTime = System.nanoTime();
		final double uOptimalTime = 1000000000 / MAX_UPDATES_PER_SECOND;
		final double fOptimalTime = 1000000000 / MAX_FRAMES_PER_SECOND;
		double uDeltaTime = 0, fDeltaTime = 0;
		int frames = 0, updates = 0;
		long timer = System.currentTimeMillis();

		while(running){
			long currentTime = System.nanoTime();
			uDeltaTime += (currentTime - startTime) / uOptimalTime;
			fDeltaTime += (currentTime - startTime) / fOptimalTime;
			startTime = currentTime;

			while(uDeltaTime >= 1){
				update();
				updates++;
				uDeltaTime--;
			}

			if(fDeltaTime >= 1){
				render();
				frames++;
				fDeltaTime--;
			}

			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("UPS: " + updates + ", FPS: " + frames);
				frames = 0;
				updates = 0;
				timer += 1000;
			}
		}

		stop(); 
	}

	public synchronized void start(){
		gameThread = new Thread(this);
		gameThread.start(); 
		running = true;
	}

	public void stop(){
		try{
			gameThread.join();
			running = false;
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public void render(){
		BufferStrategy buffer = this.getBufferStrategy(); 

		if(buffer == null){ 
			this.createBufferStrategy(3); 
			return;
		}

		Graphics g = buffer.getDrawGraphics(); 
		drawBackground(g);
		city.draw(g);
		g.dispose(); 
		buffer.show(); 
	}

	private void drawBackground(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void update(){
		city.update();
	}

	public static void main(String[] args){
		new Simulator();
	}
}
