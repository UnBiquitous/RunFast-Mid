package br.unb.unbiquitous.ubiquitos.runFast.minigames.general;

import android.content.res.Resources;

public abstract class LoadManager {
	
	protected Resources resources = null;
	
	public LoadManager(Resources resources) {
		this.resources = resources;
	}
}
