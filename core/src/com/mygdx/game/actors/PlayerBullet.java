package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.extra.Utils;

public class PlayerBullet extends Actor {

    //Se crea las propiedades necesarias
    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion pbTR;

    //Se crea propiedades para gestionar los datos del actor
    private static int count = 0;
    private String id;
    private int life = 1;

    //Elementos para facil acceso
    private static final float ANCHO = 0.3f;
    private static final float ALTO = 0.3f;
    private static final float VELOCIDAD = 8f;

    //Constructor
    public PlayerBullet(World world, TextureRegion pbTR, Vector2 position){
        //Se obtiene el mundo
        this.world = world;
        //Se obtiene el sprite
        this.pbTR = pbTR;

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
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        this.body = this.world.createBody(bodyDef);

        //Añade velocidad al objeto
        body.setLinearVelocity(0, VELOCIDAD);
    }

    //Metodo para crear el hitbox
    private void createFixture(){
        //Crea un hitbox circular
        CircleShape circle = new CircleShape();
        circle.setRadius(0.07f);

        //Añade el hitbox al cuerpo
        this.fixture = this.body.createFixture(circle, -10);
        addUserData();

        //Borra el circulo, ya que no es necesario
        circle.dispose();
    }

    //metodo para crear un uderdata personalizado
    private void addUserData(){
        //Crea un string auxiliar para el id, el cual se le añade un contador para personalizarlo
        id = Utils.USER_PLAYERBULLET + count;
        //Se le añade el string auxiliar
        fixture.setUserData(id);
        //Se le suma 1 al contador
        count ++;
        //Si el contador llega a 50 se reinicia
        if(count >= 50){
            count = 0;
        }
    }

    //Obtiene el userdata
    public String getUserData(){
        return id;
    }

    //Comprueba si esta fuera de la pantalla
    public boolean isOutOfScreen(){
        return this.body.getPosition().y <= -2f || this.body.getPosition().y >= (WORLD_HEIGHT + 1f);
    }

    //Resta una vida a este actor
    public void subtracLife(){
        life --;
    }

    //Comprueba si esta vivo este actor
    public boolean checkLife(){
        boolean dead = false;
        if(life <= 0){
            dead = true;
        }
        return dead;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-(ANCHO / 2), body.getPosition().y-(ALTO / 2));
        batch.draw(this.pbTR, getX(), getY(), ANCHO, ALTO);
    }

    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }
}
