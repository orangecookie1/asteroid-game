public class Projectile{

	private int xPosition;
	private int yPosition;
	public static int SPEED = 5;

	public Projectile(int xPosition, int yPosition){
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public int getX(){
		return xPosition;
	}

	public int getY(){
		return yPosition;
	}

	public void updateProjectilePosition(){
		yPosition -= SPEED;
	}
}
