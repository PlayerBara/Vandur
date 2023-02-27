package com.mygdx.game.screens;

import static com.mygdx.game.extra.Utils.*;
import static com.mygdx.game.extra.Utils.SCREEN_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.mygdx.game.actors.EnemyBullet;
import com.mygdx.game.actors.Explosion;
import com.mygdx.game.actors.PlayerBullet;
import com.mygdx.game.actors.Enemy;
import com.mygdx.game.actors.StarShip;
import com.mygdx.game.extra.Score;

public class GameScreen extends BaseScreen implements ContactListener {

    private World world;
    //Stage es un elemento que hace que pueda almacenar los datos de los actores desde el ultimo frame
    private Stage stage;
    private OrthographicCamera ortCamera;
    private Image background; //Imagen de fondo del juego
    private StarShip starShip;

    private float TIME_TO_SPAWN_ENEMY = 1.5f;
    private float timeToCreateEnemy;

    private float TIME_TO_SPAWN_ASTEROID = 5F;
    private float timeToCreateAsteroid;

    private static final float TIME_TO_SPAWN_PLAYERBULLET = 0.4F;
    private float timeToCreatePlayerBullet;

    private static final float TIME_TO_FINISHGAME = 180F;
    private float timeToFinishGame;
    private float timeEffect;

    private Array<Enemy> numEnemies;
    private Array<PlayerBullet> numPlayerBullets;
    private Array<Asteroid> numAsteroids;
    private Array<Explosion> numExplosions;
    private Array<EnemyBullet> numEnemyBullets;

    private boolean level1;
    private boolean level2;
    private boolean level3;
    private boolean level4;

    public boolean win;

    //Sonidos y musica
    private Sound shootSound;
    private Sound explosionSound;
    private Music music;

    //Score y su camara
    public Score score;
    private OrthographicCamera fontCamera;
    private BitmapFont scoreFont;

    //Constructor de la pantalla del juego
    public GameScreen(VandurGame vandurGame) {
        super(vandurGame);

        this.world = new World(new Vector2(0, -10), true);
        this.world.setContactListener(this);
        //Se define el tamaño del mundo
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        //Añade el tamaño del mundo al stage
        this.stage = new Stage(fitViewport);

        //ArrayList que se utiliza para identificar a los actores de ciertos grupos
        this.numEnemies = new Array<>();
        this.numPlayerBullets = new Array<>();
        this.numAsteroids = new Array<>();
        this.numExplosions = new Array<>();
        this.numEnemyBullets = new Array<>();

        //Se le añade el tiempo al efecto de tiempo que se utilizara para mostrarlo por pantalla
        this.timeEffect = TIME_TO_FINISHGAME;

        //Se preparan los sonidos
        this.music = this.vandurGame.assetmanager.getMusic();
        this.shootSound = this.vandurGame.assetmanager.getSoundShoot();
        this.explosionSound = this.vandurGame.assetmanager.getSoundExplosion();

        //Se crea la camara
        this.ortCamera = (OrthographicCamera) this.stage.getCamera();

        //Se declaran los niveles a false, para que despues se pueda acceder a ellos
        this.level1 = false;
        this.level2 = false;
        this.level3 = false;
        this.level4 = false;

        //Se declara win
        this.win = true;

        //Este metodo prepara las fuentes
        prepareFonts();
    }

    //Este metodo añade la imagen del mundo
    public void addBackground(){
        //Obtiene la imagen del mapa
        this.background = new Image(vandurGame.assetmanager.getBackground());
        //Da el punto donde se coloca la imagen del mundo
        this.background.setPosition(0,0);
        //Indica el tamaño que tendra la imagen del mundo
        this.background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        //Añade el fondo al stage
        this.stage.addActor(this.background);
    }

    //IU (Interfaz de usuario)
    private void prepareFonts(){
        //Inicializa la clase Score
        this.score = new Score();
        //Se obtiene las fuentes necesarias
        this.scoreFont = this.vandurGame.assetmanager.getScoreFont();
        this.scoreFont.getData().scale(1);

        //Se crea la camara para la fuente
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();

    }

