package br.unb.unbiquitous.ubiquitos.runFast.minigames.general;

import br.unb.unbiquitous.ubiquitos.runFast.mid.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Basic sound effects
 *
 */
public class SoundEffect {

	private SoundPool sp;
	int explosion = 0;
	int coin = 0;
	
	/**
	 * Starts the sound effects
	 * @param context
	 */
	public SoundEffect(Context context) {
		sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		
		explosion = sp.load(context, R.raw.bum, 1);
		coin = sp.load(context, R.raw.coin, 1);
	}
	
	/**
	 * Play the explosion sound effect
	 */
	public void playExplosion() {
		if(explosion!=0)
			sp.play(explosion, 1, 1, 0, 0, 1);
	}

	/**
	 * Play the coin sound effect
	 */
	public void playCoin() {
		if(coin!=0)
			sp.play(coin, 1, 1, 0, 0, 1);
	}
	
	
}
