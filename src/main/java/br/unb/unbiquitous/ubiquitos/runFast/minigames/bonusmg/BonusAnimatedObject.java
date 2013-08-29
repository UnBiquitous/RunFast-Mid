package br.unb.unbiquitous.ubiquitos.runFast.minigames.bonusmg;

import java.util.Random;

import br.unb.unbiquitous.ubiquitos.runFast.minigames.general.AnimatedObject;
import br.unb.unbiquitous.ubiquitos.runFast.minigames.general.LoadManager;

/**
 * AnimatedObjects that pass thorough the screen.
 *
 */
public class BonusAnimatedObject extends AnimatedObject{

	//Object types identifiers
	public static final int TYPE_COIN = 1; 
	public static final int TYPE_BOMB = 2; 
	
	//Frame times
	private static final int COIN_FRAME_TIME = 33;
	private static final int BOMB_FRAME_TIME = 10;
	private static final int DEADCOIN_FRAME_TIME = 100;
	private static final int DEADBOMB_FRAME_TIME = 100;
	
	//Coin and bomb sizes
	private static final int COIN_WH = 50;
	private static final int BOMB_WH = 50;
	
	//Velocity constants
	private static final int VELOCITY_BOOST = 12;
	private static final int VELOCITY_MIN = 4;
	
	//Object velocity
	private float velocity;
	
	private int type=0;
	
	public BonusAnimatedObject(float x, float y, Random random, BonusLoadManager loadManager, int type){
		super(x,y,random,loadManager);
		
		this.velocity = Math.abs(random.nextInt()%VELOCITY_BOOST)+VELOCITY_MIN;
		this.type = type;
		load(loadManager);
	}

	@Override
	public void load(LoadManager loader){
		BonusLoadManager loadManager = (BonusLoadManager)loader;
		
		time=frame=0;
		
		switch(type){
		case TYPE_COIN:
			image = loadManager.getCoin();
			deadImage = loadManager.getSparkle();
			frameTime = COIN_FRAME_TIME;
			deadFrameTime = DEADCOIN_FRAME_TIME;
			this.width = this.height = COIN_WH;
			break;
		case TYPE_BOMB:
			image = loadManager.getBomb();
			deadImage = loadManager.getSmoke();
			frameTime = BOMB_FRAME_TIME;
			deadFrameTime = DEADBOMB_FRAME_TIME;
			this.width = this.height = BOMB_WH;
			break;
		}
	}

	@Override
	public void update(int dt){
		super.update(dt);
		
	    if(!dead)
	    	updateMovement();
	}
	
	/**
	 * Moves the AnimatedObject based in its velocity.
	 */
	private void updateMovement(){
		y += velocity;
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
}