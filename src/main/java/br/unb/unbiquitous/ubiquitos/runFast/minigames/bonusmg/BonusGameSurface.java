package br.unb.unbiquitous.ubiquitos.runFast.minigames.bonusmg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.unb.unbiquitous.ubiquitos.runFast.mid.MidManager;
import br.unb.unbiquitous.ubiquitos.runFast.minigames.general.SoundEffect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Bonus game surface. Controls the game actions and implements the game view surface.
 *
 */
public class BonusGameSurface extends SurfaceView implements Runnable, OnTouchListener{

	//Game delays
	private static final int DELAY = 33;
	private static final int RANDOM_DELAY = 350;
	
	//Game bounds
	private static final int BOTTOM_LINE = 550;
	private static final int RIGHT_LINE = 400;
	
	//Game time
	private static final int GAME_TIME = 30500;
	
	private SurfaceHolder holder;
	private Thread thread = null;
	private boolean isRunning = false;
	private BonusLoadManager loadManager = null;
	private List<BonusAnimatedObject> animatedObjects;
	
	private int time, score;
	
	private Random random = null;
	private int randomTime;
	
	private Paint paintTime, paintPoints;
	
	private SoundEffect sound;
	
	public BonusGameSurface(Context context,Resources resources) {
		super(context);
		holder = getHolder();
		loadManager = new BonusLoadManager(resources);
		animatedObjects = new ArrayList<BonusAnimatedObject>();
			
		time=score= 0;
		
		random = new Random();
		randomTime = 0;
		
		this.setOnTouchListener(this);
		
		initPaint();
		
		sound = new SoundEffect(context);
	}
	
	private void initPaint(){
		paintTime = new Paint();
		paintTime.setColor(Color.WHITE);
		paintTime.setTextSize(45);
		
		paintPoints = new Paint();
		paintPoints.setColor(Color.YELLOW);
		paintPoints.setTextSize(35);
	}

	/**
	 * Pause the game thread.
	 */
	public void pause(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread = null;
	}
	
	/**
	 * Resume the game.
	 */
	public void resume(){
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		int frameStart, dt, sleep;
		dt = DELAY;
		
		while(isRunning){
			frameStart = (int)System.currentTimeMillis();
			
			/*UPDATE*/
			update(dt);
			
			/*RENDER*/
			render();
			
			/*Delay*/
            dt = (int)System.currentTimeMillis() - frameStart;
            sleep = DELAY - dt;

            if (sleep < 0)
                sleep = 2;
            if(dt<DELAY){
            	try {
            		Thread.sleep(sleep);
            	} catch (InterruptedException e) {
            		System.out.println("interrupted");
            	}
            	dt = DELAY;
            }
		}
	}

	/**
	 * Make game updates, frame by frame.
	 * @param dt
	 */
	private void update(int dt){
		time += dt;
		
		generator(dt);
		verifyValidations();
		
		int i;
		for(i=0; i<animatedObjects.size(); ++i)
			animatedObjects.get(i).update(dt);
		
		if(time>GAME_TIME)
			MidManager.endMGBonus(MidManager.getGameDevice(),score);
			//pause();
			
	}
	
	/**
	 * Generates the new objects.
	 * @param dt
	 */
	private void generator(int dt){
		randomTime += dt;
		if(randomTime > RANDOM_DELAY){
			randomTime = 0;
			
			int newType = Math.abs(random.nextInt()%10);
			if(newType < 4){
				animatedObjects.add(new BonusAnimatedObject(Math.abs(random.nextInt()%RIGHT_LINE), -10,
						random, loadManager, BonusAnimatedObject.TYPE_BOMB));
			}else{
				animatedObjects.add(new BonusAnimatedObject(Math.abs(random.nextInt()%RIGHT_LINE), -10,
						random, loadManager, BonusAnimatedObject.TYPE_COIN));
			}
		}
	}
	
	/**
	 * Verify if the object is between the bounds.
	 */
	private void verifyValidations(){
		int i;
		for(i=0; i<animatedObjects.size(); ++i){
			if((animatedObjects.get(i).getY() > BOTTOM_LINE)||(animatedObjects.get(i).isDone())){
				animatedObjects.remove(i);
				--i;
			}
		}
	}
	
	/**
	 * Renders all game objects.
	 */
	private void render(){
		int i;
		if(holder.getSurface().isValid()){
			Canvas canvas = holder.lockCanvas();
			canvas.drawRGB(70,80,40);
			
			for(i=0; i<animatedObjects.size(); ++i)
				animatedObjects.get(i).render(canvas);
			
			renderTime(canvas);
			renderPoints(canvas);
			
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	/**
	 * Renders the game time.
	 * @param canvas
	 */
	private void renderTime(Canvas canvas){
		if(time<10000)
			canvas.drawText("0"+(int)time/1000, 200, 670, paintTime);
		else
			canvas.drawText(""+(int)time/1000, 200, 670, paintTime);
	}
	
	/**
	 * Render game points.
	 * @param canvas
	 */
	private void renderPoints(Canvas canvas){
		canvas.drawText(""+score, 350, 700, paintPoints);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN)
			return false;

		for(int i=0; i<animatedObjects.size(); ++i){
		
			if(animatedObjects.get(i).isTouching(event.getX(), event.getY())){	
				if(animatedObjects.get(i).getType() == BonusAnimatedObject.TYPE_BOMB){
					score -= 10;
				
					sound.playExplosion();
				}else{
					score += 5;
					
					sound.playCoin();
				}
				
				animatedObjects.get(i).killAnimatedObject();
			}
		}
		
		return true;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
}
