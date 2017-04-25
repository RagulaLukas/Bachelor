/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author PiskoT
 */
public class LevelData {
    private ArrayList<String[]> levels = new ArrayList<>();
    private String [] currentLevel;
    private int levelCounter = 0;
    
    public static final String[] LEVEL1 = new String[]{
        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000Q0000000000000000000000000000000000000000",
        "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000Q0000000000000000000000000123000000000000000000000000000000000000000",
        "00000000000000000000000000000000130000000000000000000000000000000000000000000000000000000000002223000000000000000000000000000000000000000000000000000000000000000000",
        "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000500000000000000000000000000000000000F000000000000000000000000000000000",
        "000000Q0000000000000000000000130000000122230000000000000000000000000000000000000000000000022225000000000000000000000000000000001222222300000000000000000000000000000",
        "0000012300000000F0000000000000000000000000000001230000000000050000500000000000000000013000500000000000000000000000000000000000000000000001300000000000Q0000000000000",
        "00000050000012222230000000000000130000000000000050000000000050000005000000022300000000000050000000000000000000000000000000000000000000000000000000000123000000000000",
        "00000050000000000000000000000000000000000000000050000F0000050000000050000005000000000F000050000000000000000000000000000000000000000000000000000000000050000000000002",
        "22222254444444444444442222222224444444444444444452222222222544444444522222254444422222222254444444422442244224422442442222222442222222222222244422244454444422222222"
    };

    public LevelData() throws IOException{
        loadMaps();
    }
    
    public ArrayList getLevels (){
        return levels;
    }
    
    public String[] getFirstLevel(){
        return levels.get(0);
    }
    
    public void setLevel(String[] Level){
        currentLevel = Level;
    }
    
    public String[] getNextLevel(){
        levelCounter++;
        return levels.get(levelCounter);        
    }
    
    public void nullLevelCounter () {
        levelCounter = 0;
    }
    
    public String[] getcurrentLevel(){
        return currentLevel;
    }
    
    public boolean isNext(){
        if(levelCounter != levels.size()){
            return true;
        }
        return false;
    }
    
    private void loadMaps() throws IOException{
        File folder = new File("src/main/resources/levels/");
        File[] listOfFiles = folder.listFiles();
        
         for(int i = 0; i < listOfFiles.length; i++){
            String [] l = readFile(listOfFiles[i].getName());       
            levels.add(l);
         }    
    }
    
    private String[] readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/levels/"+fileName));

        try { 
            String line = br.readLine();
            String[] level = new String[12];
            int i = 0;

            while (line != null) { 
                level[i] = line;               
                i++;
                line = br.readLine();
            }
            return level;
        } finally {
            br.close();
        }
    }
}
