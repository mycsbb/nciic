package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestClass {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\dc\\Desktop\\test.txt";  
        String strTime = null;  
        try {  
            Process p = Runtime.getRuntime().exec("cmd /C dir "           
                    + filePath  
                    + "/tc" );  
            InputStream is = p.getInputStream();   
            BufferedReader br = new BufferedReader(new InputStreamReader(is));             
            String line;  
            while((line = br.readLine()) != null){  
                if(line.endsWith(".txt")){  
                    strTime = line.substring(0,17);  
                    break;  
                }                             
             }   
        } catch (IOException e) {  
            e.printStackTrace();  
        }         
        System.out.println("创建时间    " + strTime);    
	}
}
