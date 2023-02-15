package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.VandurGame;

public abstract class BaseScreen implements Screen {

    //
    protected VandurGame vandurGame;

    //Constructor de la clase BaseScreen
    public BaseScreen(VandurGame vandurGame){
        this.vandurGame = vandurGame;
    }

    //Metodo encargado de mostrar la pantalla
    @Override
    public void show(){

    }

    //Metodo encargado de renderizar
    @Override
    public void render(float delta){

    }

    //Ajusta la pantalla
    @Override
    public void resize(int width, int heigth){

    }

    //Opciones al pausar el juego (Cuando abres otra app)
    @Override
    public void pause(){

    }

    //Reanuda el juego
    @Override
    public void resume(){

    }

    //
    @Override
    public void hide(){

    }

    //
    @Override
    public void dispose(){

    }
}
