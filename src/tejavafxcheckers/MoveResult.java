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
public class MoveResult {
    private MoveType type;
    private Piece piece;

    public MoveResult(MoveType type){
        this(type, null);
    }
    
    public MoveResult(MoveType type, Piece piece){
        this.type = type;
        this.piece = piece;
    }    

    public Piece getPiece() {
        return piece;
    }

    public MoveType getType() {
        return type;
    }
}
