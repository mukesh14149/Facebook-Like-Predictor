package com.iiitd.project.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class ciso {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Random rand = new Random();
		BufferedReader file1=new BufferedReader(new FileReader("/media/mukesh/New Volume/post_analyzer/files/data.txt"));
		int cal=0;
 		String line;
 		int acc=0,countacc=0;
 		try {
			while((line=file1.readLine())!=null){
				String[] cas = line.split(" ");
				System.out.println(cas[0]+ " "+cas);
				acc+=Integer.parseInt(cas[0]);
				countacc++;
				System.out.println(acc);
			}
			file1.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
