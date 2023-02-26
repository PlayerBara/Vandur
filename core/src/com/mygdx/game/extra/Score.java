package com.mygdx.game.extra;

public class Score {

    private int points;
    private int combo;
    private int multiplicador;

    //Constructor
    public Score(){
        this.points = 0;
        this.combo = 0;
        this.multiplicador = 1;
    }

    //Getter y setter
    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getCombo() {
        return combo;
    }

    public int getMultiplicador() {
        return multiplicador;
    }

    //Metodos
    //Suma puntos teniendo en cuenta el combo para mas puntuación
    public void sumPoints(int cantidad){

        this.points += (cantidad * this.multiplicador);

    }

    //Suma al combo
    public void sumCombo(){
        this.combo++;
        modifyMultiplicator();
    }

    //Reinicia el combo
    public void removeCombo(){
        this.combo = 0;
    }

    private void modifyMultiplicator(){
        if(this.combo >= 20){
            multiplicador = 10;
        }else{
            if(combo >= 15){
                multiplicador = 5;
            }else{
                if(combo >= 10){
                    multiplicador = 3;
                }else{
                    if(combo >= 5){
                        multiplicador = 2;
                    }else{
                        multiplicador = 1;
                    }
                }
            }
        }
    }

}
