package com.ysl.controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.hibernate.jdbc.util.BasicFormatterImpl;

public class SqlFormatter {

	public static void prettify(String  args, String outputDir) throws IOException{
		String formattedSql = outputDir + "/FormattedSQL.sql";

		if (args.length() == 0){
			System.out.println("Usage: java formatsql sqlfile.sql");
			return;
		}
		File file=new File(args);
		if (!file.exists()){
			System.out.println("File not exists:"+args);
			return;
		}

		FileInputStream fstream = new FileInputStream(args);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		ArrayList<String> array_str = new ArrayList<String>();

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			// Print the content on the console
			array_str.add(strLine);
		}

		String formattedSQL = "";
		BufferedWriter bw = null;
		FileWriter fw = null;	

		try {
			int count =1;
			File file1 = new File(formattedSql);
			if (!file1.exists()) {
				file1.createNewFile();
			}
			fw = new FileWriter(file1.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write("\n\n===================================================================Formatted SQL======================================================================" + "\n\n");

			for(String str : array_str) {
				formattedSQL = new BasicFormatterImpl().format(str);
				if(count >1){
					bw.write("\n\n======================================================================================================================================================" + "\n\n");
				}
				
				bw.write("\n"+formattedSQL +"\n");
				count ++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		br.close();
		System.out.println("Message: Queries has been Formatted successfully !!");
	}

}
