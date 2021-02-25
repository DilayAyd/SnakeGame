import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.FontMetrics;


import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel  implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int [GAME_UNITS];
	final int y[] = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R' ;
	boolean running = false ;
	Timer timer;
	Random random ;
	
	GamePanel() {
		
		random = new Random ();
		this.setPreferredSize(new Dimension (SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground (new Color(235,151,95));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer (DELAY,this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}

	public void draw(Graphics g) {
		
		if (running) { 
					//kareli zemin çizimini sakladýk
					 /*
						for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
						
							g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
							g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE );
						} */
			//elma çizimi
			g.setColor(new Color(180,231,233));
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			//yýlan çizimi
			for (int i= 0 ; i< bodyParts; i++) {
				if (i == 0 ) {
					g.setColor(new Color(180,231,233));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
		
				else {
					//g.setColor(new Color(180,231,233)); //renk yýlan her büyüdüðünde ton ton açýlacak þekilde kod yaz 
					g.setColor(new Color (random.nextInt(255),random.nextInt(255),random.nextInt(255))); //her kareyi random farklý renk ile dolduracak
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);  
					
				}
			}
			 //oyun içi üstte yazan score çizimi
			g.setColor(new Color(46,85,87));
			g.setFont(new Font("Arial",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score:"+applesEaten, (SCREEN_WIDTH- metrics.stringWidth("Score:"+applesEaten))/2, g.getFont().getSize());
			
		}
		
		else {
			gameOver(g);
			
		}
		
		
	}  
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

	}
	
	public void move() {
		for (int i = bodyParts; i>0 ; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		
		}
		switch (direction) {
		case 'U':
			y[0] = y[0]- UNIT_SIZE ;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE ;
			break;
			
		case 'L':
			x[0] = x[0]- UNIT_SIZE ;
			break;
			
		case 'R':
			x[0] = x[0]+ UNIT_SIZE ;
			break;
		
		}
		
	}
	
	public void checkApple() {
		// x[0] ve y[0] yýlanýn baþý oluyor
		
		if ((x[0]== appleX) && (y[0]== appleY)) {
			bodyParts++; //vücudu elma yiyince 1 kare arttý
			applesEaten++; //score 1 puan arttý
			newApple();
		}
		
	}
	
	public void checkCollisions() {
		//yýlanýn kafasý kendisine deðer ise oyun biter
		for (int i= bodyParts; i>0; i--) {
			if ((x[0]== x[i]) && (y[0]== y[i])) {
				running = false;
			}
		}
		// yýlanýn kafasý sol kenara deðerse oyun biter
		if (x[0]<0) {
			running = false;
		}
		
		// yýlanýn kafasý sað kenara deðerse oyun biter
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		// yýlanýn kafasý yukarý kenara deðerse oyun biter
		if (y[0]<0) {
			running = false;
		}
		
		// yýlanýn kafasý aþaðý kenara deðerse oyun biter
		
		if (x[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if (!running) {
			timer.stop();
		}
		
	}
	
	public void gameOver(Graphics g) {
		
		//Game over text
		
		g.setColor(new Color(46,85,87));
		g.setFont(new Font("Magneto",Font.BOLD,75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH- metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//score
		g.setColor(new Color(46,85,87));
		g.setFont(new Font("Magneto",Font.BOLD,40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score:"+applesEaten, (SCREEN_WIDTH- metrics2.stringWidth("Score:"+applesEaten))/2,SCREEN_HEIGHT/3);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L' ; 
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R' ; 
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U' ; 
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D' ; 
				}
				break;
				
			}
			
		}
		
		
	}

}
