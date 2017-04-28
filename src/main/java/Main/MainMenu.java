/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Game2D.Game;
import Main.DBConnect.Score;
import SideGames.Pexeso;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import SideGames.quiz;
import java.util.ArrayList;

/**
 *
 * @author PiskoT
 */

public class MainMenu extends Application {
    private DBConnect dbc = new DBConnect();
    private Boolean admin = false;
    Stage primaryStage;
    Game game;
    Scene scene;
    Pane root;
    Pexeso pex;
    private User user;
    public boolean firstOne = true;
    private Text name,score;
    private  ImageView img;

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(game.GAME_WIDTH, game.GAME_HEIGHT);
        
        
        
       // try (InputStream is = Files.newInputStream(Paths.get("src/main/resources/imgs/mainMenu.jpg"))) {   
 
            //img = new ImageView(new Image(is));
            img = new ImageView(new Image(getClass().getResourceAsStream("/imgs/mainMenu.jpg")));
            img.setFitHeight(game.GAME_HEIGHT);
            img.setFitWidth(game.GAME_WIDTH);

            root.getChildren().add(img);
       /* } catch (IOException e) {
            System.out.print(e);
        }*/

        Title title = new Title("P I S K O T");
        title.setTranslateX(475);
        title.setTranslateY(300);

        MenuBox vBox = new MenuBox();

        MenuItem Janosik = new MenuItem("Jánošík");

