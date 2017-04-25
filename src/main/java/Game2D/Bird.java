/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game2D;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author PiskoT
 */
public class Bird extends Pane {
    
    Image playerImg = new Image(getClass().getResourceAsStream("/imgs/ptakos.png"));
    ImageView imageView = new ImageView(playerImg);
    int count;
    int columns;
    int offsetX;
    int offsetY;
    int width;
    int height;
    boolean playerSet = false;
    public SpriteAnimation flyingAnimation;
    public Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int lifes;
    private int points;
    private int BIRD_WIDTH = 60;
    private int BIRD_HEIGHT = 60;
    private Game game;
    
    public Bird() {
     flyingAnimation();
     imageView.setFitHeight(BIRD_HEIGHT);
     imageView.setFitWidth(BIRD_WIDTH);
     imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
     getChildren().addAll(this.imageView);
    }
    
    /*Creating flying animation for bird*/
    private void flyingAnimation() {
        count = 3;
        columns = 3;
        offsetX = 0;
        offsetY = 0;
        width = 60;
        height = 60;

        flyingAnimation = new SpriteAnimation(this.imageView, Duration.millis(900), count, columns, offsetX, offsetY, width, height);
    }
    
    /*Set bird movement*/
    public void startMoving(int value){
        for (int i = 0; i < Math.abs(value); i++) {
            this.setTranslateX(this.getTranslateX() -1);
        }
    }
}
