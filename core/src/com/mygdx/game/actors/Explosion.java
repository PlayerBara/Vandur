package com.mygdx.game.actors;

import static com.mygdx.game.extra.Utils.USER_ENEMY;

import com.badlogic.gdx.Gdx;
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

public class Explosion extends Actor {

    //Se crea las propiedades necesarias
    private World world;
    private Body body;
    private Animation<TextureRegion> animationExplosion;
    private float stateTime;

    //Se crea unfloat ene l que se añadira el tiempo en que se borre el actor
    private float timeToDelete;

    //Elementos para facil acceso
    private float size;

    //Constructor
    public Explosion(World world, Animation<TextureRegion> animationExplosion, Vector2 position, float size){
        //Se obtiene el mundo
        this.world = world;
        //Se obtiene el sprite
        this.animationExplosion = animationExplosion;


        this.size = size;
        this.stateTime = 0f;

        //Metodos para crear el cuerpo
        createBody(position);
    }

    private void createBody(Vector2 position){
        //Metodo para crear el cuerpo
        BodyDef bodyDef = new BodyDef();
        //Se obtiene la posicion donde se creara el cuerpo
        bodyDef.position.set(position);
        //Le da al cuerpo el tipo kinematico para que pueda moverse sin que le afecte la física
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.body = this.world.createBody(bodyDef);
    }

    //Obtiene el tiempo en que tarda en ejecutarse la animacion completa
    public float getTimeAnimation(){
        return this.animationExplosion.getAnimationDuration();
    }

    //Checkea la animacion para saber si ha terminado
    public boolean checkAnimation(float delta){
        boolean endAnimation = false;
        this.timeToDelete += delta;

        if(this.timeToDelete >= this.getTimeAnimation()){
            this.timeToDelete -= this.getTimeAnimation();
            endAnimation = true;
        }
        return endAnimation;
    }

    //Dibuja al actor
    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-0.4f, body.getPosition().y-0.25f);
        batch.draw(this.animationExplosion.getKeyFrame(stateTime, false), getX(), getY(), size,size );

        stateTime += Gdx.graphics.getDeltaTime();
    }

    public void detach(){
        //Destruye el cuerpo del objeto
        this.world.destroyBody(this.body);
    }
}
