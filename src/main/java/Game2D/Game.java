    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game2D;

import Main.DBConnect;
import Main.MainMenu;
import Main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author PiskoT
 */

public class Game {
    
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    public static ArrayList<Block> platforms;
    public static ArrayList<Block> questions;
    public static ArrayList<Block> flames;
    public static ArrayList<Bird> birds; 
    
    public static Pane appRoot = new Pane();
    public static Pane gameRoot = new Pane() ;    
    
    public static final int BLOCK_SIZE = 60;
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;    
   
     
    private int levelWidth;
    private String[] mapLevel;   
    public  Text live = new Text();
    public  Text points = new Text();      
    private boolean inMenu = false;
    private boolean dialogBox = false;
    private boolean running = true;
    private boolean flinching = false;
    private boolean end = false, win = false;
    
    private long flinchTimer;
    public Stage primaryStage;
    public Scene scene;
    private Rectangle bg;
    private ImageView backgroundIV;
    private MainMenu menu;
    
    private GameDialog dialog;   
    private Player player; 
    private LevelData ld;
    private Bird bird,bird2;
    private User user;
    
    public Game(Stage stage) throws Exception{
        appRoot = new Pane();
        gameRoot = new Pane();        
        mapLevel = LevelData.LEVEL1;
        start(stage);        
    }
            