    //StarShip
    private void addStarShip(){
        //Obtiene la textura de la nave del jugador
        TextureRegion starShipSprite = vandurGame.assetmanager.getStarShipTR();
        //Obtiene el Sprite de la animación de la nave del jugador y le añade una posicion de inicio con vector2
        this.starShip = new StarShip(this.world, starShipSprite, new Vector2(Float.valueOf(WORLD_WIDTH)/2, 1f));
        //Añade el actor al stage
        this.stage.addActor(starShip);
    }

    //Metodo que se utiliza para mover la nave
    private void moveStarShip(){
        //Variable que se utilizara para ajustar el centro de la nave
        float centroNave = this.starShip.SPACESHIP_WIDTH / 2;

        //Si se esta tocando la pantalla...
        if(Gdx.input.isTouched()){
            //Se obtiene las coordenadas donde se esta tocando
            Vector3 coordenadas = new Vector3 (Gdx.input.getX(), 1, 0);
            //Se descomprime la camara
            ortCamera.unproject(coordenadas);
            //Si la coordenada donde toca esta a la derecha de la nave y la nave no sale de la pantalla entonces lo mueve
            if((coordenadas.x >= (this.starShip.getX() + centroNave + 0.07f)) && this.starShip.getX() <= WORLD_WIDTH - this.starShip.SPACESHIP_WIDTH){
                this.starShip.move(0);
            }else{
                //Si la coordenada donde se toca la pantalla esta a la izquierda y la nave no sale de la pantalla entonces lo mueve a la izquierda
                if((coordenadas.x <= (this.starShip.getX() + centroNave - 0.07f)) && this.starShip.getX() >= 0){
                    this.starShip.move(1);
                }else{
                    //Si nada de eso ocurre se para
                    this.starShip.move(2);
                }
            }
        }else{
            this.starShip.move(2);
        }
    }

    //Chequea si la partida a finalizado
    private void checkEndGame(float delta){

        //Añade el tiempo trascurrido
        this.timeToFinishGame+=delta;

        //Le resta el tiempo trascurrido para luego mostrar el tiempo faltante por pantalla
        this.timeEffect-=delta;

        //Si el timeToFinish es mayor a TIME_TO_FINISHGAME en
        if(this.timeToFinishGame >= TIME_TO_FINISHGAME) {

            //Borra los actories
            removeActors();

            //Para la musica
            this.music.stop();

            //Cambia de pantalla
            vandurGame.setScreen(vandurGame.gameEndScreen);

        }else{
            //Si no termina el tiempo chequea la vida, si no esta vivo entonces...
            if(!starShip.checkLife()){
                //Win es falso
                win = false;

                //Borra los actores
                removeActors();

                //Para la musica
                this.music.stop();

                //Cambia la pantalla
                vandurGame.setScreen(vandurGame.gameEndScreen);
            }
        }
    }

    //Borra todos los actores mediante foreachs
    private void removeActors(){
        for(Enemy enemy:numEnemies){
            removeEnemy(enemy);
        }
        for (EnemyBullet enemyBullet:numEnemyBullets){
            removeEnemyBullet(enemyBullet);
        }
        for (Asteroid asteroid:numAsteroids){
            removeAsteroid(asteroid);
        }
    }

    //Enemy

    //Añade un enemigo
    public void addEnemy(float delta){

        //Obtiene la textura del enemigo
        TextureRegion enemySprite = vandurGame.assetmanager.getEnemyTR();

        //Añade tiempo para que se cree un enemigo
        this.timeToCreateEnemy+=delta;

        //Si el tiempo llega a cierto limite entocnes...
        if(this.timeToCreateEnemy >= TIME_TO_SPAWN_ENEMY) {

            //Reinicia el tiempo para que se genere un enemigo
            this.timeToCreateEnemy-=TIME_TO_SPAWN_ENEMY;
            //Añade una posición aleatoria donde se genera el enemigo
            float posRandomX = MathUtils.random(0.5f, 4.5f);

            //Se crea el enemigo
            Enemy enemy = new Enemy(this.world, enemySprite, new Vector2(posRandomX, WORLD_HEIGHT + 1));
            //Se añade al arrayList
            numEnemies.add(enemy);
            //Se añade al Stage
            this.stage.addActor(enemy);
        }
    }

