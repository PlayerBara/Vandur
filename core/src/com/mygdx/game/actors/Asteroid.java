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

public class Asteroid extends Actor {

    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion asteroidTR;

    private int state = 2;

    //Elementos para facil acceso
    private static final float ALTO = 1;
    private static final float ANCHO = 1;
    private static final float GRAVEDAD = 0.03f;

    private static int count = 0;

    //Constructor
    public Asteroid (World world, TextureRegion asteroidTR, Vector2 position){
        this.world = world;
        this.asteroidTR = asteroidTR;

        createBody(position);
        createFixture();
    }

    private void createBody(Vector2 position){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(position);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        this.body = this.world.createBody(bodyDef);

        body.setUserData(Utils.USER_ASTEROID + count);

        body.setGravityScale(GRAVEDAD);

        //body.setLinearVelocity(2, 0);
    }

    private void createFixture(){
        CircleShape circle = new CircleShape();
        circle.setRadius(0.35f);

        this.fixture = this.body.createFixture(circle, 0);
        this.fixture.setUserData(Utils.USER_ASTEROID);
        circle.dispose();
    }

    //Daño a objeto
    public void hit(){
        state --;
        if(state <= 0){

        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-(ANCHO/2), body.getPosition().y-(ALTO/2));
        batch.draw(this.asteroidTR, getX(), getY(), ALTO, ANCHO);
    }

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
