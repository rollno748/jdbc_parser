package com.ysl.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DuplicateQueries {
	public static void findDuplicates(String sqlOut, String outDir) throws IOException{

		String sourceSqlFile = outDir + "/IdentifiedSQL.log";
		String sqlout = outDir + "/DuplicatedQueries.log";


		FileInputStream fis = new FileInputStream(sourceSqlFile);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fis));
		BufferedWriter bw = null;
		FileWriter fw = null;
		//int dupCount=0;
		String strLine;
		ArrayList<String> origAr1 = new ArrayList<String>();
		ArrayList<String> ar2 = new ArrayList<String>();

		//Read File Line By Line
		while ((strLine = br1.readLine()) != null)   {
			// Print the content on the console
			origAr1.add(strLine);
		}

		for(int i=0;i<origAr1.size();i++) {
			String test = origAr1.get(i);
			if(test.contains("select") || test.contains("update") || test.contains("insert") || test.contains("delete")){
				if(test.contains("insert")){
					String Query=test;
					ar2.add(Query);				
				}
				if(test.contains("where")){
					String Query=test.substring(0,test.lastIndexOf("where"));
					ar2.add(Query);
				}
			}		

		}
		
		try{

			//If file doesn't exists, then create it
			File file = new File(sqlout);
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);	
			bw.write("===========================================================Duplicated SQL Queries=====================================================================" + "\n");
			bw.write("Below Query/Queries are just a duplicate suspect.. Please check with YSL Perf team for more information" + "\n\n");

			for(int i=0; i<ar2.size(); i++ ){
				String comp= ar2.get(i);
				for (int j=i+1; j<origAr1.size(); j++){

					if (origAr1.get(j).contains(comp)){
						//dupCount++;
						bw.write("\n"+comp + "\n\n");
					}					
								
				}
				/*if (dupCount>1){
					
					bw.write("Duplicate Query Suspect:\n\n");
					bw.write(comp + "\n\n");
					bw.write("\n\n======================================================================================================================================================" + "\n\n");

				}		
				dupCount=0;*/				

			}

		}catch (IOException e) {
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
		br1.close();
		System.out.println("Message: Duplicates queries has been extracted from the Log successfully !!");

	}
}
