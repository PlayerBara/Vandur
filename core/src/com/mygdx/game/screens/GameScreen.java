package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.VandurGame;
import com.mygdx.game.actors.Asteroid;
import com.mygdx.game.actors.EnergyBullet;
import com.mygdx.game.actors.Enemy;
import com.mygdx.game.actors.StarShip;

public class GameScreen extends BaseScreen implements ContactListener {

    private World world;
    //Stage es un elemento que hace que pueda almacenar los datos de los actores desde el ultimo frame
    private Stage stage;
    private OrthographicCamera ortCamera;
    private Box2DDebugRenderer debugRenderer;
    private Image background; //Imagen de fondo del juego
    private StarShip starShip;

    private  static final float TIME_TO_SPAWN_ENEMY = 1f;
    private float timeToCreateEnemy;

    private static final float TIME_TO_SPAWN_ASTEROID = 1F;
    private float timeToCreateAsteroid;

    private static final float TIME_TO_SPAWN_ENERGYBULLET = 0.5F;
    private float timeToCreateEnergyBullet;

    private Array<Enemy> numEnemies;
    private Array<EnergyBullet> numBullets;
    private Array<Asteroid> numAsteroids;

    //Sonidos y musica
    private Sound shootSound;
    private Music music;

    //Constructor de la pantalla del juego
    public GameScreen(VandurGame vandurGame) {
        super(vandurGame);

        this.world = new World(new Vector2(0, -10), true);
        //Se define el tamaño del mundo
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        this.stage = new Stage(fitViewport);

        this.numEnemies = new Array<>();
        this.numBullets = new Array<>();
        this.numAsteroids = new Array<>();

        this.world.setContactListener(this);

        this.music = this.vandurGame.assetmanager.getMusic();
        this.shootSound = this.vandurGame.assetmanager.getSoundShoot();

        //TEST
        //ortCamera.setToOrtho(false, WORLD_HEIGHT, WORLD_WIDTH);
        //----

        this.ortCamera = (OrthographicCamera) this.stage.getCamera();

        this.debugRenderer = new Box2DDebugRenderer();


    }

    //Este metodo añade la imagen del mundo
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

    //StarShip
    public void addStarShip(){
        //Obtiene la textura de la nave del jugador
        TextureRegion starShipSprite = vandurGame.assetmanager.getStarShipTR();
        //Obtiene el Sprite de la animación de la nave del jugador y le añade una posicion de inicio con vector2
        this.starShip = new StarShip(this.world, starShipSprite, new Vector2(Float.valueOf(WORLD_WIDTH)/2, 1f));
        //Añade el actor al stage
        this.stage.addActor(starShip);
    }

    private void moveStarShip(){

        float centroNave = this.starShip.SPACESHIP_WIDTH / 2;

        if(Gdx.input.isTouched()){
            Vector3 coordenadas = new Vector3 (Gdx.input.getX(), 1, 0);
            ortCamera.unproject(coordenadas);
            if((coordenadas.x > (this.starShip.getX() + centroNave)) && this.starShip.getX() <= WORLD_WIDTH - this.starShip.SPACESHIP_WIDTH){
                this.starShip.move(0);
            }else{
                if(coordenadas.x < (this.starShip.getX() + centroNave) && this.starShip.getX() >= 0){
                    this.starShip.move(1);
                }else{
                    this.starShip.move(2);
                }
            }
        }else{
            this.starShip.move(2);
        }
    }

    //Enemy
    public void addEnemy(float delta){

        TextureRegion enemySprite = vandurGame.assetmanager.getEnemyTR();

        this.timeToCreateEnemy+=delta;

        if(this.timeToCreateEnemy >= TIME_TO_SPAWN_ENEMY) {

            this.timeToCreateEnemy-=TIME_TO_SPAWN_ENEMY;
            float posRandomX = MathUtils.random(0f, 5f);

            Enemy enemy = new Enemy(this.world, enemySprite, new Vector2(posRandomX, WORLD_HEIGHT + 1)); //Posición de la tubería inferior
            numEnemies.add(enemy);
            this.stage.addActor(enemy);
        }
    }