    private Parent createContent(String [] mapLvl){
        mapLevel = mapLvl;        
        bg = new Rectangle(GAME_WIDTH,GAME_HEIGHT); 
        
        Image backgroundImg = new Image(getClass().getResourceAsStream("/imgs/background.jpg"));
        backgroundIV = new ImageView(backgroundImg);        
        backgroundIV.setFitHeight(GAME_HEIGHT);
        backgroundIV.setFitWidth(GAME_WIDTH);
        
        Image ihud = new Image(getClass().getResourceAsStream("/tiles/hud.png"));
        ImageView hud = new ImageView(ihud);
        
        levelWidth = mapLevel[0].length()*BLOCK_SIZE;
        
        platforms = new ArrayList<>();
        questions = new ArrayList<>();
        flames = new ArrayList<>();
        birds = new ArrayList<>();
        
        gameRoot.getChildren().clear();
        
        for (int i = 0; i < mapLevel.length; i++) {
            String line = mapLevel[i];
            for (int j = 0; j < line.length(); j++) {
               switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Block PLATFORML = new Block(Block.BlockType.PLATFORML, j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '2':                      
                        Block PLATFORM = new Block(Block.BlockType.PLATFORM,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '3':
                        Block PLATFORMR = new Block(Block.BlockType.PLATFORMR,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '4':
                        Block WATER = new Block(Block.BlockType.WATER,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '5':
                        Block GROUND = new Block(Block.BlockType.GROUND,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case 'F':
                        Block flame = new Block(Block.BlockType.FLAMES,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        flame.startFlames();
                        flame.fAnimation.play();
                        flames.add(flame); 
                        break;    
                    case 'Q':
                        Block question = new Block(Block.BlockType.QUESTION,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        question.setUserData(true);
                        questions.add(question);                        
                }
            }
        }
        if(player == null){
            player = new Player();
        }
        
        hud.setTranslateX(0);
        hud.setTranslateY(20);
        
        bird = new Bird();
        bird.setTranslateX(2200);
        bird.setTranslateY(200);
        bird.flyingAnimation.play();
        birds.add(bird);
        
        bird2 = new Bird();
        bird2.setTranslateX(5200);
        bird2.setTranslateY(100);
        bird2.flyingAnimation.play();
        birds.add(bird2);
        
        player.setTranslateX(20);
        player.setTranslateY(400);
        
        live.setText(""+player.getLifes());
        live.setFont(new Font(30));
        live.setFill(Color.WHITE);
        live.setTranslateX(-40);
        live.setTranslateY(17);
       
        points.setText(""+player.getPoints());
        points.setFont(new Font(30));
        points.setFill(Color.WHITE);
        points.setTranslateX(55);
        points.setTranslateY(17);
        
        player.translateXProperty().addListener((obs, old, newValue) ->{
            int offset = newValue.intValue();             
            if (offset > GAME_WIDTH/2 && offset < levelWidth-GAME_WIDTH/2) {
                gameRoot.setLayoutX(-(offset-GAME_WIDTH/2));                
            }
        });
        
        StackPane info = new StackPane();
        
        info.getChildren().addAll(hud,live,points);
        
        gameRoot.setLayoutX(0);
        
        gameRoot.getChildren().addAll(player,bird,bird2);
        
        appRoot.getChildren().clear();
        appRoot.getChildren().addAll(bg,backgroundIV,gameRoot,info);
        
        return appRoot;
    }

    private void start(Stage primaryStage) throws Exception {
        ld = new LevelData();
        mapLevel = ld.getFirstLevel();        
        this.primaryStage = primaryStage;
        scene = new Scene(createContent(mapLevel));
        scene.setOnKeyPressed(event -> {
            keys.put(event.getCode(),true);
               
        });        
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false); 
            player.runningAnimation.stop();
            player.jumpingAnimation.stop();
            player.standingAnimation.play();
        });
        
        primaryStage.setTitle("Janosikos");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        AnimationTimer timer = new AnimationTimer(){

            @Override
            public void handle(long now) {
                if(running || end){
                    try {
                        update();
                    } catch (Exception ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if(end){
                    try {
                        update();
                    } catch (Exception ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(!win){
                        Image backgroundImg = new Image(getClass().getResourceAsStream("/imgs/go.png"));
                        backgroundIV = new ImageView(backgroundImg);
                    }else{
                        Image backgroundImg = new Image(getClass().getResourceAsStream("/imgs/cg.jpg"));
                        backgroundIV = new ImageView(backgroundImg);
                    }
                    
                    backgroundIV.setFitHeight(GAME_HEIGHT);
                    backgroundIV.setFitWidth(GAME_WIDTH);
                    
                    appRoot.getChildren().add(backgroundIV);
                    
                    Text info = new Text("Vaše celkové skóre je: "+player.getPoints());
                    Text con = new Text("Pre pokračovanie stlačte Enter!");
                    
                    info.setTranslateX(550);
                    info.setTranslateY(400);
                    info.setFill(Color.WHITE);
                    
                    con.setTranslateX(530);
                    con.setTranslateY(450);
                    con.setFill(Color.WHITE);
                    
                    appRoot.getChildren().addAll(info,con);
                }
                
                if(dialogBox){
                    dialogBox = false;
                    keys.keySet().forEach(key -> keys.put(key,false));
                    
                    if(dialog == null){
                        dialog = new GameDialog();
                    }
                    
                    dialog.getCorrectButton().setOnAction(ecent ->{
                        dialog.correct = true;

                        addPoints(dialog.isCorrect());

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.setContentText("Správna odpoveď dostávaš 100 bodov");

                        alert.showAndWait();
                        
                        dialog.close();
                        running = true;
                    });
                    
                    dialog.getInCorrectButton().setOnAction(ecent ->{
                        dialog.correct = false;

                        addPoints(dialog.isCorrect());

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.setContentText("Nesprávna odpoveď dostávaš 10 bodov");

                        alert.showAndWait();
                        
                        dialog.close();
                        running = true;
                    });

                 dialog.open();
                }
            }            
        };  
        timer.start();       
    }
    
    private void update() throws Exception{
        if((isPressed(KeyCode.W)|| isPressed(KeyCode.UP))&& player.getTranslateY() >=5){
            player.jumpingAnimation.play();            
            player.jumpPlayer();
        }
        
        if((isPressed(KeyCode.A) || isPressed(KeyCode.LEFT)) && player.getTranslateX() >=5){
            player.setScaleX(-1);            
            player.runningAnimation.play();      
            player.movePlayerX(-5);
        }
        
        if((isPressed(KeyCode.D)|| isPressed(KeyCode.RIGHT))&& player.getTranslateX()+60 <= levelWidth -5){
            player.setScaleX(1);                      
            player.runningAnimation.play();      
            player.movePlayerX(5);
        }
        
        if(isPressed(KeyCode.ESCAPE)){
               if (!inMenu) {
                backToMenu();
            }
        }
        
        if(player.getTranslateX()>levelWidth-120){
            if(ld.isNext()){
                mapLevel = ld.getNextLevel();
                createContent(mapLevel);
            }else{
            end = true;
            win = true;
            }
        }
        
        if(end){
            if(isPressed(KeyCode.ENTER)){
                end = false;
                backToMenu();
            }
        }    
        
        if(flinching){
            player.flinchingAnimation.play();
            long  elapsed = (System.nanoTime()- flinchTimer) /1000000;
            if(elapsed > 500){
                flinching = false;
                player.flinchingAnimation.stop();
            }
        }
        
        if (player.playerVelocity.getY()<10) {
            player.playerVelocity = player.playerVelocity.add(0,1);
        }
        
        player.movePlayerY((int)player.playerVelocity.getY());
        
        if(player.getTranslateY()>700){  
            player.dead();
            if(player.getLifes() <=0){
                player.setTranslateX(0);
                player.setTranslateY(400);
                gameRoot.setLayoutX(0);
               if (!inMenu) {
                end = true;                
               }
            }else{                    
                setLive(""+player.getLifes());
                player.setTranslateX(0);
                player.setTranslateY(400);
                gameRoot.setLayoutX(0);
            }
        }
        
        setPoints(""+player.getPoints());
         
        for(Block question : questions){
            if(player.getBoundsInParent().intersects(question.getBoundsInParent())){                
                question.setUserData(false);
                dialogBox = true;
                running = false;
            }
        }
       
        for(Block flame : flames){
            if(player.getBoundsInParent().intersects(flame.getBoundsInParent())){  
               takeDamage();               
               flinchTimer = System.nanoTime();
            }   
        }
        
        for(Bird b : birds){
            if(player.getBoundsInParent().intersects(b.getBoundsInParent())){  
               takeDamage();               
               flinchTimer = System.nanoTime();
            }   
        }
        
        for(Iterator<Block> it = questions.iterator(); it.hasNext();){
            Block question = it.next();            
           if((Boolean)question.getUserData()==false){
                it.remove();
                platforms.remove(question);
                gameRoot.getChildren().remove(question);
            }
        }
        
        if(bird.getTranslateX() <= 60){
            bird.setTranslateX(levelWidth);
        }
        
        if(bird2.getTranslateX() <= 60){
            bird2.setTranslateX(levelWidth);
        }
        bird.startMoving(-5);
        bird2.startMoving(-5);
    }
    
    private void takeDamage() throws Exception{
        if(flinching){
            return;
        }
        
        player.dead();
        flinching = true;
        setLive(""+player.getLifes());
        if(player.getLifes() <=0){
            player.setTranslateX(0);
            player.setTranslateY(400);
            gameRoot.setLayoutX(0);
            end = true;        
        } 
    }
    
    public  void addPoints(boolean correct) {
        if(correct){
            player.setPoints(100);   
        }else{
            player.setPoints(10);
        }
    }
    
    public boolean isPressed(KeyCode key){
        return keys.getOrDefault(key, false);
    }
    
    public void backToMenu() throws Exception{
        
        appRoot.getChildren().clear();
        gameRoot.getChildren().clear();
        
        String userName = user.getMeno();
        
        DBConnect db = new DBConnect();
        db.insertScore(userName,player.getPoints(),"J");

        inMenu = true;
        if(menu == null){
            menu = new MainMenu();
        }        
        ld.nullLevelCounter();
        menu.start(primaryStage);
    }    
 
    public void setRunning(boolean run){
        running = run;
    }
    
    public Player getPlayer (){
        return player;
    }
    
    public Text getPoints(){
        return points;
    }
    
    public void setPoints(String point){
        points.setText(point);               
    }
    
    public void setUser(User u){
        this.user = u;
    }
    
    public Text getLive(){
        return live;
    }
    
    public void setLive(String liv){
        live.setText(liv);
    }
    
}
