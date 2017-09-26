/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tejavafxcheckers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author igor
 */
public class Tile extends Rectangle {        
    private Piece piece;
    
    public Tile(int x, int y, boolean isLight){
        this.setWidth(TeJavaFXCheckers.TILE_SIZE);
        this.setHeight(TeJavaFXCheckers.TILE_SIZE);
        this.relocate(x * TeJavaFXCheckers.TILE_SIZE,
                y * TeJavaFXCheckers.TILE_SIZE);
        this.setFill(isLight ? Color.valueOf("#feb") : Color.valueOf("#582"));
    }
    
    public boolean hasPiece(){
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

}
