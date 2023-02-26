package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.USER_STARSHIP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StarShip extends Actor {

    //Se crea las propiedades necesarias
    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion starShipSprite;

    //Se crea propiedades para medir la vida de la nave
    private int life;
    private boolean isLife;

    //Elementos para facil acceso
    private static final float LARGO = 0.5f;
    public static final float SPACESHIP_WIDTH = 0.5f;
    private static final float VELOCIDAD = 9f;
    private String id;

    //Constructor
    public StarShip(World world, TextureRegion starShipSprite, Vector2 position) {
        //Se obtiene el mundo
        this.world = world;
        //Se obtiene el sprite
        this.starShipSprite = starShipSprite;

        //Se coloca la vida de la nave
        this.life = 3;
        //Crea un elemento para saber si esta vivo o muerto el jugador
        this.isLife = true;

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

        //Le da al cuerpo el tipo telematico para que pueda moverse sin que le afecte la física
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        this.body = this.world.createBody(bodyDef);
    }

    //Metodo para crear el hitbox
    private void createFixture(){
        //Crea un hitbox circular
        CircleShape circle = new CircleShape();
        circle.setRadius(0.27f);

        //Añade el hitbox al cuerpo
        this.fixture = this.body.createFixture(circle, 0);
        id = USER_STARSHIP;
        this.fixture.setUserData(id);

        //Borra el circulo, ya que no es necesario
        circle.dispose();

    }

    //Obtiene el userData
    public String getUserData(){
        return id;
    }

    //Mueve la nave
    public void move(int xMove){
        switch(xMove){
            case 0:
                //En el estado 0 se mueve a la derecha
                this.body.setLinearVelocity(VELOCIDAD, 0);
                break;
            case 1:
                //En el estado 1 se mueve a la izquierda
                this.body.setLinearVelocity(-VELOCIDAD, 0);
                break;
            default:
                //En el estado default para de moverse
                this.body.setLinearVelocity(0, 0);
                break;
        }
    }

    //Obtiene la vida
    public int getLife() {
        return life;
    }

    //Resta vida
    public void subtrackLife(){
        Gdx.input.vibrate(300);
        if(!(life <= 0)){
            life --;
        }
    }

    //Comprueba si la nave esta viva
    public boolean checkLife(){
        if(life <= 0){
            isLife = false;
        }
        return isLife;
    }

    //Dibuja al actor
    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-0.25f, body.getPosition().y-0.25f);

        batch.draw(this.starShipSprite, getX(), getY(), SPACESHIP_WIDTH, LARGO);
    }

    //Elimina el cuerpo y la fixture
    public void detach(){
        //Destruye el fixture del objeto
        this.body.destroyFixture(this.fixture);
        //Destruye el cuerpo del objeto
        this.world.destroyBody(this.body);
    }
}