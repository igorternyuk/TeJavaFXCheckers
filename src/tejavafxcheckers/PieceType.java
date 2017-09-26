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
public enum PieceType {
    RED(1), WHITE(-1);
    public int getMoveDir(){
        return moveDir;
    }
    private final int moveDir;
    private PieceType(int moveDir){
        this.moveDir = moveDir;
    }
}
