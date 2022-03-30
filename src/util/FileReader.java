/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author JeffersonSales
 */
public final class FileReader {
    
    public static ArrayList<String> readFile(String fileDir) throws FileNotFoundException, IOException {
        
        BufferedReader reader = new BufferedReader(new java.io.FileReader(fileDir));
        ArrayList<String> content = new ArrayList<>();
        
        for(String line; (line = reader.readLine()) != null;){
            
            content.add(line);
        }
        
        reader.close();
        
        return content;
    }
    
}