    //Checkea el enemigo si tiene vida o si esta fuera de pantalla
    public void checkEnemy(){
        //Pasa por el el bucle todos los enemigos guardados en el array
        for(Enemy enemy:this.numEnemies){

            //Si el mundo no esta bloqueado entonces...
            if(!world.isLocked()){
                //Si el enemigo esta fuera de pantalla entonces...
                if(enemy.isOutOfScreen()){
                    //Elimina el enemigo
                    removeEnemy(enemy);
                }else{
                    //Si el enemigo esta mucerto entonces
                    if(enemy.checkLife()){
                        //Hace sonar el sonido de explosion
                        this.explosionSound.play();
                        //Añade la explosión
                        addExplosion(1, enemy.getX(), enemy.getY());
                        //Elimina el enemigo
                        removeEnemy(enemy);

                        //Suma puntos
                        this.score.sumPoints(25);
                        //suma un al combo
                        this.score.sumCombo();

                    }
                }
            }
        }
    }

    //Borra el enemigo
    private void removeEnemy(Enemy enemy){
        enemy.detach();
        enemy.remove();
        numEnemies.removeValue(enemy, false);
    }

    //Asteroid
    //Añade
    public void addAsteroid (float delta){

        //Añade tiempo para que se cree un asteroide
        this.timeToCreateAsteroid += delta;

        //Si el tiempo llega a cierto limite entocnes...
        if(this.timeToCreateAsteroid >= TIME_TO_SPAWN_ASTEROID){
            //Reinicia el tiempo para que se genere un asteroide
            this.timeToCreateAsteroid -= TIME_TO_SPAWN_ASTEROID;

            //Añade una posición aleatoria donde se genera el asteroide
            float posX = MathUtils.random(1f, 4f);
            //Se crea el asteroide
            Asteroid asteroid = new Asteroid(this.world, vandurGame.assetmanager.getAsteroidTR(), new Vector2(posX, WORLD_HEIGHT + 1));
            //Se añade el asteroide al arrayList
            numAsteroids.add(asteroid);
            //Se añade el actor al asteroide
            this.stage.addActor(asteroid);
        }
    }

    //Se chequea el estado del asteroide, si esta "Vivo" o si esta fuera de pantalla
    public void checkAsteroidInScreen(){
        //Usa un foreach para comprobar todos los asteroides
        for(Asteroid asteroid:this.numAsteroids){

            //Si el mundo no esta bloqueado entonces...
            if(!world.isLocked()){
                //Si el asteroide esta fuera de pantalla entonces...
                if(asteroid.isOutOfScreen()){
                    //Elimina el asteroide
                    removeAsteroid(asteroid);
                }else{
                    //chequea la vida del asteroide
                    if(asteroid.checkLife()){
                        //Elimina el asteroide
                        removeAsteroid(asteroid);
                        //Añade una explosion
                        addExplosion(1.5f, asteroid.getX(), asteroid.getY());
                        //ejecuta el sonido de explosion
                        this.explosionSound.play();

                        //Suma los puntos y el combo
                        this.score.sumPoints(40);
                        this.score.sumCombo();
                    }
                }
            }
        }
    }

    //Borra el asteroide
    private void removeAsteroid(Asteroid asteroid){
        asteroid.detach();
        asteroid.remove();
        numAsteroids.removeValue(asteroid, false);
    }

