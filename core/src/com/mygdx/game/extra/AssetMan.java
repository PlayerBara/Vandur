package com.mygdx.game.extra;

import static com.mygdx.game.extra.Utils.*;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

//El asset manager se utiliza para cargar y descargar recursos
//hasta que ya no sean necesarios
public class AssetMan {

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public AssetMan(){
        this.assetManager = new AssetManager();

        assetManager.load(MUSIC, Music.class);
        assetManager.load(ATLAS_MAP, TextureAtlas.class);
        assetManager.load(SOUND_SHOOT, Sound.class);
        assetManager.finishLoading();

        this.textureAtlas = assetManager.get(ATLAS_MAP);
    }

    //Imagen del mundo
    public TextureRegion getBackground(){
        return this.textureAtlas.findRegion(BACKGROUND_IMAGE);
    }

    //Animacion de la nave del jugador
    public Animation<TextureRegion> getStarShipAnimation(){
        return new Animation<TextureRegion>(0.2f,
                textureAtlas.findRegion(STARSHIP1),
                textureAtlas.findRegion(STARSHIP2),
                textureAtlas.findRegion(STARSHIP3),
                textureAtlas.findRegion(STARSHIP4));
    }

    public TextureRegion getStarShipTR(){
        return this.textureAtlas.findRegion(STARSHIP);
    }

    //Animación de enemigo pequeño
    public Animation<TextureRegion> getEnemyAnimation(){
        return new Animation<TextureRegion>(0.2f,
                textureAtlas.findRegion(STARSHIP1),
                textureAtlas.findRegion(STARSHIP2),
                textureAtlas.findRegion(STARSHIP3),
                textureAtlas.findRegion(STARSHIP4));
    }

    public TextureRegion getEnemyTR(){
        TextureRegion sprite;

        int tipo = MathUtils.random(0, 3);

        switch (tipo){
            case 0:
                sprite = this.textureAtlas.findRegion(ENEMY_A);
                break;
            case 1:
                sprite = this.textureAtlas.findRegion(ENEMY_B);
                break;
            case 2:
                sprite = this.textureAtlas.findRegion(ENEMY_C);
                break;
            case 3:
                sprite = this.textureAtlas.findRegion(ENEMY_D);
            default:
                sprite = this.textureAtlas.findRegion(ENEMY_E);
                break;
        }

        return sprite;
    }

    public TextureRegion getEnergyBulletTR(){
        return this.textureAtlas.findRegion(ENERGYBULLET);
    }

    public TextureRegion getAsteroidTR(){
        TextureRegion sprite;

        int tipo = MathUtils.random(0, 1);

        if(tipo == 1){
            sprite = this.textureAtlas.findRegion(ASTEROID_A);
        }else{
            sprite = this.textureAtlas.findRegion(ASTEROID_B);
        }

        return sprite;
    }

    //Musica y sonido
    public Music getMusic(){
        return this.assetManager.get(MUSIC);
    }

    public Sound getSoundShoot(){
        return this.assetManager.get(SOUND_SHOOT);
    }
}
