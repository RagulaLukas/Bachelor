/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author PiskoT
 */
public class Dialog extends Stage{
    private Button confirm = new Button("Potvrď");
    private TextField nameInput;
    
    public Dialog(){
        Label label = new Label("Meno hráča");    
        nameInput = new TextField();
        
        VBox vb = new VBox(10,label,nameInput,confirm);
        
        vb.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vb);
        
        setScene(scene);
        
        //Window block, cant click to another window
        initModality(Modality.APPLICATION_MODAL);    
     }
    
    public Button getButton(){
        return confirm;
    }
    
    public String getTextName(){
        return nameInput.getText();
    }
    
}