    //PlayerBullets
    public void addPlayerBullet(float delta){

        //Añade tiempo a esta propiedad
        this.timeToCreatePlayerBullet += delta;

        //Si timeToCreatePlayerBullet es igual al tiempo asignado entonces...
        if(this.timeToCreatePlayerBullet >= TIME_TO_SPAWN_PLAYERBULLET){
            //Se resetea el tiempo
            this.timeToCreatePlayerBullet -= TIME_TO_SPAWN_PLAYERBULLET;

            //Se genera la PlayerBullet y se añade al array y al stage
            PlayerBullet eb = new PlayerBullet(this.world, vandurGame.assetmanager.getPlayerBulletTR(), new Vector2(starShip.getX() + 0.25f, starShip.getY() + 0.5f));
            numPlayerBullets.add(eb);

            this.stage.addActor(eb);

            //Se acciona la musica de disparo
            this.shootSound.play();

        }
    }

    //Chequea si la bala del jugador esta viva o fuera del mapa
    public void checkPlayerBullet(){
        for(PlayerBullet bullet:this.numPlayerBullets){
            if(!world.isLocked()){
                if(bullet.isOutOfScreen()){
                    removePlayerBullet(bullet);
                }else{
                    if(bullet.checkLife()){
                        addExplosion(0.4f, bullet.getX(), bullet.getY());

                        removePlayerBullet(bullet);
                    }
                }
            }
        }
    }

    //Borra la bala del jugador
    private void removePlayerBullet(PlayerBullet bullet){
        bullet.detach();
        bullet.remove();
        numPlayerBullets.removeValue(bullet, false);
    }


    //EnemyBullet
    //Añade la bala del enemigo
    public void addEnemyBullet(float delta){

        for(Enemy enemy:numEnemies){
            if(enemy.canShoot(delta)){
                Vector2 pos = new Vector2(enemy.getX() + 0.25f, enemy.getY());
                EnemyBullet enemyBullet = new EnemyBullet(this.world, vandurGame.assetmanager.getEnemyBulletTR(), pos);
                numEnemyBullets.add(enemyBullet);
                this.stage.addActor(enemyBullet);
            }
        }

    }

    //Chequea si el actor esta "vivo" o si esta fuera de los limites del mundo
    private void checkEnemyBullet(){
        for(EnemyBullet bullet:this.numEnemyBullets){
            if(!world.isLocked()){
                if(bullet.isOutOfScreen()){
                    removeEnemyBullet(bullet);
                }else{
                    if(bullet.checkLife()){
                        addExplosion(0.4f, bullet.getX(), bullet.getY());

                        removeEnemyBullet(bullet);
                    }
                }
            }
        }
    }

    //Borra la bala
    private void removeEnemyBullet(EnemyBullet bullet){
        bullet.detach();
        bullet.remove();
        numEnemyBullets.removeValue(bullet, false);
    }


    //Explosion
    //Añade una explosion
    private void addExplosion(float size, float posX, float posY){
        Animation<TextureRegion> animationExplosion = vandurGame.assetmanager.getExplosionAnimation();
        Vector2 pos = new Vector2(posX + 0.4f, posY);
        Explosion explosion = new Explosion(this.world, animationExplosion, pos, size);
        numExplosions.add(explosion);
        this.stage.addActor(explosion);

    }

    //Limpia la explosión
    private void cleanExplosion(float delta){
        for(Explosion explosion:numExplosions){
            if(explosion.checkAnimation(delta)){
                explosion.detach();
                explosion.remove();
                numExplosions.removeValue(explosion, false);
            }
        }
    }

