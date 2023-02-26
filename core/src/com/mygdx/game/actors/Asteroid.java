package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.extra.Utils;

public class Asteroid extends Actor {

    //Se crea las propiedades necesarias
    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion asteroidTR;

    //Se crea propiedades para gestionar los datos del actor
    private static int count = 0;
    private String id;
    private int lives = 7;

    //Elementos para facil acceso
    private static final float ALTO = 1;
    private static final float ANCHO = 1;
    private static final float GRAVEDAD = 0.01f;
    private static final float VELOCIDAD = -1f;


    //Constructor
    public Asteroid (World world, TextureRegion asteroidTR, Vector2 position){
        //Se obtiene el mundo
        this.world = world;
        //Se obtiene el sprite
        this.asteroidTR = asteroidTR;

        //Metodos para crear el cuerpo y la hitbox
        createBody(position);
        createFixture();
    }

    //Metodo para crear el cuerpo
    private void createBody(Vector2 position){
        //Se inicializa el cuerpo
        BodyDef bodyDef = new BodyDef();

        //Se obtiene la posicion donde se creara el cuerpo
        bodyDef.position.set(position);

        //Le da al cuerpo el tipo kinematico para que pueda moverse sin que le afecte la física
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = this.world.createBody(bodyDef);

        //Añade velocidad al objeto
        body.setGravityScale(GRAVEDAD);
        float velX = MathUtils.random(-0.3f, 0.3f);

        body.setLinearVelocity(velX, VELOCIDAD);
    }

    //Metodo para crear el hitbox
    private void createFixture(){
        //Crea un hitbox circular
        CircleShape circle = new CircleShape();
        circle.setRadius(0.35f);

        //Añade el hitbox al cuerpo
        this.fixture = this.body.createFixture(circle, -100);
        this.fixture.setSensor(true);
        addUserData();

        //Borra el circulo, ya que no es necesario
        circle.dispose();
    }

    //metodo para crear un uderdata personalizado
    private void addUserData(){
        //Crea un string auxiliar para el id, el cual se le añade un contador para personalizarlo
        id = Utils.USER_ASTEROID + count;
        fixture.setUserData(id);
        count ++;
        if(count >= 50){
            count = 0;
        }
    }

    //Obtiene el userdata
    public String getUserData(){
        return id;
    }

    //Resta una vida a este actor
    public void subtracLife(){
        lives --;
    }

    //Comprueba si esta vivo este actor
    public boolean checkLife(){
        boolean dead = false;
        if(lives <= 0){
            dead = true;
        }
        return dead;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-(ANCHO/2), body.getPosition().y-(ALTO/2));
        batch.draw(this.asteroidTR, getX(), getY(), ALTO, ANCHO);
    }

    //Comprueba si esta fuera de la pantalla
    public boolean isOutOfScreen(){
        return this.body.getPosition().y <= -2f
                || this.body.getPosition().y > WORLD_HEIGHT + 3
                || this.body.getPosition().x < - 2
                || this.body.getPosition().x > WORLD_HEIGHT + 2;
    }

    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }
}
