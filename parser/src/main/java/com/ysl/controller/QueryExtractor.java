package com.ysl.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QueryExtractor {

	public static void extract(ArrayList<String> ar, String sqlout){
		sqlout = sqlout +"/IdentifiedSQL.log";
		int x;
		String st2="";
		for(int i=0;i<ar.size();i++) {
			String test = ar.get(i);
		if(test.contains(". select") || test.contains(". update") || test.contains(". insert") || test.contains(". delete")){    	
			for(x=i ; x <ar.size(); x++) {
				st2 = st2+ar.get(x);
				String t1 = ar.get(x);
				if(t1.contains("ms}")){
					x=0;
					String Query=st2.substring(st2.indexOf(". ")+2,st2.length());
					Query = Query.substring(0,Query.indexOf(" {executed"));
					/*if(Query.contains("\"")) {
						Query=Query.replace	("\"", "\\\\\"");
					    }	*/
					//System.out.println(Query);
					BufferedWriter bw = null;
					FileWriter fw = null;
					try {

						File file = new File(sqlout);
						// if file doesnt exists, then create it
						if (!file.exists()) {
							file.createNewFile();
						}
						// true = append file
						fw = new FileWriter(file.getAbsoluteFile(), true);
						bw = new BufferedWriter(fw);
						bw.write( Query + "\n");						
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
					st2="";
					break;		
				}
			}
		}
		}	
		System.out.println("Message: Queries has been extracted from the Log successfully !!");
	}


}
