/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SideGames;


import Game2D.Game;
import Main.MainMenu;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.plaf.metal.MetalIconFactory;

/**
 *
 * @author PiskoT
 */
public class Pexeso {
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private Pane appRoot;
    private int num_of_pairs = 16;
    private int num_of_row = 4;
    private Tile selected = null;
    private Tile selected2 = null;
    Game game;
    private int clickCount = 2;
    private Stage primaryStage;
    private boolean inMenu = false;
    private ImageView img;
    private Button btn;
    private int correctPairs = 0;
    private Text textA;
    
    public Pexeso(Stage primaryStage) throws Exception {        
       appRoot = new Pane();
       this.primaryStage=primaryStage;
        start(primaryStage);
    }
    
    private Parent createContent() throws Exception{
        appRoot.setPrefSize(game.GAME_WIDTH,game.GAME_HEIGHT);
        
        try(InputStream is = Files.newInputStream(Paths.get("src/main/resources/imgs/mainMenu.jpg"))){
            img = new ImageView(new Image(is));
            img.setFitHeight(game.GAME_HEIGHT);
            img.setFitWidth(game.GAME_WIDTH);
            
            appRoot.getChildren().add(img);
                    
        }catch(IOException e){
            System.out.print(e);
        }
        
        Title title = new Title("P E X E S O");
        title.setTranslateX(475);
        title.setTranslateY(300);     
        MainMenu.MenuBox vBox = new MainMenu.MenuBox();
                
        File folder = new File("src/main/resources/pexeso/");
        File[] listOfFiles = folder.listFiles();
        
        
        for(int i = 0; i < listOfFiles.length; i++){
            String list = listOfFiles[i].getName();
            MainMenu.MenuItem pom = new MainMenu.MenuItem(list);
            
            pom.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
            @Override
            public void handle(MouseEvent t) {
                try {
                    appRoot.getChildren().removeAll(btn,title,vBox);
                    loadPictures(list);
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });    
        vBox.getChildren().add(pom);
        }
        
        MainMenu.MenuItem menu = new MainMenu.MenuItem("Do Menu");
        menu.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                try {
                    backToMenu();
                } catch (Exception ex) {
                    Logger.getLogger(Pexeso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        vBox.setTranslateX(500);
        vBox.setTranslateY(400);
        vBox.getChildren().add(menu);
        /*
        
        */
            appRoot.getChildren().addAll(title,vBox);
        return appRoot;
    }
    
    private void loadPictures (String s) throws Exception{
    List<Tile>tiles = new ArrayList<>();
    correctPairs = 0;
        
        File folder = new File("src/main/resources/pexeso/"+s);
        File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < num_of_pairs/2; i++) {
              if (listOfFiles[i].isFile()) {
                Image backgroundImg = new Image(getClass().getResourceAsStream("/pexeso/"+s+"/"+listOfFiles[i].getName()));
                
                ImageView pImg = new ImageView(backgroundImg);               
                Tile t = new Tile(pImg);
                t.setId(listOfFiles[i].getName());                
                tiles.add(t);
                
                ImageView pImg2 = new ImageView(backgroundImg);               
                Tile t2 = new Tile(pImg2);
                t2.setId(listOfFiles[i].getName());
                tiles.add(t2);
                
              } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
              }
            }        
                
            Collections.shuffle(tiles);
            
            for (int i = 0; i < tiles.size(); i++) {
                Tile tile=tiles.get(i);
                tile.setTranslateX(125*(i % num_of_row)+350);
                tile.setTranslateY(125*(i / num_of_row)+100);
                appRoot.getChildren().add(tile);
            }
            
            textA = new Text();
            textA.setText("Prave si nasiel: ");
            textA.setTranslateX(350);
            textA.setTranslateY(70);
            textA.setFill(Color.WHITE);
            textA.setFont(Font.font("Ubuntu",FontWeight.SEMI_BOLD,35)); 
                    
            btn = new Button();
            btn.setText("Spat na vyber");
            btn.setTranslateX(900);
            btn.setTranslateY(650);
            btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    try {
                        createContent();
                    } catch (Exception ex) {
                        Logger.getLogger(Pexeso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            appRoot.getChildren().addAll(btn,textA);
    }

    private class Tile extends StackPane{
        private ImageView iw = new ImageView();        
        public Tile(ImageView value) throws Exception{
            iw = value;
            Rectangle border = new Rectangle(125,125);
            border.setFill(Color.WHITE);
            border.setStroke(Color.BLACK);
            iw.setFitHeight(124);
            iw.setFitWidth(124);
            
            setOnMouseClicked(event -> {
                if (this.isOpen() || clickCount==0) {
                    return;
                }else{
                    clickCount--;
                    if(selected == null){
                        selected = this;     
                        iw.setOpacity(1);
                        //selected.open(() -> {});
                    }else{
                        //open(() -> {
                        iw.setOpacity(1);   
                        selected2 = this;
                          if(!sameValue(selected,selected2)){
                            selected.close();
                            selected2.close();                        
                            }
                          selected = null;
                          selected2=null;
                          clickCount = 2;
                       // });                  
                    }
                }    
            });

            setAlignment(Pos.CENTER);
            getChildren().addAll(border,iw);
            
            close();
        }
        
        public boolean sameValue(Tile other,Tile other2){
            if(other2.getId().equals(other.getId())){
                correctPairs++;
                String pair = other.getId().substring(0, other.getId().length() - 4);
                pair = pair.replace("_", " ");
                textA.setText("Prave si nasiel: "+pair);
            }
            if(correctPairs == num_of_pairs/2){
                try {
                    createContent();
                } catch (Exception ex) {
                    Logger.getLogger(Pexeso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return other2.getId().equals(other.getId());
        }
        
        public boolean isOpen(){
            return iw.getOpacity()==1;
        }
        
        public void close(){
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5),iw);
            ft.setToValue(0);
            ft.play();
        }

        public void open(Runnable action){
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5),iw);
            ft.setToValue(1);
            ft.setOnFinished(e -> action.run());
            ft.play();
        }
    }    
     private class Title extends StackPane{
        public Title(String name){
            Rectangle bg = new Rectangle (250,60);
            bg.setStroke(Color.WHITE);
            bg.setStrokeWidth(2);
            bg.setFill(null);
            
            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Ubuntu",FontWeight.SEMI_BOLD,50));
            
            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
        }
    }   
     
    public boolean isPressed(KeyCode key){
        return keys.getOrDefault(key, false);
    }

    public void backToMenu() throws Exception{ 
        inMenu = true;
        MainMenu menu = new MainMenu();
        menu.firstOne = false;
        menu.start(primaryStage);
    }
   
    private void update() throws Exception{
        if(isPressed(KeyCode.ESCAPE)){
            if (!inMenu) {
                backToMenu();
            }
        }
    }
    
    public void start(Stage ps) throws Exception{
        Scene sc = new Scene(createContent());
        ps.setTitle("Pexeso");
        
        sc.setOnKeyPressed(event -> {
            keys.put(event.getCode(),true);
               
        }); 
        
        ps.setScene(sc);
       
        ps.show();
        AnimationTimer timer = new AnimationTimer(){

        @Override
        public void handle(long now) {

                try {
                    update();
                } catch (Exception ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timer.start(); 
        
    }
}
