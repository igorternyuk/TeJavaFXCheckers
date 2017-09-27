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
    private Piece killedPiece;

    public MoveResult(MoveType type){
        this(type, null);
    }
    
    public MoveResult(MoveType type, Piece piece){
        this.type = type;
        this.killedPiece = piece;
    }    

    public Piece getPiece() {
        return killedPiece;
    }

    public MoveType getType() {
        return type;
    }
}
