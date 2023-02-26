package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.USER_ENEMY;

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

public class Enemy extends Actor {

    //Se crea las propiedades básicas necesarias
    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion enemySprite;

    //Se crea propiedades para gestionar los datos del actor
    private static int count = 0;
    private String id;
    private int life = 3;

    //Elementos para facil acceso
    private static final float LARGO = 0.5f;
    private static final float ANCHO = 0.5f;
    private static final float VELOCIDAD = -2f;
    private static final float GRAVEDAD = 0f;
    private float time_to_shoot;
    private float aux_shoot;

    //Constructor
    public Enemy(World world, TextureRegion enemySprite, Vector2 position){
        //Se obtiene el mundo
        this.world = world;
        //Se obtiene el sprite
        this.enemySprite = enemySprite;
        //Crea al azar el tiempo en el que disparara la bala
        this.time_to_shoot = MathUtils.random(1.75f, 2.25f);

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

        //Le da al cuerpo el tipo dinamico para que pueda afectarle elementos de las fisicas
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = this.world.createBody(bodyDef);

        //Se genera una velocidad y se cambia la gravedad del actor
        this.body.setLinearVelocity(0, VELOCIDAD);
        this.body.setGravityScale(GRAVEDAD);
    }

    //Metodo para crear el hitbox
    private void createFixture(){
        //Crea un hitbox circular
        CircleShape circle = new CircleShape();
        circle.setRadius(0.27f);

        //Añade el hitbox al cuerpo
        this.fixture = this.body.createFixture(circle, 0);
        this.fixture.setSensor(true);
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
        return this.body.getPosition().y <= -2f;
    }

    //Resta una vida a este actor
    public void subtracLife(int damage){
        life -= damage;
    }

    //Comprueba si esta vivo este actor
    public boolean checkLife(){
        boolean dead = false;
        if(life <= 0){
            dead = true;
        }
        return dead;
    }

    public boolean canShoot(float delta){
        boolean shoot = false;
        this.aux_shoot += delta;

        if(this.aux_shoot >= this.time_to_shoot){
            this.aux_shoot -= this.time_to_shoot;
            shoot = true;
        }
        return shoot;
    }

    //Dibuja al actor
    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-(ANCHO/2), body.getPosition().y-(LARGO/2));

        batch.draw(this.enemySprite, getX(), getY(), ANCHO, LARGO);
    }

    public void detach(){
        //Destruye el fixture del objeto
        this.body.destroyFixture(this.fixture);
        //Destruye el cuerpo del objeto
        this.world.destroyBody(this.body);
    }
}
