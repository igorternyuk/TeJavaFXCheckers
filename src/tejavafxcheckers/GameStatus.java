/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tejavafxcheckers;

/**
 *
 * @author igor
 */
public enum GameStatus {
    PLAY(""),
    WHITE_WON("White won!!!"),
    RED_WON("Red won!!!"),
    DRAW("Draw!");
    public String getDescription(){
        return desc;
    }
    private final String desc;
    private GameStatus(String desc){
        this.desc = desc;
    }
}
