package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.extra.AssetMan;
import com.mygdx.game.screens.GameScreen;

public class VandurGame extends Game {

	private GameScreen gameScreen;

	public AssetMan assetmanager;

	@Override
	public void create(){
		this.assetmanager = new AssetMan();

		this.gameScreen = new GameScreen(this);

		setScreen(this.gameScreen);
	}

}