        Janosik.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try {
                   if(firstOne){
                        firstOne = false;
                        Dialog dialog = new Dialog();
                        dialog.show();

                        dialog.getButton().setOnAction(event ->{
                            String meno = dialog.getTextName();
                            user = new User(meno); 
                            game.setUser(user);
                            dialog.close();                        
                    });
        }
                     game = new Game(primaryStage);
                     
                } catch (Exception ex){
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        MenuItem pexeso = new MenuItem("Pexeso");
        pexeso.setOnMouseClicked((MouseEvent t) -> {
            try {
                pex = new Pexeso(primaryStage);
            } catch (Exception ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //MenuItem mapa = new MenuItem("Mapa");
        MenuItem quiz = new MenuItem("Kvíz");
        quiz.setOnMouseClicked((MouseEvent t) -> {
            try {
                quiz quiz1 = new quiz(primaryStage);
            }catch (Exception ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } 
        });

        MenuItem Koniec = new MenuItem("Koniec");
        Koniec.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                System.exit(0);
            }
        });
        
        MenuItem Skore = new MenuItem("Skóre");
        Skore.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {  
                
                ArrayList<Score> scoreAl = new ArrayList<>();
                scoreAl = dbc.getScore();
                
                Text scr = new Text("Traja najlepší hráči");
                scr.setTranslateX(470);
                scr.setTranslateY(200);
                scr.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 40)); 
                root.getChildren().add(scr);
                
                
                for (int i = 0;i < scoreAl.size(); i++) {
                    name = new Text(scoreAl.get(i).getName());
                    name.setTranslateX(490);
                    name.setTranslateY(290+i*30);
                    name.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 20)); 
                    
                    score = new Text(""+scoreAl.get(i).getSc());
                    score.setTranslateX(800);
                    score.setTranslateY(290+i*30);
                    score.setFont(Font.font("Ubuntu", FontWeight.BOLD, 20)); 
                    
                    root.getChildren().removeAll(title, vBox);
                    root.getChildren().addAll(name,score);
                }
                
                Button back = new Button("Do menu");
                back.setTranslateX(800);
                back.setTranslateY(650);
                root.getChildren().add(back);
                
                back.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                    
                    root.getChildren().clear(); 
                    root.getChildren().addAll(img,title, vBox);
                    }
                });
               
            }
        });
      
        MenuItem Administracia = new MenuItem("Administrácia");
        Administracia.setOnMouseClicked( 
            new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                dialog.setTitle("Login");
                
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Scene dialogScene = new Scene(grid, 300, 275);
                
                
                Text scenetitle = new Text("Administrácia");
                scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid.add(scenetitle, 0, 0, 2, 1);

                Label userName = new Label("Meno:");
                grid.add(userName, 0, 1);

                TextField userTextField = new TextField();
                grid.add(userTextField, 1, 1);

                Label pw = new Label("Heslo:");
                grid.add(pw, 0, 2);

                PasswordField pwBox = new PasswordField();
                grid.add(pwBox, 1, 2);
                
                Button btn = new Button("Prihlásiť");
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().add(btn);
                grid.add(hbBtn, 1, 4);

                btn.setOnAction(new EventHandler<ActionEvent>() {
 
                    @Override
                    public void handle(ActionEvent e) {
                        
                        admin = dbc.isAdmin(userTextField.getText(),pwBox.getText());
                       
                        if(admin){
                        GridPane grid = new GridPane();
                        grid.setAlignment(Pos.CENTER);
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(50, 50, 50, 50));

                        Scene dialogScene = new Scene(grid, 600, 450);

                        Text back = new Text("");
                        back.setFont(Font.font("Tahoma", FontWeight.NORMAL,15));
                        grid.add(back, 0,9);                
                        
                        Text scenetitle = new Text("Do hry");
                        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                        grid.add(scenetitle, 0, 0, 2, 1);                        
                        
                        Label t1 = new Label("Otazka:");
                        grid.add(t1, 0, 1);

                        TextField question = new TextField();
                        grid.add(question, 0, 2);

                        Label t2 = new Label("Správna Odpoveď");
                        grid.add(t2, 0, 3);

                        TextField cAnswer = new TextField();
                        grid.add(cAnswer, 0,4);
                                                
                        Label t30 = new Label("Nesprávna Odpoveď");
                        grid.add(t30, 0, 5);

                        TextField incAnswer = new TextField();
                        grid.add(incAnswer, 0,6);
                        
                        Button btn = new Button("Pridaj otázku");
                        HBox hbBtn = new HBox(10);
                        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                        hbBtn.getChildren().add(btn);
                        grid.add(hbBtn, 0, 7);
                        
                        btn.setOnAction(new EventHandler<ActionEvent>() {
 
                        @Override
                        public void handle(ActionEvent e) {
                            dbc.insertGameQ(question.getText(),cAnswer.getText(),incAnswer.getText());
                            back.setText("Pridané do databázy");
                            question.setText("");
                            cAnswer.setText("");
                            incAnswer.setText("");
                            
                        }});
                        
                        Text quizTitle = new Text("Do kvízu");
                        quizTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                        grid.add(quizTitle, 2,0);
                
                        Label t3 = new Label("Otázka");
                        grid.add(t3, 2, 1);

                        TextField questionQ = new TextField();
                        grid.add(questionQ, 2,2);
                        
                        Label t4 = new Label("Správna odpoveď");
                        grid.add(t4, 2, 3);
                
                        TextField cAnswerQ = new TextField();
                        grid.add(cAnswerQ, 2,4);
                        
                        Label t5 = new Label("Odpoveď 2");
                        grid.add(t5, 2, 5);

                        TextField answer2 = new TextField();
                        grid.add(answer2, 2,6);
                        
                        Label t6 = new Label("Odpoveď 3");
                        grid.add(t6, 2, 7);

                        TextField answer3 = new TextField();
                        grid.add(answer3, 2,8);
                        
                        Label t7 = new Label("Odpoveď 4");
                        grid.add(t7, 2, 9);

                        TextField answer4 = new TextField();
                        grid.add(answer4, 2,10);  

                        Button btnG = new Button("Pridaj otázku");
                        HBox hbBtnG = new HBox(10);
                        hbBtnG.setAlignment(Pos.BOTTOM_RIGHT);
                        hbBtnG.getChildren().add(btnG);
                        grid.add(hbBtnG, 2, 11);
                        
                        btnG.setOnAction((ActionEvent e1) -> {
                            dbc.insertQuizQ(questionQ.getText(),cAnswerQ.getText(),answer2.getText(),answer3.getText(),answer4.getText());
                            back.setText("Pridane do databázy");
                            questionQ.setText("");
                            cAnswerQ.setText("");
                            answer2.setText("");
                            answer3.setText("");
                            answer4.setText("");
                        });
                        
                        Button btnC = new Button("Koniec");
                        HBox btnCx = new HBox(10);
                        hbBtnG.setAlignment(Pos.BOTTOM_RIGHT);
                        hbBtnG.getChildren().add(btnC);
                        grid.add(btnCx, 3, 11);
 
                        btnC.setOnAction((ActionEvent e1) -> {
                            dialog.close();
                        });
                                                
                        dialog.setScene(dialogScene);
                        }else{
                         dialog.close();
                        } 
                    }
                });
                dialog.setScene(dialogScene);
                dialog.show();
            }
         });
      
        vBox.setTranslateX(500);
        vBox.setTranslateY(400);
        vBox.getChildren().addAll(Janosik, pexeso, quiz, Administracia,Skore,Koniec);   
        
        root.getChildren().addAll(title, vBox);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        scene = new Scene(createContent());
        this.primaryStage = primaryStage;
        primaryStage.setTitle("PiskoT App");
       
        primaryStage.setScene(scene);
        primaryStage.show();
        
         System.gc();
    }

    private static class Title extends StackPane {

        public Title(String name) {
            Rectangle bg = new Rectangle(250, 60);
            bg.setStroke(Color.WHITE);
            bg.setStrokeWidth(2);
            bg.setFill(null);

            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 50));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
        }
    }

    public static class MenuBox extends VBox {

        public MenuBox(MenuItem... items) {
            getChildren().add(createSeparator());

            for (MenuItem item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }

        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(200);
            sep.setStroke(Color.DARKGRAY);
            return sep;
        }
    }

    public static class MenuItem extends StackPane {

        public MenuItem(String name) {
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[]{
                new Stop(0, Color.CYAN),
                new Stop(0.1, Color.BLACK),
                new Stop(0.9, Color.BLACK),
                new Stop(1, Color.CYAN)
            });

            Rectangle bg = new Rectangle(200, 30);
            bg.setOpacity(0.2);

            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 22));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setFill(gradient);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });

            setOnMouseEntered(event -> {
                text.setFill(Color.CYAN);
            });

            setOnMouseReleased(event -> {
                bg.setFill(gradient);
            });
        }

    }
    
    public User getUser(){
        return user;
    }
    
    public static void main(String[] args) {
        launch(args);

    }
}
