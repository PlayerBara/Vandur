package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.extra.AssetMan;
import com.mygdx.game.screens.GameEndScreen;
import com.mygdx.game.screens.GameReadyScreen;
import com.mygdx.game.screens.GameScreen;

public class VandurGame extends Game {

	public GameReadyScreen gameReadyScreen;
	public GameScreen gameScreen;
	public GameEndScreen gameEndScreen;

	public AssetMan assetmanager;

	@Override
	public void create(){
		this.assetmanager = new AssetMan();

		this.gameReadyScreen = new GameReadyScreen(this);
		//this.gameScreen = new GameScreen(this);
		this.gameEndScreen = new GameEndScreen(this);

		setScreen(this.gameReadyScreen);

	}
}
