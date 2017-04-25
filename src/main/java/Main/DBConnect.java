/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import SideGames.Question;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author PiskoT
 */
public class DBConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;
    private ArrayList<Admin> users = new ArrayList<>();
    private ArrayList<Question> questions;
    private ArrayList<Score> score;
    public DBConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bcdb", "root", "");
            st = con.createStatement();
            //getUser();
            //getQuestions(1);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void getUser() {
        try {
            String query = "SELECT * from users";
            rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String pass = rs.getString("pass");
                Admin admin = new Admin(name, pass);
                users.add(admin);
            }

        } catch (Exception e) {
            System.out.print(e);
        }
    }
    
    public boolean isAdmin(String name, String pass){
            try {
            String query = "SELECT * FROM users WHERE name='"+name+"' AND pass='"+pass+"'";
            rs = st.executeQuery(query);
            
            return rs.first();
            
        } catch (Exception e) {
            System.out.print(e);
        }
            return false;
        
    }
    
    public void insertGameQ (String question, String correctA, String incA){
       try {
            String query = "INSERT INTO gamequestions (question, correctAnswer,incorrectAnswer) VALUE ('"+question+"','"+correctA+"','"+incA+"')";
            
            st.executeUpdate(query);
            
        } catch (Exception e) {
            System.out.print(e);
        } 
    }
    
    public void insertQuizQ (String question, String correctA, String a2, String a3, String a4){
       try {
            String query = "INSERT INTO questions (question, answerC, answer1, answer2, answer3, category) VALUE ('"+question+"','"+correctA+"','"+a2+"','"+a3+"','"+a4+"','1')";
            
            st.executeUpdate(query);
            
        } catch (Exception e) {
            System.out.print(e);
        } 
    }   
    
    public void insertScore (String name, Integer score, String game){
       try {
            String query = "INSERT INTO points (Name, Score, Game) VALUE ('"+name+"','"+score+"','"+game+"')";
            
            st.executeUpdate(query);
            
        } catch (Exception e) {
            System.out.print(e);
        } 
    } 

    public ArrayList getQuestions(Integer category) {
        try {
            questions = new ArrayList<>();
            String query = "SELECT * FROM questions WHERE category = " + category;
            rs = st.executeQuery(query);
            Question q;

            while (rs.next()) {
                String question = rs.getString("question");
                String ca = rs.getString("answerC");
                String a1 = rs.getString("answer1");
                String a2 = rs.getString("answer2");
                String a3 = rs.getString("answer3");
                q = new Question(question, ca, a1, a2, a3);
                questions.add(q);
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        return questions;
    }
    
    public ArrayList getGameQuestions (){
        try {
        questions = new ArrayList<>();
        String query = "SELECT * FROM gamequestions";
        rs = st.executeQuery(query);
        Question q;

        while (rs.next()) {
            String question = rs.getString("question");
            String ca = rs.getString("correctAnswer");
            String a1 = rs.getString("incorrectAnswer");
            String a2 = "";
            String a3 = "";
            q = new Question(question, ca, a1, a2, a3);
            questions.add(q);
        }
        } catch (Exception e) {
            System.out.print(e);
        }
        
        return questions;
    }
    
    public ArrayList getScore() {
        try {
        score = new ArrayList<>();
        String query = "SELECT * FROM points Order BY Score DESC LIMIT 3";
        rs = st.executeQuery(query);
        Question q;

        while (rs.next()) {
            String name = rs.getString("Name");
            Integer points = rs.getInt("Score");

            Score s = new Score(name,points);
            score.add(s);
        }
        } catch (Exception e) {
            System.out.print(e);
        }       
        return score;
    }
    
    public class Score{

        String name;
        Integer sc;
        public Score(String name,Integer sc){
            this.name = name;
            this.sc = sc;
        }
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSc() {
            return sc;
        }

        public void setSc(Integer sc) {
            this.sc = sc;
        }
    }
    
}

