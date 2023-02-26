package com.mygdx.game.extra;

import static com.mygdx.game.extra.Utils.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

//El asset manager se utiliza para cargar y descargar recursos
//hasta que ya no sean necesarios
public class AssetMan {

    //Se crean las variables necesarias
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    //Constructor
    public AssetMan(){
        this.assetManager = new AssetManager();

        //Se obtinene de los assets el Atlas para obtener las texturas
        assetManager.load(ATLAS_MAP, TextureAtlas.class);
        //Se obtiene la musica y sonidos
        assetManager.load(MUSIC, Music.class);
        assetManager.load(SOUND_SHOOT, Sound.class);
        assetManager.load(EXPLOSION_SOUND, Sound.class);
        assetManager.load(WIN_SOUND, Sound.class);
        assetManager.load(LOSE_SOUND, Sound.class);
        assetManager.finishLoading();

        this.textureAtlas = assetManager.get(ATLAS_MAP);
    }

    //Imagen del mundo
    public TextureRegion getBackground(){
        return this.textureAtlas.findRegion(BACKGROUND_IMAGE);
    }

    //Imagen de la nave
    public TextureRegion getStarShipTR(){
        return this.textureAtlas.findRegion(STARSHIP);
    }

    //Imagenes de los enemigos
    public TextureRegion getEnemyTR(){
        TextureRegion sprite;

        //Se obtiene un numero al azar
        int tipo = MathUtils.random(0, 3);

        //Se obtiene una textura con el numero generado al azar anteriormente
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

    //Se obtiene la imagen de la bala del jugador
    public TextureRegion getPlayerBulletTR(){
        return this.textureAtlas.findRegion(PLAYERBULLET);
    }

    //Se ibtine la imagen de la bala del enemigo
    public TextureRegion getEnemyBulletTR(){
        return this.textureAtlas.findRegion(ENEMYBULLET);
    }

    //Se obtine la imagen del asteroide
    public TextureRegion getAsteroidTR(){
        TextureRegion sprite;

        //Se crea un numero al azar
        int tipo = MathUtils.random(0, 1);

        //Se obtine la imagen a partir del numero creado al azar
        if(tipo == 1){
            sprite = this.textureAtlas.findRegion(ASTEROID_A);
        }else{
            sprite = this.textureAtlas.findRegion(ASTEROID_B);
        }

        return sprite;
    }

    //Se monta la animación de la explosión
    public Animation<TextureRegion> getExplosionAnimation(){
        return new Animation<TextureRegion>(0.05f,
                textureAtlas.findRegion(EXPLOSION_1),
                textureAtlas.findRegion(EXPLOSION_2),
                textureAtlas.findRegion(EXPLOSION_3),
                textureAtlas.findRegion(EXPLOSION_4),
                textureAtlas.findRegion(EXPLOSION_5),
                textureAtlas.findRegion(EXPLOSION_6),
                textureAtlas.findRegion(EXPLOSION_7),
                textureAtlas.findRegion(EXPLOSION_8));
    }

    //Obtención de música y sonido
    public Music getMusic(){
        return this.assetManager.get(MUSIC);
    }

    public Sound getSoundShoot(){
        return this.assetManager.get(SOUND_SHOOT);
    }

    public Sound getSoundExplosion(){
        return this.assetManager.get(EXPLOSION_SOUND);
    }

    public Sound getSoundLose(){
        return this.assetManager.get(LOSE_SOUND);
    }

    public Sound getSoundWin(){
        return this.assetManager.get(WIN_SOUND);
    }

    //Obtención de fuentes
    public BitmapFont getScoreFont(){
        return new BitmapFont(Gdx.files.internal(SCOREFONT_FNT),Gdx.files.internal(SCOREFONT_PNG), false);
    }

    public BitmapFont getMenuFont(){
        return new BitmapFont(Gdx.files.internal(MENUFONT_FNT),Gdx.files.internal(MENUFONT_PNG), false);
    }

    public BitmapFont getMenuSecondFont(){
        return new BitmapFont(Gdx.files.internal(MENUSECONDFONT_FNT),Gdx.files.internal(MENUSECONDFONT_PNG), false);
    }

}
