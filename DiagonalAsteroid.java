import javax.swing.JComponent;

public class DiagonalAsteroid extends Asteroid{
    private int asteroidSpeedX;
    private int asteroidX;
	private int asteroidY;
    private int asteroidSpeedY;
    private boolean isDestroyed;
    private double leftOrRight = (int)(Math.random() * 2) + 1;


    public DiagonalAsteroid(JComponent component){
        super(component);
        asteroidSpeedX = (int)(Math.random() * 2) + 1;
        asteroidSpeedY = (int)(Math.random() * 2) + 1;
        isDestroyed = false;
        if(leftOrRight == 1){
            asteroidX = 0;    
        }else{
            asteroidX = component.getWidth();
        }
        
        asteroidY = (int)(Math.random()*component.getHeight());
    }

    @Override
    public void updateAsteroid() {
        if (!isDestroyed()) {
            if(leftOrRight == 1){
                asteroidX += asteroidSpeedX;
            }else{
                asteroidX -= asteroidSpeedX;
            }
            asteroidY += asteroidSpeedY;

            if (asteroidY > getComponent().getHeight() || asteroidX > getComponent().getWidth()) {
                asteroidY = 0;
                asteroidX = (int)(Math.random() * getComponent().getWidth());
            }
        }
    }

    public JComponent getComponent() {
        return super.getComponent();
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
}