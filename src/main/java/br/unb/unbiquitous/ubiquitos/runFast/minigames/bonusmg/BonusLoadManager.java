package br.unb.unbiquitous.ubiquitos.runFast.minigames.bonusmg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import br.unb.unbiquitous.ubiquitos.runFast.mid.R;
import br.unb.unbiquitous.ubiquitos.runFast.minigames.general.LoadManager;

public class BonusLoadManager extends LoadManager{

	private static Bitmap[] coin =null,bomb =null,sparkle=null,smoke=null;
	
	public BonusLoadManager(Resources resources){
		super(resources);
		if(coin==null)
			loadCoin();
		if(bomb==null)
			loadBomb();
		if(sparkle==null)
			loadSparkle();
		if(smoke==null)
			loadSmoke();
	}
	
	private void loadCoin(){
		coin = new Bitmap[9];
		coin[0] = BitmapFactory.decodeResource(resources, R.drawable.coin1);
		coin[1] = BitmapFactory.decodeResource(resources, R.drawable.coin2);
		coin[2] = BitmapFactory.decodeResource(resources, R.drawable.coin3);
		coin[3] = BitmapFactory.decodeResource(resources, R.drawable.coin4);
		coin[4] = BitmapFactory.decodeResource(resources, R.drawable.coin5);
		coin[5] = BitmapFactory.decodeResource(resources, R.drawable.coin6);
		coin[6] = BitmapFactory.decodeResource(resources, R.drawable.coin7);
		coin[7] = BitmapFactory.decodeResource(resources, R.drawable.coin8);
		coin[8] = BitmapFactory.decodeResource(resources, R.drawable.coin9);
	}
	
	private void loadBomb(){
		bomb = new Bitmap[1];
		bomb[0] = BitmapFactory.decodeResource(resources, R.drawable.bomb);
	}
	
	private void loadSparkle(){
		sparkle = new Bitmap[5];
		sparkle[0] = BitmapFactory.decodeResource(resources, R.drawable.sparkle1);
		sparkle[1] = BitmapFactory.decodeResource(resources, R.drawable.sparkle2);
		sparkle[2] = BitmapFactory.decodeResource(resources, R.drawable.sparkle3);
		sparkle[3] = BitmapFactory.decodeResource(resources, R.drawable.sparkle4);
		sparkle[4] = BitmapFactory.decodeResource(resources, R.drawable.sparkle5);
	}

	private void loadSmoke(){
		smoke = new Bitmap[5];
		smoke[0] = BitmapFactory.decodeResource(resources, R.drawable.smoke1);
		smoke[1] = BitmapFactory.decodeResource(resources, R.drawable.smoke2);
		smoke[2] = BitmapFactory.decodeResource(resources, R.drawable.smoke3);
		smoke[3] = BitmapFactory.decodeResource(resources, R.drawable.smoke4);
		smoke[4] = BitmapFactory.decodeResource(resources, R.drawable.smoke5);
	}
	
	/**
	 * @return the coin
	 */
	public Bitmap[] getCoin() {
		return coin;
	}

	/**
	 * @return the bomb
	 */
	public Bitmap[] getBomb() {
		return bomb;
	}

	/**
	 * @return the sparkle
	 */
	public Bitmap[] getSparkle() {
		return sparkle;
	}

	/**
	 * @return the smoke
	 */
	public Bitmap[] getSmoke() {
		return smoke;
	}
	
	
}
