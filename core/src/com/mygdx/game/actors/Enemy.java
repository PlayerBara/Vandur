package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.USER_ENEMY;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class Enemy extends Actor {

    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion enemySprite;

    //Elementos para facil acceso
    private static final float LARGO = 0.5f;
    private static final float ANCHO = 0.5f;
    private static final float VELOCIDAD = 3f;
    private static final float GRAVEDAD = 0.05f;

    //Constructor
    public Enemy(World world, TextureRegion enemySprite, Vector2 position){
        this.world = world;
        this.enemySprite = enemySprite;

        createBody(position);
        createFixture();
    }

    private void createBody(Vector2 position){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(position);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        this.body = this.world.createBody(bodyDef);
        body.setUserData(USER_ENEMY);
        //this.body.setLinearVelocity(0, -VELOCIDAD);
        this.body.setGravityScale(GRAVEDAD);
    }

    private void createFixture(){
        CircleShape circle = new CircleShape();
        circle.setRadius(0.27f);

        this.fixture = this.body.createFixture(circle, 8);
        this.fixture.setUserData(USER_ENEMY);

        circle.dispose();
    }

    public boolean isOutOfScreen(){
        return this.body.getPosition().y <= -2f;
    }

    //Dibuja al actor
    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-(ANCHO/2), body.getPosition().y-(LARGO/2));

        batch.draw(this.enemySprite, getX(), getY(), ANCHO, LARGO);

        /*
        batch.draw(this.enemyAnimation.getKeyFrame(stateTime, true), getX(), getY(), ancho, largo);

        stateTime += Gdx.graphics.getDeltaTime();

         */
    }

    //
    public void detach(){
        //Destruye el fixture del objeto
        this.body.destroyFixture(this.fixture);
        //Destruye el cuerpo del objeto
        this.world.destroyBody(this.body);
    }
}
