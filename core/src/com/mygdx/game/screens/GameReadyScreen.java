package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.VandurGame;

public class GameReadyScreen extends BaseScreen{
    //Crea un mundo
    private World world;
    //Crea un stage para guardar los elementos en pantalla
    private Stage stage;
    //Crea una propiedad de fondo de pantalla
    private Image background;
    //Crea una camara para 2D
    private OrthographicCamera ortCamera;

    //Crea una música
    private Music music;

    //Crea una camara para las fuentes
    private OrthographicCamera fontCamera;
    //Crea las fuentes
    private BitmapFont menuFont;
    private BitmapFont menuSecondFont;
    private BitmapFont scoreFont;

    //Constructor
    public GameReadyScreen(VandurGame vandurGame) {
        super(vandurGame);

        this.world = new World(new Vector2(0, -10), true);

        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        this.stage = new Stage(fitViewport);

        this.music = this.vandurGame.assetmanager.getMusic();

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

    //Prepara las fuentes
    private void prepareFont(){
        this.menuFont = this.vandurGame.assetmanager.getMenuFont();
        this.menuFont.getData().scale(1);

        this.scoreFont = this.vandurGame.assetmanager.getScoreFont();
        this.scoreFont.getData().scale(1);

        this.menuSecondFont = this.vandurGame.assetmanager.getMenuSecondFont();
        this.menuSecondFont.getData().scale(1);

        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();
    }

    //Prepara el texto
    private void text(){
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();

        this.menuFont.draw(this.stage.getBatch(), "VANDUR" ,((SCREEN_WIDTH / 100)*20), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)*30));

        this.scoreFont.draw(this.stage.getBatch(), "Juego desarrollado por",((SCREEN_WIDTH / 100)*10), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)* 45));

        this.scoreFont.draw(this.stage.getBatch(), "Ivan Zuniga Vazquez",((SCREEN_WIDTH / 100)*15), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)* 50));

        this.menuSecondFont.draw(this.stage.getBatch(), "Toca para jugar" ,((SCREEN_WIDTH / 100)*10), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100)*75));
    }

    //Empieza el juego cuando se toca la pantalla
    private void starGame(){
        if(Gdx.input.justTouched()){
            vandurGame.setScreen(vandurGame.gameScreen = new GameScreen(vandurGame));
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        starGame();

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

        addBackground();
    }

    public void hide(){

    }

    public void dispose(){
        this.stage.dispose();
        this.world.dispose();
    }


}
