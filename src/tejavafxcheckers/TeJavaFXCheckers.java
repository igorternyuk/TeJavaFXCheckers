/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tejavafxcheckers;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author igor
 * Last edited 26.09.2017
 */
public class TeJavaFXCheckers extends Application {
    private static final String TITLE_OF_PROGRAM = "TeCheckers";
    static final int BOARD_X = 8;
    static final int BOARD_Y = 8;
    static final int TILE_SIZE = 90;
    private static final int WINDOW_WIDTH = BOARD_X * TILE_SIZE;
    private static final int WINDOW_HEIGHT = BOARD_Y * TILE_SIZE;
    private final Tile[][] board = new Tile[BOARD_Y][BOARD_Y];
    private final Group gTiles = new Group();
    private final Group gPieces = new Group();
    private final List<Piece> whitePieces = new ArrayList<>();
    private final List<Piece> redPieces = new ArrayList<>();
    boolean isWhiteTurn = true;
    private GameStatus gameStatus = GameStatus.PLAY;
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        scene.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.SPACE){
                prepareNewGame();
            }
        });
        primaryStage.setTitle(TITLE_OF_PROGRAM);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        for(int y = 0; y < BOARD_Y; ++y){
            for(int x = 0; x < BOARD_X; ++x){
                Tile tile = new Tile(x, y, (x + y) % 2 == 0);
                gTiles.getChildren().add(tile);
                board[y][x] = tile;
            } 
        }
        root.getChildren().addAll(gTiles, gPieces);
        setPieces();
        return root;
    }
    
    public void prepareNewGame(){
        setPieces();
        gameStatus = GameStatus.PLAY;
        isWhiteTurn = true;
    }
    
    private void clearBord(){
        whitePieces.clear();
        redPieces.clear();
        for(int y = 0; y < BOARD_Y; ++y){
            for(int x = 0; x < BOARD_X; ++x){
                if(board[y][x].hasPiece()){
                    gPieces.getChildren().remove(board[y][x].getPiece()); 
                }
            } 
        }
    }
    
    private void setPieces(){
        clearBord();
        for(int y = 0; y < BOARD_Y; ++y){
            for(int x = 0; x < BOARD_X; ++x){
                Piece piece = null;
                if(y <= 2 && (x + y) % 2 != 0){
                    piece = makePiece(x, y, PieceType.RED);
                    gPieces.getChildren().add(piece);
                    redPieces.add(piece);
                }                
                if(y >= 5 && (x + y) % 2 != 0){
                    piece = makePiece(x, y, PieceType.WHITE);
                    gPieces.getChildren().add(piece);
                    whitePieces.add(piece);
                }                
                board[y][x].setPiece(piece);
            } 
        }
    }
    
    private Piece makePiece(int x, int y, PieceType type) {
        Piece piece = new Piece(x, y, type);
        piece.setOnMousePressed(e -> {
            if (gameStatus == GameStatus.PLAY &&
                (isWhiteTurn && piece.getType() == PieceType.WHITE)
                || (!isWhiteTurn && piece.getType() == PieceType.RED)) {
                piece.setClickedX(e.getSceneX());
                piece.setClickedY(e.getSceneY());
            }
        });

        piece.setOnMouseDragged(e -> {
            if (gameStatus == GameStatus.PLAY &&
                (isWhiteTurn && piece.getType() == PieceType.WHITE)
                || (!isWhiteTurn && piece.getType() == PieceType.RED)) {
                piece.relocate(piece.getOldX() + e.getSceneX() - 
                piece.getClickedX(), piece.getOldY() + e.getSceneY() - 
                piece.getClickedY());
            }
        });

        piece.setOnMouseReleased(e -> {
            if (gameStatus == GameStatus.PLAY) {
                if ((isWhiteTurn && piece.getType() == PieceType.WHITE)
                    || (!isWhiteTurn && piece.getType() == PieceType.RED)) {
                    int newX = toBoardCoords(piece.getLayoutX());
                    int newY = toBoardCoords(piece.getLayoutY());
                    int oldX = toBoardCoords(piece.getOldX());
                    int oldY = toBoardCoords(piece.getOldY());
                    MoveResult result = tryMove(newX, newY, piece);
                    switch (result.getType()) {
                        case NONE:
                            piece.abortMove();
                            System.out.println("Aborting move");
                            break;
                        case NORMAL:
                            piece.move(newX, newY);
                            board[newY][newX].setPiece(piece);
                            board[oldY][oldX].setPiece(null);
                            checkKing(piece);
                            isWhiteTurn = !isWhiteTurn;
                            checkGameStatus();
                            System.out.println("Normal move");
                            break;
                        case JUMP:
                            piece.move(newX, newY);
                            board[newY][newX].setPiece(piece);
                            board[oldY][oldX].setPiece(null);
                            Piece killedPiece = result.getPiece();
                            int kpx = toBoardCoords(killedPiece.getOldX());
                            int kpy = toBoardCoords(killedPiece.getOldY());
                            board[kpy][kpx].setPiece(null);
                            if (isWhiteTurn) {
                                redPieces.remove(killedPiece);
                                gPieces.getChildren().remove(killedPiece);

                            } else {
                                whitePieces.remove(killedPiece);
                                gPieces.getChildren().remove(killedPiece);
                            }
                            checkKing(piece);
                            if (!hasJumps(piece)) {
                                if(piece.isKing()){
                                    System.out.println("King has no jumps");
                                }
                                isWhiteTurn = !isWhiteTurn;
                            }
                            checkGameStatus();                            
                            break;
                    }
                }
            }

        });
        return piece;
    }
    
    private void checkKing(Piece piece){
        if(piece == null) return;
        if(piece.getType() == PieceType.RED){
            if(toBoardCoords(piece.getLayoutY()) == BOARD_Y - 1){
                piece.crown();
            }
        }
        else {
            if(toBoardCoords(piece.getLayoutY()) == 0){
                piece.crown();
            }
        }
    }
    
    private void checkGameStatus(){
        boolean hasWhiteAnyMoves = hasMovesOrJumps(whitePieces);
        boolean hasRedAnyMoves = hasMovesOrJumps(redPieces);
        /*System.out.println("isWhiteTurn = " + isWhiteTurn);
        System.out.println("hasWhiteAnyMoves  = " + hasWhiteAnyMoves);
        System.out.println("whitePieces.size() = " + whitePieces.size());
        System.out.println("hasRedAnyMoves = " + hasRedAnyMoves);
        System.out.println("redPieces.size() = " + redPieces.size());*/
        if(isWhiteTurn){
            if(!hasWhiteAnyMoves){
                gameStatus = GameStatus.RED_WON;
                System.out.println("Red won!");
            }
        }
        else
        {
            if(!hasRedAnyMoves){
                gameStatus = GameStatus.WHITE_WON;
                System.out.println("White won!");
            }
        }
        
        if(gameStatus != GameStatus.PLAY){
            String info = gameStatus == GameStatus.WHITE_WON ? "WHITE WON!!!" :
                    "RED WON!!!";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, info +
                    "\nPlay again?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Do you want continue?");
            alert.setHeaderText("Do you want continue?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                prepareNewGame();
            }
            else {
                Platform.exit();
                System.exit(0);
            }
        }
    }
    
    private boolean isValidPosition(double x, double y){
        return x >= 0 && x < BOARD_X && y >= 0 && y < BOARD_Y;
    }
    
    private boolean checkKingJumps(List<Point2D> range){
        System.out.println("range.size() = " + range.size());
        for(int i = 0; i < range.size(); ++i){
            int curX = (int)range.get(i).getX();
            int curY = (int)range.get(i).getY();
            if(board[curY][curX].hasPiece()){
                if(i == range.size() - 1) return false;
                PieceType typeCurr = board[curY][curX].getPiece().getType();
                int nextX = (int)range.get(i + 1).getX();
                int nextY = (int)range.get(i + 1).getY();
                boolean isNextTileFree = !board[nextY][nextX].hasPiece();
                return isWhiteTurn ?
                       (typeCurr != PieceType.WHITE && isNextTileFree) :
                       (typeCurr != PieceType.RED && isNextTileFree);
            }
        }
        return false;
    }
    
    private boolean hasMoves(Piece piece) {
        if (piece == null) {
            return false;
        }
        int px = toBoardCoords(piece.getLayoutX());
        int py = toBoardCoords(piece.getLayoutY());
        if(piece.isKing()){
            int[] dx = {-1,1,-1,1};
            int[] dy = {-1,-1,1,1};
            for(int d = 0; d < dx.length; ++d){
                if(isValidPosition(px + dx[d], py + dy[d]) &&
                   !board[py + dy[d]][px + dx[d]].hasPiece()){
                    return true;
                }
            }
            return false;
        }
        else {
            int newX1 = px - 1, newX2 = px + 1;
            int newY = py + piece.getType().getMoveDir();
            return (isValidPosition(newX1, newY)
                    && !board[newY][newX1].hasPiece())
                    || (isValidPosition(newX2, newY)
                    && !board[newY][newX2].hasPiece());
        }
    }
    
    private boolean hasJumps(Piece piece) {
        if (piece == null) {
            return false;
        }
        int px = toBoardCoords(piece.getOldX());
        int py = toBoardCoords(piece.getOldY());
        
        if(piece.isKing()){
            List<Point2D> topLeft = new ArrayList<>();
            List<Point2D> topRight = new ArrayList<>();
            List<Point2D> bottomLeft = new ArrayList<>();
            List<Point2D> bottomRight = new ArrayList<>();
            for(int i = 1; i < BOARD_X - 1; ++i){
                //TopLeft
                Point2D p1 = new Point2D(px - i, py - i);
                if(isValidPosition(p1.getX(), p1.getY())){
                    topLeft.add(p1);
                }
                //TopRight
                Point2D p2 = new Point2D(px + i, py - i);
                if(isValidPosition(p2.getX(), p2.getY())){
                    topRight.add(p2);
                }
                //BottomLeft
                Point2D p3 = new Point2D(px - i, py + i);
                if(isValidPosition(p3.getX(), p3.getY())){
                    bottomLeft.add(p3);
                }
                //BottomRight
                Point2D p4 = new Point2D(px + i, py + i);
                if(isValidPosition(p4.getX(), p4.getY())){
                    bottomRight.add(p4);
                }                
            }
            /*System.out.println("checkKingJumps(topLeft) = " + checkKingJumps(topLeft));
            System.out.println("checkKingJumps(topRight) = " + checkKingJumps(topRight));
            System.out.println("checkKingJumps(bottomLeft) = " + checkKingJumps(bottomLeft));
            System.out.println("checkKingJumps(bottomRight) = " + checkKingJumps(bottomRight));*/
            return checkKingJumps(topLeft) || checkKingJumps(topRight) ||
                   checkKingJumps(bottomLeft) || checkKingJumps(bottomRight);
        }
        else {
            boolean topLeft = isValidPosition(py - 2, px - 2) &&
                   !(board[py - 2][px - 2].hasPiece()) &&
                    (board[py - 1][px - 1].hasPiece()) &&
                    (board[py - 1][px - 1].getPiece().getType() != piece
                            .getType());

            boolean bottomLeft = isValidPosition(py + 2, px - 2) &&
                    !(board[py + 2][px - 2].hasPiece()) &&
                    (board[py + 1][px - 1].hasPiece()) &&
                    (board[py + 1][px - 1].getPiece().getType() != piece
                            .getType());

            boolean topRight = isValidPosition(py - 2, px + 2) &&
                    !(board[py - 2][px + 2].hasPiece()) &&
                    (board[py - 1][px + 1].hasPiece()) &&
                    (board[py - 1][px + 1].getPiece().getType() != piece
                            .getType());

            boolean bottomRight = isValidPosition(py + 2, px + 2) &&
                    !(board[py + 2][px + 2].hasPiece()) &&
                    (board[py + 1][px + 1].hasPiece()) &&
                    (board[py + 1][px + 1].getPiece().getType() != piece
                            .getType());
            return topLeft || bottomLeft || topRight || bottomRight;
        }       
    }
    
    private boolean hasMovesOrJumps(List<Piece> pieces){
        return !pieces.isEmpty() && pieces.stream().anyMatch((p) ->
                (hasMoves(p) || hasJumps(p)));
    }
    
    private boolean hasSideJumps(List<Piece> pieces){
        return !pieces.isEmpty() && pieces.stream().anyMatch((p) ->
                (hasJumps(p)));
    }
    
    private int toBoardCoords(double px){
        return (int)(px + TILE_SIZE / 2) / TILE_SIZE;
    }
    
    private MoveResult tryMove(int newX, int newY, Piece piece){
        int x0 = toBoardCoords(piece.getOldX());
        int y0 = toBoardCoords(piece.getOldY());
        if(board[newY][newX].hasPiece() ||
                    (newX + newY) % 2 == 0){
                return new MoveResult(MoveType.NONE);
        }
        else{
            if(piece.isKing()){
                int dx = Math.abs(newX - x0) / (newX - x0);
                int dy = Math.abs(newY - y0) / (newY - y0);
                boolean isEnemyPieceOnThreWay = false;
                Piece enemyPiece = null;
                for(int x = x0 + dx, y = y0 + dy; x != newX; x += dx, y += dy){
                    if(board[y][x].hasPiece()){
                        if(board[y][x].getPiece().getType() == piece.getType()){
                            return new MoveResult(MoveType.NONE);
                        }
                        else {
                            if(!isEnemyPieceOnThreWay){
                                isEnemyPieceOnThreWay = true;
                                enemyPiece = board[y][x].getPiece();
                            }
                            else {
                               return new MoveResult(MoveType.NONE); 
                            }
                        }
                    }
                }
                if(isEnemyPieceOnThreWay){
                    return new MoveResult(MoveType.JUMP, enemyPiece);                    
                }
                else {
                    if(hasSideJumps(isWhiteTurn ? whitePieces : redPieces)) {
                        System.out.println("Jump is mandatory!");
                        return new MoveResult(MoveType.NONE);
                    }
                    else {
                        return new MoveResult(MoveType.NORMAL);
                    } 
                }
            }
            else {
                if(Math.abs(newX - x0) == 1 && 
                        newY - y0 == piece.getType().getMoveDir()){
                    if(hasSideJumps(isWhiteTurn ? whitePieces : redPieces)) {
                        System.out.println("Jump is mandatory!");
                        return new MoveResult(MoveType.NONE);
                    }
                    else {
                        return new MoveResult(MoveType.NORMAL);
                    }            
                }
                else if(Math.abs(newX - x0) == 2 &&
                        Math.abs(newY - y0) == 2){
                    int x1 = (x0 + newX) / 2;
                    int y1 = (y0 + newY) / 2;
                    if(board[y1][x1].getPiece().getType() != piece.getType()){
                        return new MoveResult(MoveType.JUMP,
                                board[y1][x1].getPiece());
                    }
                }
            }
        }        
        
        return new MoveResult(MoveType.NONE);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
