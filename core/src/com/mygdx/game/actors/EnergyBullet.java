package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.extra.Utils.WORLD_WIDTH;

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

public class EnergyBullet extends Actor {

    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion ebTR;

    //Elementos para facil acceso
    private static final float ANCHO = 0.5f;
    private static final float ALTO = 0.5f;
    private static final float VELOCIDAD = 8f;

    //Constructor
    public EnergyBullet(World world, TextureRegion ebTR, Vector2 position){
        this.world = world;
        this.ebTR = ebTR;

        createBody(position);
        createFixture();
    }

    private void createBody(Vector2 position){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(position);

        bodyDef.type = BodyDef.BodyType.KinematicBody;
        this.body = this.world.createBody(bodyDef);
        body.setUserData(Utils.USER_ENERGYBULLET);
        body.setLinearVelocity(0, VELOCIDAD);
    }

    private void createFixture(){
        CircleShape circle = new CircleShape();
        circle.setRadius(0.07f);

        this.fixture = this.body.createFixture(circle, -10);
        this.fixture.setUserData(Utils.USER_ENERGYBULLET);
        circle.dispose();
    }

    public boolean isOutOfScreen(){
        return this.body.getPosition().y <= -2f || this.body.getPosition().y >= (WORLD_HEIGHT + 1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-(ANCHO / 2), body.getPosition().y-(ALTO / 2));
        batch.draw(this.ebTR, getX(), getY(), ANCHO, ALTO);
    }

    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }
}
