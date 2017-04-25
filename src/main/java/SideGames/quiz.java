    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SideGames;

import Game2D.Game;
import Main.MainMenu;
import Main.DBConnect;

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
import javafx.event.ActionEvent;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author PiskoT
 */
public class quiz {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private Pane appRoot;
    private Stage primaryStage;
    private boolean inMenu = false;
    private QuestionPane qPane;
    private SidePane sPane;
    private List<Question> qs = new ArrayList<>();
    private int category = 1;
    private int qNumber = 1;
    private Pane question = new Pane();
    int correct;

    public quiz(Stage primaryStage) throws Exception {
        appRoot = new Pane();
        this.primaryStage = primaryStage;
        start(primaryStage);
        correct = 0;
    }

    private void update() throws Exception {
        if (isPressed(KeyCode.ESCAPE)) {
            if (!inMenu) {
                backToMenu();
            }
        }
    }

    public void start(Stage ps) throws Exception {
        Scene sc = new Scene(createContent());
        ps.setTitle("Pexeso");

        sc.setOnKeyPressed(event -> {
            keys.put(event.getCode(), true);
        });

        ps.setScene(sc);

        ps.show();
        AnimationTimer timer = new AnimationTimer() {

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

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void backToMenu() throws Exception {
        inMenu = true;
        MainMenu menu = new MainMenu();
        menu.firstOne = false;
        menu.start(primaryStage);
    }

    private Parent createContent() {
        appRoot.getChildren().clear();
        appRoot.setPrefSize(1280, 720);

        try (InputStream is = Files.newInputStream(Paths.get("src/main/resources/imgs/quizBG.png"))) {   
            ImageView img = new ImageView(new Image(is));
            img.setFitHeight(720);
            img.setFitWidth(1280);

            appRoot.getChildren().add(img);

        } catch (IOException e) {
            System.out.print(e);
        }

        DBConnect dbc = new DBConnect();
        qs = dbc.getQuestions(category);
        Collections.shuffle(qs);
        
        qPane = new QuestionPane();
        sPane = new SidePane();    
        qPane.setQuestion(new Question(qs.get(0).getQuestion(), qs.get(0).getCorrectAnswer(), qs.get(0).getAnswer(1), qs.get(0).getAnswer(2), qs.get(0).getAnswer(3)));
        
   
        appRoot.getChildren().addAll(sPane,qPane,question);
        return appRoot;
    }

    private void nextQuestion(Boolean c) {
        if (qNumber != qs.size()) {
            qPane.setQuestion(new Question(qs.get(qNumber).getQuestion(), qs.get(qNumber).getCorrectAnswer(), qs.get(qNumber).getAnswer(1), qs.get(qNumber).getAnswer(2), qs.get(qNumber).getAnswer(3)));
            sPane.selectNext(c);
            qNumber++;
        } else {            
            scoreBoard();
        }
    }

    private void scoreBoard() {
        question.getChildren().clear();
        qPane.getChildren().clear();
        sPane.getChildren().clear();
        
        Text text = new Text ();
        text.setText("Počet správnych odpovedí: "+correct);
        text.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 30));
        text.setFill(Color.WHITE);
        text.setTranslateX(410);
        text.setTranslateY(300);
        
        
        Button btnA = new Button("Znova");
        Button btnB = new Button("Menu");
        btnA.setPrefHeight(30);
        btnB.setPrefWidth(100);
        btnB.setPrefHeight(30);
        btnA.setPrefWidth(100);
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setTranslateX(410);
        grid.setTranslateY(380);

        grid.add(btnA, 0, 1);
        grid.add(btnB, 1, 1);
        
        btnB.setOnAction((ActionEvent e1) -> {
            try {
                backToMenu();
            } catch (Exception ex) {
                Logger.getLogger(quiz.class.getName()).log(Level.SEVERE, null, ex);
            }
        });        
        
        qPane.getChildren().addAll(text,grid);
        
        
        btnA.setOnAction((ActionEvent e1) -> {
            qPane.getChildren().clear();
            qNumber = 1;
            correct = 0;
            createContent();
        });
    }

    private class SidePane extends VBox {

        private int current = 1;

        public SidePane() {
            super(7);
            int allQuestions = qs.size();
            
            if(allQuestions > 10){
                allQuestions = 10;
            }
            for (int i = qs.size(); i > 0; i--) {
                Text textQ = new Text("Otázka " + i);
                textQ.setTranslateX(950);
                textQ.setTranslateY(170);
                textQ.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 20));
                textQ.setFill(i == current ? Color.BLACK : Color.GRAY);
                
                getChildren().add(textQ);
            }
        }
        
        public void selectNext (Boolean c){
            if(current == qs.size()){
                return;
            }
            
            Text text = (Text)getChildren().get(qs.size()-current);
            if(c){
                text.setFill(Color.GREEN);                
                correct++;
            }else{
                text.setFill(Color.RED);
            } 
            current++;
            text = (Text)getChildren().get(qs.size()-current);
            text.setFill(Color.GRAY);
        }
    }

    private class QuestionPane extends VBox {

        private Text text = new Text();
        private List<Button> buttons = new ArrayList<>();
        private Question current;
       
        
        public QuestionPane() {
            super(20);

            text.setTranslateX(450);
            text.setTranslateY(250);
            
            text.setFont(Font.font("Ubuntu", FontWeight.SEMI_BOLD, 24));

           // text.setTextAlignment(TextAlignment.JUSTIFY);
            text.setFill(Color.WHITE);

            question.getChildren().add(text);
            
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);            
            grid.setHgap(20);
            grid.setVgap(20);
            grid.setTranslateX(450);
            grid.setTranslateY(300);

            for (int i = 0; i < 4; i++) {
                Button btn = new Button();
                btn.setPrefWidth(150);
                btn.setOnAction(event -> {                    
                    if (btn.getText().equals(current.getCorrectAnswer())) {
                        nextQuestion(true);
                    }else{
                        nextQuestion(false);
                    }
                });

                buttons.add(btn);
                if (i == 0) {
                    grid.add(btn, 0, 3);
                } else if (i == 1) {
                    grid.add(btn, 1, 3);
                } else if (i == 2) {
                    grid.add(btn,0, 4);
                } else if (i == 3) {
                    grid.add(btn, 1, 4);
                }

            }
            getChildren().add(grid);
        }

        public void setQuestion(Question question) {
            current = question;
            text.setText(question.getQuestion());
            Collections.shuffle(buttons);

            for (int i = 0; i < 4; i++) {
                buttons.get(i).setText(question.getAnswer(i));
            }
        }

    }
}
