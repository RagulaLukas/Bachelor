/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game2D;



import Main.DBConnect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import SideGames.Question;

/**
 *
 * @author PiskoT
 */
class GameDialog extends Stage{
    
    private Text textQuestion = new Text();
    private TextField fieldAnswer = new TextField();
    private Text textAnswer = new Text();
    private List<Question> qs = new ArrayList<>();
    private Text text = new Text();
    private Button cor = new Button();
    private Button incor = new Button();
    public boolean correct = false;
    private int i = 0;
    private VBox vbox;
    Button submitBtn = new Button("Potvrď");
    
    public GameDialog(){    
        
        if(Math.random()<=0.5){
            vbox = new VBox(10,textQuestion, cor, incor);
        }else{
            vbox = new VBox(10,textQuestion, incor, cor);
        }
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox);
        
        DBConnect dbc = new DBConnect();
        qs = dbc.getGameQuestions();
        Collections.shuffle(qs);
        
        setScene(scene);
        //Window block, cant click to another window
        initModality(Modality.APPLICATION_MODAL);        
    }
    
    public void open(){
        cor.setText(getCAnswer(i));
        incor.setText(getAnswer(i));
        textQuestion.setText(getQuestion(i));        
        correct=false;
        show();
        i++;
    }    
    
    public boolean isCorrect(){
        return correct;
    }
    
    public Button getCorrectButton(){
        return cor;
    }
    
    public Button getInCorrectButton(){
        return incor;
    }
    
    public Button getButton(){
        return submitBtn;
    }
    
    public TextField getTextField(){
        return fieldAnswer;
    }
    
    public Text getTextAnswer(){
        return textAnswer;
    }
    
    public void answer (String s){
        text.setText(s);
    }
    
    public void setTextAnswer(String value){
        textAnswer.setText(value);
    }
    
    private String getQuestion(int i) {
        return qs.get(i).getQuestion();
    }
    
    private String getAnswer(int i){
        return qs.get(i).getAnswer(1);
    }

    private String getCAnswer(int i) {
        return qs.get(i).getCorrectAnswer();
    }
}
