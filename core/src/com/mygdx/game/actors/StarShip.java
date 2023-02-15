package com.mygdx.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StarShip extends Actor {

    private World world;
    private Body body;
    private Fixture fixture;
    private TextureRegion starShipSprite;

    //Elementos para facil acceso
    private static final float LARGO = 0.5f;
    public static final float SPACESHIP_WIDTH = 0.5f;
    private static final float VELOCIDAD = 9f;

    //Constructor
    public StarShip(World world, TextureRegion starShipSprite, Vector2 position) {
        this.world = world;
        this.starShipSprite = starShipSprite;

        createBody(position);
        createFixture();
    }

    private void createBody(Vector2 position){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(position);

        bodyDef.type = BodyDef.BodyType.KinematicBody;

        this.body = this.world.createBody(bodyDef);
    }

    private void createFixture(){


        /*
        CircleShape circle = new CircleShape();
        circle.setRadius(0.27f);

        this.fixture = this.body.createFixture(circle, 0);
        this.fixture.setUserData(USER_STARSHIP);

        circle.dispose();
         */
    }

    public void move(int stateMove){
        if(stateMove == 0){
            this.body.setLinearVelocity(VELOCIDAD, 0);
        }else{
            if(stateMove == 1){
                this.body.setLinearVelocity(-VELOCIDAD, 0);
            }else{
                this.body.setLinearVelocity(0, 0);
            }
        }

    }

    //Actualiza el actor en base al tiempo
    @Override
    public void act(float delta){


        /*
        if(Gdx.input.isTouched()){
            //float width = ((ANCHO  + this.body.getPosition().x) * 2) * (100 / ((WORLD_WIDTH * 100) / SCREEN_WIDTH));
            float width = (((getX() + (ANCHO/2)) / WORLD_WIDTH)/((WORLD_WIDTH*100)/SCREEN_WIDTH)) * (SCREEN_WIDTH / 100);      ;
            float posX = Gdx.input.getX();
            if(posX > width && getX() <= (WORLD_WIDTH - 1f)){
                body.setLinearVelocity(VELOCIDAD, 0);
            }else{
                if(posX < width && getX() >= 1f){
                    body.setLinearVelocity(-VELOCIDAD, 0);
                }else{
                    body.setLinearVelocity(0,0);
                }
            }
        }else{
            body.setLinearVelocity(0,0);
        }
         */

        /*
        float pos = Gdx.input.getAccelerometerX();

        if((pos < -1f || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && getX() <= (WORLD_WIDTH - 1f)){
            body.setLinearVelocity(VELOCIDAD, 0);
        }else{
            if((pos > 1f || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && getX() >= 0.5f){
                body.setLinearVelocity(-VELOCIDAD, 0);
            }else{
                body.setLinearVelocity(0, 0);
            }
        }
         */
    }

    //Dibuja al actor
    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-0.25f, body.getPosition().y-0.25f);

        batch.draw(this.starShipSprite, getX(), getY(), SPACESHIP_WIDTH, LARGO);

        /*
        batch.draw(this.starShipAnimation.getKeyFrame(stateTime, true), getX(), getY(), ancho, largo);

        stateTime += Gdx.graphics.getDeltaTime();

         */
    }

    //Elimina el cuerpo y la fixture
    public void detach(){
        //Destruye el fixture del objeto
        this.body.destroyFixture(this.fixture);
        //Destruye el cuerpo del objeto
        this.world.destroyBody(this.body);
    }
}