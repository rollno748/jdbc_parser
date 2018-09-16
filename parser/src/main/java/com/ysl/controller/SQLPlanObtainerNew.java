package com.ysl.controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class SQLPlanObtainerNew { 

	public static void sqlPlan(String filepath, String outDir) throws IOException, ClassNotFoundException {

		//Class.forName("oracle.jdbc.driver.OracleDriver"); 
		String URL = "";
		String username="";
		String password = "";
		InputStream inputStream = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		Connection conn = null;
		String queryExplainPlan = outDir + "/queryExplainPlan.log";
		//static Connection getConnection(String url, String user, String password);
		try {			
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = SQLPlanObtainerNew.class.getClassLoader().getResourceAsStream(propFileName);


			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException(propFileName +" file not found in the classpath");
			}

			// get the property value and print it out
			URL = prop.getProperty("config.db.url");
			username = prop.getProperty("db.username");
			password = prop.getProperty("db.password");

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}

		try{				

			conn = DriverManager.getConnection(URL, username, password);
			if (conn != null) {
				System.out.println("Message: Connection with database has been established successfully!!");
			}
			Statement stmt = conn.createStatement();
			FileInputStream fstream = new FileInputStream(filepath);					
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			try {

				File file = new File(queryExplainPlan);
				// if file doesn't exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				// true = append file
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);
				bw.write("\n==================================================================== Query Explain Plan ========================================================================\n");

				while ((strLine = br.readLine()) != null)   {
					// Print the content on the console
					//ar.add(strLine);
	
					ResultSet rs=null;
					String query="explain plan for "+strLine;
					//Execute a SQL SELECT query, the query result
					stmt.execute(query);
					rs = stmt.executeQuery("select plan_table_output from table(dbms_xplan.display())");

					bw.write("\n"+"Query :\n"+strLine + "\n\n");
					while (rs.next()) 	{
						//bw.write( query + "\n");
						bw.write( rs.getString(1)+"\n");
					}
					bw.write("\n==================================================================================================================================================================\n");
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
			conn.close();

		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		System.out.println("Message: Explain plan for the respective queries has been generated successfully !!");
	}
}