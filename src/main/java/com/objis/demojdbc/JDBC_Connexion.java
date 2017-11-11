package com.objis.demojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.*;

public class JDBC_Connexion {
    protected static Logger log =  Logger.getLogger(JDBC_Connexion.class);
    
    public static void main(String[] args) {
	System.out.println("Request :\nSELECT last_name FROM actor\n\n------------------------------------");
	System.out.println("List of all actors' names :");
	lireEnBase();
    }
    
    public static void lireEnBase(){
	String url = "jdbc:mysql://localhost/sakila";
	String login = "root";
	String passwd = "root";
	Connection cn = null;
	Statement st = null;
	ResultSet rsFields = null;
        ResultSet rsRequestAnswer = null;
		
	try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(url, login, passwd);
            log.debug("Connexion to the JDBC driver successfull");
            st = cn.createStatement();
            String sql = "SHOW COLUMNS FROM actor;";
            rsFields = st.executeQuery(sql);
            log.debug("Execution of the first request :   " + sql);
            st = cn.createStatement();
            String sql2 = "SELECT last_name FROM actor";
            rsRequestAnswer = st.executeQuery(sql2);
            log.debug("Execution of the second request :   " + sql2);
			
            log.debug("Recuperation of the fieldnames searched in the request");
            while(rsFields.next()){
                if(rsFields.getString(1).equals("last_name")){
                    System.out.println("+-------------+\n|  " + rsFields.getString(1) + "  |\n+-------------+");
                }
            }
	
            log.debug("Recuperation of the results of the request");
            while(rsRequestAnswer.next()){
                System.out.println("->  " + rsRequestAnswer.getString("last_name"));
            }
	} catch(SQLException e){
            log.debug("Error encountered with the request text or the execution of the request (if searching for an unknown field, creating an already existing table, etc");
            System.out.println("\n===========================================\n"+
                               "| There was a problem with the request... |\n"+
                               "===========================================\n\n");
            e.printStackTrace();
	} catch(ClassNotFoundException e){
            System.out.println("\n==========================================================\n"+
                               "| There is a problem with the connexion to the driver... |\n"+
                               "==========================================================\n\n");
            //e.printStackTrace();
	} finally {
            try{
                cn.close();
                st.close();
                log.debug("Close the connexion");
            } catch(SQLException e){
                e.printStackTrace();
            } catch (NullPointerException e){
                System.out.println("The connexion to the database failed...");
            }
	}
    }
}
