/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SideGames;

/**
 *
 * @author PiskoT
 */
public class Question {
    private String question;
    private String cAnswer;
    private String answer1;
    private String answer2;
    private String answer3;
    private boolean used = false;
    
    public Question(String q, String c, String a1,String a2, String a3){
        this.question = q;
        this.cAnswer = c;
        this.answer1 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
    }
    
    public String getQuestion(){
        return question;
    }
    
   public String getCorrectAnswer (){
       return cAnswer;
   }
   
    public String getAnswer (int cislo){
        if (cislo == 0) {
            return cAnswer;
        }
        if (cislo == 1) {
            return answer1;
        }
        if (cislo == 2) {
            return answer2;
        }
        if (cislo == 3) {
            return answer3;
        }
        
        return null;
    }
    
    public boolean getUsed(){
        return used;    
    }
    
    public void setUsed (){
        used = true;
    }

}
