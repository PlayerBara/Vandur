package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.VandurGame;

import java.net.Socket;

public class GameEndScreen extends BaseScreen{

    private World world;
    private Stage stage;
    private Image background;
    private OrthographicCamera ortCamera;

    private Music music;
    private Sound winSound;
    private Sound loseSound;

    private OrthographicCamera fontCamera;
    private BitmapFont menuFont;
    private BitmapFont scoreFont;


    public GameEndScreen(VandurGame vandurGame) {
        super(vandurGame);

        this.world = new World(new Vector2(0, -10), true);

        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        this.stage = new Stage(fitViewport);

        this.music = this.vandurGame.assetmanager.getMusic();
        this.winSound = this.vandurGame.assetmanager.getSoundWin();
        this.loseSound = this.vandurGame.assetmanager.getSoundLose();

        this.ortCamera = (OrthographicCamera) this.stage.getCamera();

        prepareFont();
    }

    public void addBackground(){
        //Obtiene la imagen del mapa
        this.background = new Image(vandurGame.assetmanager.getBackground());
        //Da el punto donde se coloca la imagen del mundo
        this.background.setPosition(0,0);
        //Indica el tamaño que tendra la imagen del mundo
        this.background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        //
        this.stage.addActor(this.background);
    }

    private void prepareFont(){
        this.menuFont = this.vandurGame.assetmanager.getMenuFont();
        this.menuFont.getData().scale(1);

        this.scoreFont = this.vandurGame.assetmanager.getScoreFont();
        this.scoreFont.getData().scale(1);

        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();
    }

    private void text(){
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();

        if(vandurGame.gameScreen.win){
            this.menuFont.draw(this.stage.getBatch(), "YOU WIN" ,((SCREEN_WIDTH / 100)*18), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)*30));
        }else{
            this.menuFont.draw(this.stage.getBatch(), "YOU LOSE" ,((SCREEN_WIDTH / 100)*12), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)*30));
        }
        this.scoreFont.draw(this.stage.getBatch(), "POINTS: " + vandurGame.gameScreen.score.getPoints() ,((SCREEN_WIDTH / 100)*25), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)* 45));

    }

    private void passGameScrean(){
        if(Gdx.input.justTouched()){
            vandurGame.setScreen(vandurGame.gameReadyScreen);
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        passGameScrean();

        ortCamera.update();

        this.stage.act();
        this.world.step(delta, 6, 2);
        this.stage.draw();

        text();

        this.stage.getBatch().end();

    }

    @Override
    public void show(){
        this.music.setLooping(true);

        soundEndGame();

        addBackground();
    }

    private void soundEndGame(){
        if(vandurGame.gameScreen.win){
            this.winSound.play();
        }else{
            this.loseSound.play();
        }
    }

    public void hide(){

    }

    public void dispose(){
        this.stage.dispose();
        this.world.dispose();
    }

    
}
