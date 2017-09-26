/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tejavafxcheckers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static tejavafxcheckers.TeJavaFXCheckers.TILE_SIZE;

/**
 *
 * @author igor
 */
public class Piece extends StackPane{        

    private double clickedX, clickedY, oldX, oldY;
    private final PieceType type;
    boolean isKing = false;
    Text kingLabel = new Text("K");
    public Piece(int x, int y, PieceType type){
        this.move(x, y);
        this.type = type;
        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        ellipse.setFill(type == PieceType.RED ? Color.valueOf("#c40003") :
                Color.valueOf("#fff9f4"));
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);
        ellipse.setTranslateX((TILE_SIZE - ellipse.getRadiusX() * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - ellipse.getRadiusY() * 2) / 2);
        
        Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        bg.setFill(Color.BLACK);
        bg.setTranslateX((TILE_SIZE - ellipse.getRadiusX() * 2) / 2);
        bg.setTranslateY((TILE_SIZE - ellipse.getRadiusY() * 2) / 2 +
                TILE_SIZE * 0.07);        
        kingLabel.setFont(Font.font(32));
        kingLabel.setVisible(false);
        //double lblWidth = kingLabel.getBoundsInLocal().getWidth();
        //double lblHeight = kingLabel.getBoundsInLocal().getHeight();
        kingLabel.setTranslateX(TILE_SIZE / 5);
        kingLabel.setTranslateY(TILE_SIZE / 4);
        this.getChildren().addAll(bg, ellipse,kingLabel);
    }

    public void setClickedX(double clickedX) {
        this.clickedX = clickedX;
    }

    public void setClickedY(double clickedY) {
        this.clickedY = clickedY;
    }

    public PieceType getType() {
        return type;
    }

    public double getClickedX() {
        return clickedX;
    }

    public double getClickedY() {
        return clickedY;
    }
    
    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }
    
    public void crown(){
        isKing = true;
        kingLabel.setVisible(true);
    }
     
    public final void move(int x, int y){
        this.oldX = x * TILE_SIZE;
        this.oldY = y * TILE_SIZE;
        this.relocate(this.oldX, this.oldY);
    }
    
    public void abortMove(){
        this.relocate(this.oldX, this.oldY);
    }
    
    public boolean isKing(){
        return isKing;
    }
}
 