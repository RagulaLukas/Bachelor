package Game2D;

/**
 *
 * @author PiskoT
 */
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Block extends Pane {
    
    Image blocksImg = new Image(getClass().getResourceAsStream("/tiles/tilemap2.png"));
    Image ibL = new Image(getClass().getResourceAsStream("/tiles/lSide.png"));
    Image ibR = new Image(getClass().getResourceAsStream("/tiles/rSide.png"));
    Image ibQ = new Image(getClass().getResourceAsStream("/tiles/qMark.png"));
    Image ibW = new Image(getClass().getResourceAsStream("/tiles/water.png"));
    Image ibG = new Image(getClass().getResourceAsStream("/tiles/ground.png"));
    Image ibF = new Image(getClass().getResourceAsStream("/tiles/flames.png"));
    
    ImageView block, blockG, blockF;
    ImageView blockL;
    ImageView blockR;
    ImageView blockQ;
    ImageView blockW;
    
    SpriteAnimation fAnimation;    
    
    public enum BlockType {
        PLATFORM, PLATFORML, PLATFORMR, WATER, GROUND, FLAMES, QUESTION
    }

    public Block(BlockType blockType, int x, int y) {
        block = new ImageView(blocksImg);
        blockL = new ImageView(ibL);
        blockR = new ImageView(ibR);
        blockQ = new ImageView(ibQ);
        blockW = new ImageView(ibW);
        blockG = new ImageView(ibG);
        blockF = new ImageView(ibF);

        setTranslateX(x);
        setTranslateY(y);

        switch (blockType) {
            case PLATFORML:
                blockL.setViewport(new Rectangle2D(0, 0, 60, 60));                
                getChildren().add(blockL);
                Game.platforms.add(this);
                break;
            case PLATFORM:
                block.setViewport(new Rectangle2D(0, 0, 60, 60));                
                getChildren().add(block);
                Game.platforms.add(this);
                break;
            case PLATFORMR:
                blockR.setViewport(new Rectangle2D(1, 0, 60, 60));                
                getChildren().add(blockR);
                Game.platforms.add(this);
                break;
            case QUESTION:
                blockQ.setViewport(new Rectangle2D(0, 0, 60, 60));
                blockQ.setUserData(true);
                getChildren().add(blockQ);
                Game.platforms.add(this);
                break;    
            case WATER:
                blockW.setViewport(new Rectangle2D(0,0,60,60));
                getChildren().add(blockW);
                break;
            case GROUND:
                blockG.setViewport(new Rectangle2D(0,0,60,60));
                getChildren().add(blockG);
                Game.platforms.add(this);
                break;
            case FLAMES:
                blockF.setViewport(new Rectangle2D(0,0,60,60));
                getChildren().add(blockF);               
                break;    
        }
        Game.gameRoot.getChildren().add(this);
    }
    
    public void startFlames (){
        fAnimation = new SpriteAnimation(blockF, Duration.millis(500), 3, 3, 0, 0, 60, 60);
    }
}
