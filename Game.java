import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Game extends JComponent {
    private int shipX;
    private int shipY;
    private int time = 0;
    private int lives = 5;
	private int asteroidWinAmount = 50;
	private int asteroidsHit = 0;
	private boolean gameOver = false;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Rectangle> enemyRectangles;
	private Rectangle playerRectangle;
	private Timer timer;
	private JFrame frame;
	private int dim = 50;
	BufferedImage spaceshipImage;
	BufferedImage asteroidImage;
	BufferedImage redLaser;
	BufferedImage blueLaser;
	BufferedImage spaceBackground2;

    public Game(JFrame frame){
        this.frame = frame;
        asteroids = new ArrayList<>();
        projectiles = new ArrayList<>();
        enemyRectangles = new ArrayList<>();
        shipX = frame.getWidth() / 2;
        shipY = frame.getHeight() / 2;
        playerRectangle = new Rectangle(shipX, shipY, 50, 50);
        setFocusable(true);
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent event){
                handleKeyPress(event);
            }
        });

		try{
			spaceshipImage = ImageIO.read(new File("spaceShip.png"));
			asteroidImage = ImageIO.read(new File("Asteroid.png"));
			redLaser = ImageIO.read(new File("redLaser.png"));
			blueLaser = ImageIO.read(new File("blueLaser.png"));
			spaceBackground2 = ImageIO.read(new File("spaceBackground2.jpg"));
		}catch(IOException e){}
		spaceshipImage = resize(spaceshipImage, dim, dim);
		asteroidImage = resize(asteroidImage, 20, 20);
		redLaser = resize(redLaser, 10, 20);
		blueLaser = resize(blueLaser, 10, 20);
		spaceBackground2 = resize(spaceBackground2, 400, 600);

        timer = new Timer(10, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                if (!gameOver) {
					updateScreen();
					frame.repaint();
				}
            }
        });
        timer.start();
    }

    private void shoot() {
		projectiles.add(new Projectile(shipX + 20, shipY - 5));
	}

	public BufferedImage resize(BufferedImage image, int width, int height){
		Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage scaledVersion = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = scaledVersion.createGraphics();
		g2.drawImage(temp, 0, 0,null);
		g2.dispose();
		return scaledVersion;
	}

	private void generateNewAsteroid(){
		double chance = Math.random();
		double diagonalChance = Math.random();
		if(chance > .03) return;
		if(diagonalChance > .3){
			DiagonalAsteroid asteroid1 = new DiagonalAsteroid(this);
			asteroids.add(asteroid1);
			enemyRectangles.add(new Rectangle(asteroid1.getAsteroidX(), asteroid1.getAsteroidY(), 20, 20));
		}
		Asteroid asteroid = new Asteroid(this);
		asteroids.add(asteroid);
		enemyRectangles.add(new Rectangle(asteroid.getAsteroidX(), asteroid.getAsteroidY(), 20, 20));
	}

	private void removeAsteroid(int index){
		asteroids.get(index).setDestroyed(true);
		asteroids.remove(index);
		enemyRectangles.remove(index);
	}

	private void handleKeyPress(KeyEvent event) {
		int code = event.getKeyCode();
		int speed = 15;
		if (code == event.VK_SPACE) {
			shoot();
		}

		if (code == event.VK_LEFT) shipX -= speed;
		if (code == event.VK_RIGHT) shipX += speed;
		if (code == event.VK_UP) shipY -= speed;
		if (code == event.VK_DOWN) shipY += speed;

		playerRectangle.setLocation(shipX, shipY);
	}

	private void updateScreen() {
		checkForAsteroidCollisions();
		updateAsteroidLocation();
		generateNewAsteroid();
		checkProjectileCollisions();
		updateProjectiles();
	}

	public void updateEnemyRectangles() {
		for (int i = 0; i < asteroids.size(); i++) {
			Asteroid asteroid = asteroids.get(i);
			enemyRectangles.get(i).setLocation(asteroid.getAsteroidX(), asteroid.getAsteroidY());
		}
	}

	private void checkForAsteroidCollisions() {
		if (enemyRectangles.size() == 0) return;

		for (int i = enemyRectangles.size() - 1; i >= 0; i--) {
			Rectangle enemy = enemyRectangles.get(i);
			if (enemy.intersects(playerRectangle)) {
				removeAsteroid(i);
				i --;
				lives --;
			}
		}
	}

	private void checkProjectileCollisions() {
		ArrayList<Integer> projectilesToRemove = new ArrayList<>();

		if (projectiles.size() == 0) return;
		Rectangle hitbox = new Rectangle(0, 0, 10, 20);

		for (int i = projectiles.size() - 1; i >= 0; i--) {
			Projectile proj = projectiles.get(i);

			if (proj.getY() < 0) {
				projectilesToRemove.add(i);
				continue;
			}

			if (enemyRectangles.size() == 0) continue;

			hitbox.setLocation(proj.getX(), proj.getY());

			for (int j = asteroids.size() - 1; j >= 0; j--) {
				if (enemyRectangles.get(j).intersects(hitbox)) {
					removeAsteroid(j);
					asteroidsHit++;
				}
			}

		}
	}

	private void drawShip(Graphics graphics) {
		graphics.drawImage(spaceshipImage, shipX, shipY, this);
	}

	private void drawAsteroids(Graphics graphics) {
		for (Asteroid asteroid : asteroids) {
			if(asteroid.isDestroyed() == false && asteroid instanceof DiagonalAsteroid){
				graphics.setColor(Color.RED);
				graphics.drawImage(asteroidImage, asteroid.getAsteroidX(), asteroid.getAsteroidY(), this);
			}else if (!asteroid.isDestroyed()){
				graphics.setColor(Color.YELLOW);
				graphics.drawImage(asteroidImage, asteroid.getAsteroidX(), asteroid.getAsteroidY(), this);

			}
		}
	}

	private void drawProjectiles(Graphics graphics) {
		for (Projectile projectile : projectiles){
			graphics.setColor(Color.GREEN);
			graphics.drawImage(redLaser, projectile.getX(), projectile.getY(), this);

		}
	}

	private void updateAsteroidLocation() {
		for (Asteroid asteroid : asteroids) {
			asteroid.updateAsteroid();
		}

		updateEnemyRectangles();
	}

	private void updateProjectiles() {
		for (Projectile projectile : projectiles) {
			projectile.updateProjectilePosition();
		}
	}

	protected void paintComponent(Graphics graphics) {
		graphics.setFont(new Font("sans-serif", Font.PLAIN, 18));

		graphics.setColor(Color.RED);
		graphics.drawString("Asteroid count: " + asteroidsHit, 10, 30);

		graphics.setColor(Color.BLACK);
		//graphics.drawImage(spaceBackground, 0, 0, this);
		graphics.drawImage(spaceBackground2, 0, 0, this);

		graphics.setColor(Color.WHITE);
		graphics.drawString("Time: " + time / 1000.0, frame.getWidth()-150, 30);

		if(!gameOver){
			drawShip(graphics);
			drawAsteroids(graphics);
			drawProjectiles(graphics);
			time += 20;
		}
		setGameOver(graphics);
	}

	private void setEndScreenText(Graphics graphics, String str) {
		graphics.setFont(new Font("sans-serif", Font.BOLD, 40));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Game Over!", 20, 100);

		graphics.setFont(new Font("sans-serif", Font.PLAIN, 20));
		graphics.drawString(str, 20, 150);

		graphics.drawString("Stats:", 20, 400);
		graphics.drawString(asteroidsHit + " asteroids destroyed", 20, 450);
	}

	private void setGameOver(Graphics graphics) {
		if (lives <= 0) {
			gameOver = true;
			setEndScreenText(graphics, "You died!");
		}

		if (asteroidsHit >= asteroidWinAmount) {
			gameOver = true;
			setEndScreenText(graphics, "All asteroids destroyed.");

		}
	}
}