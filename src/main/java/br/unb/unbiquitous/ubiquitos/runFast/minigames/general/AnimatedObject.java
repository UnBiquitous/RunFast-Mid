package br.unb.unbiquitous.ubiquitos.runFast.minigames.general;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Animated object which has two states, alive and dead. The object makes the alive animation
 * until it is killed and change the rendered animation. The images used in both animations must
 * be loaded in the load method. The type must also be initialized in if there is more then one.
 *
 */
public abstract class AnimatedObject {

	//Default frame time
	protected static final int DEFAULT_FRAME_TIME = 33;
	
	//Default type
	protected static final int DEFAULT_POINT_TYPE = 0;
	protected static final int DEFAULT_MISS_TYPE = -1;
	
	//AnimatedObject attributes
	protected float x,y;
	protected int width,height;
	protected Bitmap[] image,deadImage;
	protected int time,frameTime,deadFrameTime,frame;
	protected boolean dead;
	
	protected Random random;
	
	public AnimatedObject(float x, float y, Random random, LoadManager loadManager){
		this.x = x;
		this.y = y;
		
		frameTime = deadFrameTime = DEFAULT_FRAME_TIME;
		
		this.random = random;
		
		dead = false;
		load(loadManager);
	}
	
	/**
	 * Load the animated object informations, must load the images.
	 * @param loadManager
	 */
	public abstract void load(LoadManager loader);
	
	/**
	 * Update the object frame based in the dt time.
	 * @param dt
	 */
	public void update(int dt){
		time += dt;

	    int frameSkip;
	    
	    if(!dead){
	    	frameSkip = time/frameTime;
		    time = time%frameTime;
		    
		    frame += frameSkip;
		    
	    	if(frame >= image.length){
	    		frame = (frame - image.length)%image.length;
	    	}
	    }else{
	    	frameSkip = time/deadFrameTime;
		    time = time%deadFrameTime;
		    
		    frame += frameSkip;
	    	if(frame >= deadImage.length){
	    		frame = deadImage.length-1;
	    	}
	    }
	    
	}
	
	/**
	 * Renders the animated object in the given canvas, based in it state (dead or not).
	 * @param canvas
	 */
	public void render(Canvas canvas){
		if(!dead)
			canvas.drawBitmap(image[frame], x, y, null);
		else
			canvas.drawBitmap(deadImage[frame], x, y, null);
	}
	
	/**
	 * Verify if it was touched by the given point.
	 * @param x
	 * @param y
	 * @return true if it touches the point and false otherwise.
	 */
	public boolean isTouching(float x, float y){
		if((x >= this.x)&&(x <= this.x+width)
				&&(y >= this.y)&&(y <= this.y+height))
			return true;
		return false;
	}

	/**
	 * Kills the AnimatedObject changing its state from alive to dead.
	 */
	public void killAnimatedObject(){
		dead = true;
		time = frame = 0;
	}
	
	/**
	 * Verify if the AnimatedObject is dead and finished the dead animation.
	 * So that it can be removed.
	 * @return true if it is dead and done, false otherwise
	 */
	public boolean isDone(){
		if((dead)&&(frame == deadImage.length-1))
			return true;
		return false;
	}
	
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
}
