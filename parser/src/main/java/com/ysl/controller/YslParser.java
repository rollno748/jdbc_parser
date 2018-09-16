/*
 * Purpose		: This jar is created to obtain JDBC trace for YSL component
 * Created by 	: YSL Performance Engineering Team
 * Usage Notes	: yslparser.jar [source file] -o [output folder path] 
 * 
 */

package com.ysl.controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class YslParser {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		if (args.length != 3) {
		      System.out.println("Command line arguments missing (Source file / Output destination)");
		      System.out.println("Usage Notes: yslparser.jar [source file path] -o [output folder path]");
		      System.exit(-1);
		    }		
		
		String outputDir="";
		String formatLoc="";
				
		//String filePath="D:\\jdbc_trace\\SC01_YSL_SDGON_T01_UserAccessToken.log";	
		String filePath=args[0];
		
		for (int i = 1; i < args.length; i++) {
			 if (args[i].equals("-o")) {
		    	  try {
		    		  outputDir = args[++i];
		    		  File dir = new File(outputDir);
				      System.out.println("Output Files will be found in " + outputDir);
				      if(!dir.exists()){
				    	  dir.mkdirs();
				      }
		            } catch (Exception e) {
		              e.printStackTrace();
		              System.out.println("Output Directory path is missing");
		              System.exit(1);
		            }
		      }
		}
		
		formatLoc= outputDir +"/IdentifiedSQL.log";	
				
		FileInputStream fstream = new FileInputStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		ArrayList<String> ar = new ArrayList<String>();

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			// Print the content on the console
			ar.add(strLine);
		}
		
		QueryExtractor.extract(ar, outputDir);
		SqlFormatter.prettify(formatLoc, outputDir);
		DuplicateQueries.findDuplicates(formatLoc, outputDir);
		SQLPlanObtainerNew.sqlPlan(formatLoc, outputDir);
		//Summary.summarizer(formatLoc);

		br.close();

	}
}
