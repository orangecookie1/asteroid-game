import javax.swing.JComponent;

public class Asteroid{
	private JComponent component;
	private int asteroidX;
	private int asteroidY;
	private int asteroidSpeed;
	private boolean isDestroyed;

	public Asteroid(JComponent component){
		this.component = component;
		asteroidX = (int)(Math.random()*component.getWidth());
		isDestroyed = false;
		asteroidSpeed = (int)(Math.random()*5)+1;
	}
	
    public JComponent getComponent() {
        return component;
    }
	public int getAsteroidX(){
		return asteroidX;
	}
	public int getAsteroidY(){
		return asteroidY;
	}
	public boolean isDestroyed(){
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed){
		this.isDestroyed = isDestroyed;
	}
	public void updateAsteroid(){
		if(!isDestroyed){
			asteroidY += asteroidSpeed;

			if(asteroidY > component.getHeight()){
			 asteroidY = 0;
			 asteroidX = (int)(Math.random()*component.getWidth());
			}
		}

	}
}