    public void removeEnemy(){
        for(Enemy enemy:this.numEnemies){

            if(!world.isLocked()){
                if(enemy.isOutOfScreen()){
                    enemy.detach();
                    enemy.remove();
                    numEnemies.removeValue(enemy, false);
                }
            }
        }
    }

    //Asteroid
    public void addAsteroid (float delta){
        this.timeToCreateAsteroid += delta;

        if(this.timeToCreateAsteroid >= TIME_TO_SPAWN_ASTEROID){
            this.timeToCreateAsteroid -= TIME_TO_SPAWN_ASTEROID;

            float posX = MathUtils.random(1f, 4f);
            Asteroid asteroid = new Asteroid(this.world, vandurGame.assetmanager.getAsteroidTR(), new Vector2(posX, WORLD_HEIGHT + 1));
            numAsteroids.add(asteroid);
            this.stage.addActor(asteroid);
        }
    }

    public void removeAsteroid(){
        for(Asteroid asteroid:this.numAsteroids){

            if(!world.isLocked()){
                if(asteroid.isOutOfScreen()){
                    asteroid.detach();
                    asteroid.remove();
                    numAsteroids.removeValue(asteroid, false);
                }
            }
        }
    }

    //EnergyBullets
    public void addEnergyBullet (float delta){


        this.timeToCreateEnergyBullet += delta;

        if(this.timeToCreateEnergyBullet >= TIME_TO_SPAWN_ENERGYBULLET){
            this.timeToCreateEnergyBullet -= TIME_TO_SPAWN_ENERGYBULLET;

            EnergyBullet eb = new EnergyBullet(this.world, vandurGame.assetmanager.getEnergyBulletTR(), new Vector2(starShip.getX() + 0.25f, starShip.getY() + 0.5f));
            numBullets.add(eb);

            this.stage.addActor(eb);

            this.shootSound.play();

        }
    }

    public void removeEnergyBullet(){
        for(EnergyBullet bullet:this.numBullets){

            if(!world.isLocked()){
                if(bullet.isOutOfScreen()){
                    bullet.detach();
                    bullet.remove();
                    numBullets.removeValue(bullet, false);
                }
            }
        }
    }

    private void shoot(float delta){
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            addEnergyBullet(delta);
        }
    }

    //El render hace que se repite la accion segun los frames por segundo
    @Override
    public void render(float delta){

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ortCamera.update();

        //Mover nave espacial
        moveStarShip();

        //shoot(delta);
        addEnergyBullet(delta);
        addAsteroid(delta);
        //addEnemy(delta);

        removeEnemy();
        removeEnergyBullet();
        removeAsteroid();

        //
        this.stage.act();
        //
        this.world.step(delta, 6, 2);
        //
        this.stage.draw();
        //
        this.debugRenderer.render(this.world, this.ortCamera.combined);
    }

    //Muestra la imagen del mundo y la del actor(nave espacial del jugador)
    @Override
    public void show(){
        this.music.setLooping(true);
        this.music.play();

        addBackground();
        addStarShip();
    }

    public void hide(){
        this.starShip.detach();
        this.starShip.remove();
    }

    public void dispose(){
        this.stage.dispose();
        this.world.dispose();
    }

    //COLISIONES
    private boolean areColider(Contact contact, Object objA, Object objB) {

        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }

    private boolean colisionAsteroid(Contact contact, Asteroid asteroid, EnergyBullet energyBullet){

        return (contact.getFixtureA().getBody().equals(asteroid) && contact.getFixtureB().getBody().equals(energyBullet)) ||
                (contact.getFixtureA().getBody().equals(energyBullet) && contact.getFixtureB().getBody().equals(asteroid));
    }

    @Override
    public void beginContact(Contact contact) {

        //Crear un userdata para cada objeto

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}