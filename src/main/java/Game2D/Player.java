/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game2D;

import static Game2D.Game.BLOCK_SIZE;
import static Game2D.Game.platforms;
/**
 *
 * @author PiskoT
 */
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Player extends Pane {

    Image playerImg = new Image(getClass().getResourceAsStream("/imgs/janosikos.png"));
    ImageView imageView = new ImageView(playerImg);
    int count;
    int columns;
    int offsetX;
    int offsetY;
    int width;
    int height;
    boolean playerSet = false;
    public SpriteAnimation standingAnimation, runningAnimation, jumpingAnimation, flinchingAnimation;
    public Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int lifes;
    private int points;
    private int PLAYER_WIDTH = 50;
    private int PLAYER_HEIGHT = 70;
    private Game game;

    public Player() {
        points = 0;
        lifes = 5;
        standingAnimation();
        runningAnimation();
        jumpingAnimation();
        flinchingAnimation();
        imageView.setFitHeight(PLAYER_HEIGHT);
        imageView.setFitWidth(PLAYER_WIDTH);
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        getChildren().addAll(this.imageView);
    }

    public void standingAnimation() {
        count = 2;
        columns = 2;
        offsetX = 0;
        offsetY = 0;
        width = 60;
        height = 60;

        standingAnimation = new SpriteAnimation(this.imageView, Duration.millis(900), count, columns, offsetX, offsetY, width, height);
    }

    public void jumpingAnimation() {
        count = 2;
        columns = 2;
        offsetX = 300;
        offsetY = 0;
        width = 60;
        height = 60;

        jumpingAnimation = new SpriteAnimation(this.imageView, Duration.millis(800), count, columns, offsetX, offsetY, width, height);
    }
    
    public void flinchingAnimation() {

        count = 2;
        columns = 2;
        offsetX = 420;
        offsetY = 0;
        width = 60;
        height = 60;

        flinchingAnimation = new SpriteAnimation(this.imageView, Duration.millis(100), count, columns, offsetX, offsetY, width, height);
    }

    public void runningAnimation() {
        count = 3;
        columns = 3;
        offsetX = 120;
        offsetY = 0;
        width = 60;
        height = 60;

        runningAnimation = new SpriteAnimation(this.imageView, Duration.millis(400), count, columns, offsetX, offsetY, width, height);
    }

    public void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (this.getTranslateX() + PLAYER_WIDTH - 5 == platform.getTranslateX()) {
                            return;
                        }
                    } else {
                        if (this.getTranslateX() == platform.getTranslateX() + BLOCK_SIZE -5) {
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    public void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (this.getTranslateY() + PLAYER_HEIGHT == platform.getTranslateY()) {
                            this.setTranslateY(this.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    } else {
                        if (this.getTranslateY() == platform.getTranslateY() + BLOCK_SIZE) {
                            canJump = false;
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    public void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    public int getLifes() {
        return lifes;
    }

    public void dead() {
        lifes = lifes - 1;
    }

    public int getPoints() {
        return points;
    }
   
    public void setPoints(int point) {
        points += point;
    }
}
