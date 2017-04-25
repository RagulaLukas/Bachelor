/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author PiskoT
 */
public class Admin {
    private String name;
    private String pass;
    
    public Admin(String n,String p){
        this.name = n;
        this.pass = p;
    }
    
    public String getName(){
        return name;
    }
    
    public String getPass(){
        return pass;
    }
    
    public Boolean checkPass(String p){
        if(p==pass){
            return true;
        }else{
            return false;
        }
    }
}
