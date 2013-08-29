package br.unb.unbiquitous.ubiquitos.runFast.minigames.breakmg;

import java.util.Random;

import android.graphics.Bitmap;
import br.unb.unbiquitous.ubiquitos.runFast.minigames.general.AnimatedObject;
import br.unb.unbiquitous.ubiquitos.runFast.minigames.general.LoadManager;

/**
 * AnimatedObjects that appears randomly through the screen.
 *
 */
public class BreakAnimatedObject extends AnimatedObject{

	//Object types identifiers
	public static final int TYPE_DYNAMITE = 1; 
	public static final int TYPE_GEAR = 2; 
	public static final int TYPE_TIRE = 3; 
	
	//Object types scores
	public static final int SCORE_DYNAMITE = -20; 
	public static final int SCORE_GEAR = 6; 
	public static final int SCORE_TIRE = 10;
	
	//Default frame time
	private static final int DEFAULT_FRAME_TIME = 10;
	
	//Dead frame time
	private static final int DEADSMOKE_FRAME_TIME = 100;
	private static final int DEADSPARKLE_FRAME_TIME = 100;
	
	//LifeTime in the screen and score
	private int lifeTime;
	private int score;
	
	private int type;
	
	public BreakAnimatedObject(float x, float y, Random random, BreakLoadManager loadManager){
		super(x,y,random,loadManager);
		
		lifeTime = (Math.abs(random.nextInt()%5)+2)*1000;
	}
	
	/**
	 * Randomly initialize the AnimatedObject type.
	 */
	private void initType(){
		int newType = Math.abs(random.nextInt()%10);
		if(newType < 3){
			type = TYPE_DYNAMITE;
			score = SCORE_DYNAMITE;
		}else if(newType <7){
			type = TYPE_GEAR;
			score = SCORE_GEAR;
		}else{
			type = TYPE_TIRE;
			score = SCORE_TIRE;
		}
	}
	
	@Override
	public void load(LoadManager loader){
		BreakLoadManager loadManager = (BreakLoadManager)loader;
		
		initType();
		
		time=frame=0;

		Bitmap[] imageHelp;
		switch(type){
		case TYPE_DYNAMITE:
			image = loadManager.getDynamite();
			frameTime = DEFAULT_FRAME_TIME;
			deadImage = loadManager.getSmoke();
			deadFrameTime = DEADSMOKE_FRAME_TIME;
			break;
		case TYPE_GEAR:
			imageHelp = loadManager.getGear();
			image = new Bitmap[1];
			image[0] = imageHelp[Math.abs(random.nextInt()%imageHelp.length)];
			frameTime = DEFAULT_FRAME_TIME;
			
			deadImage = loadManager.getSparkle();
			deadFrameTime = DEADSPARKLE_FRAME_TIME;
			break;
		case TYPE_TIRE:
			imageHelp = loadManager.getTire();
			image = new Bitmap[1];
			image[0] = imageHelp[Math.abs(random.nextInt()%imageHelp.length)];
			frameTime = DEFAULT_FRAME_TIME;
			
			deadImage = loadManager.getSparkle();
			deadFrameTime = DEADSPARKLE_FRAME_TIME;
			break;
		}
		this.width = image[0].getWidth();
		this.height = image[0].getHeight();
	}
	
	@Override
	public void update(int dt){
		if(!dead)
	    	lifeTime -= dt;

		super.update(dt);
	}
	
	@Override
	public boolean isDone(){
		if((!dead) && (lifeTime < 0))
			return true;
		
		return super.isDone();
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

}