    //El render hace que se repite la accion segun los frames por segundo
    @Override
    public void render(float delta){

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Actualiza la camara
        ortCamera.update();

        moveStarShip();

        addPlayerBullet(delta);
        addAsteroid(delta);
        addEnemy(delta);
        addEnemyBullet(delta);

        checkEndGame(delta);

        this.stage.act();
        this.world.step(delta, 6, 2);
        this.stage.draw();

        cleanExplosion(delta);

        incrementDifficulty();

        checkEnemy();
        checkEnemyBullet();
        checkPlayerBullet();
        checkAsteroidInScreen();

        //Estos stage se utilizan para mostrar la fuente y los datos del juego
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.scoreFont.draw(this.stage.getBatch(), "POINTS "+this.score.getPoints(),(SCREEN_WIDTH * 0.05f), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100) * 2));
        this.scoreFont.draw(this.stage.getBatch(), "X "+this.score.getMultiplicador(),(SCREEN_WIDTH * 0.05f), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100) * 6));
        this.scoreFont.draw(this.stage.getBatch(), "COMBO "+this.score.getCombo(),(SCREEN_WIDTH * 0.05f), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100) * 10));
        this.scoreFont.draw(this.stage.getBatch(), "LIFE "+this.starShip.getLife(),(SCREEN_WIDTH * 0.05f), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100) * 14));
        this.scoreFont.draw(this.stage.getBatch(), "SECONDS " + (int) this.timeEffect,(SCREEN_WIDTH * 0.55f), SCREEN_HEIGHT - ((SCREEN_HEIGHT/100) * 2));


        this.stage.getBatch().end();

    }

    //Muestra la imagen del mundo y la del actor(nave espacial del jugador)
    @Override
    public void show(){
        this.music.setLooping(true);

        this.music.play();

        addBackground();
        addStarShip();
    }

    //oculta los actores
    public void hide(){
        removeActors();
        this.starShip.detach();
        this.starShip.remove();
    }

    //Elimina los actores
    public void dispose(){
        this.stage.dispose();
        this.world.dispose();
    }

    //Metodo utilizado para añadir dificultad en base a la puntuación
    //Si pasa de nivel ese nivel pasa a ser true para que no se repita el efecto de la dificultad
    private void incrementDifficulty(){
        if(this.score.getPoints() >= 12000 && !level4){
            TIME_TO_SPAWN_ENEMY -= 0.1f;

            TIME_TO_SPAWN_ASTEROID -= 0.2f;

            level4 = true;

        }else{
            if(this.score.getPoints() >= 7500 && !level3){
                TIME_TO_SPAWN_ENEMY -= 0.1f;

                TIME_TO_SPAWN_ASTEROID -= 0.2f;

                level3 = true;

            }else{
                if(this.score.getPoints() >= 3000 && !level2){
                    TIME_TO_SPAWN_ENEMY -= 0.1f;

                    TIME_TO_SPAWN_ASTEROID -= 0.2f;

                    level2 = true;

                }else{
                    if(this.score.getPoints() >= 1000 && !level1){
                        TIME_TO_SPAWN_ENEMY -= 0.1f;

                        TIME_TO_SPAWN_ASTEROID -= 0.2f;

                        level1 = true;

                    }else{

                    }
                }
            }
        }
    }
    //COLISIONES
    //Chequea las colisiones de dos objetos
    private boolean checkCollision(Contact contact, Object objA, Object objB) {

        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }

    //Este metodo comprueba las colisiones de todos los objetos mediante foreach
    @Override
    public void beginContact(Contact contact) {

        for(PlayerBullet playerBullet: numPlayerBullets){
            for(Asteroid asteroid:numAsteroids){
                if(checkCollision(contact, asteroid.getUserData(), playerBullet.getUserData() )){

                    asteroid.subtracLife();
                    playerBullet.subtracLife();

                }else{
                    if(checkCollision(contact, asteroid.getUserData(), starShip.getUserData())){

                        asteroid.subtracLife();
                        this.score.removeCombo();
                        this.starShip.subtrackLife();

                    }
                }
            }

            for(Enemy enemy:numEnemies){
                if(checkCollision(contact, enemy.getUserData(), playerBullet.getUserData())){

                    enemy.subtracLife(1);
                    playerBullet.subtracLife();

                }else{
                    if(checkCollision(contact, enemy.getUserData(), starShip.getUserData())){

                        enemy.subtracLife(4);

                        this.score.removeCombo();
                        this.starShip.subtrackLife();

                    }
                }
            }
        }

        for(EnemyBullet enemyBullet:numEnemyBullets){
            if(checkCollision(contact, enemyBullet.getUserData(), starShip.getUserData())){

                enemyBullet.subtracLife();

                this.score.removeCombo();

                this.starShip.subtrackLife();

            }
        }

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