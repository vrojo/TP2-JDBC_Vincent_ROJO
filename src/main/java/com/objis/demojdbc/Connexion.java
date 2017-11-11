package com.objis.demojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class Connexion {
    protected static Logger log =  Logger.getLogger(Connexion.class);
    private String dataBaseURL;
    private String driverName;
    private String dbUser;
    private String dbPwd;
    private String dbQuery;
    
    public Connexion(String dabaBaseURL_p, String driverName_p, String dbUser_p, String dbPwd_p, String dbQuery_p){
	this.dataBaseURL = dabaBaseURL_p;
	this.driverName = driverName_p;
	this.dbUser = dbUser_p;
	this.dbPwd = dbPwd_p;
	this.dbQuery = dbQuery_p;
    }
    
    
    public void select(){
	String url = this.dataBaseURL;
	String login = this.dbUser;
	String passwd = this.dbPwd;
	Connection cn = null;
	Statement st = null;
	ResultSet rsFields = null;
        ResultSet rsRequestAnswer = null;
        
        String queryUPPER = this.dbQuery.toUpperCase();
        String[] differentFields = queryUPPER.split("FROM");
        queryUPPER = differentFields[0].replace("SELECT ", "");
        String tablePart = differentFields[1];
        String table[] = tablePart.split(" ", 2);
        queryUPPER = queryUPPER.replace(" ", "");
        String[] fieldList = queryUPPER.split(",");
        
        
        
		
	try{
            Class.forName(this.driverName);
            cn = DriverManager.getConnection(url, login, passwd);
            log.debug("Connexion to the JDBC driver successfull");
            st = cn.createStatement();
            String sqlFields = "SHOW COLUMNS FROM " + table[1];
            String sql = this.dbQuery;
            log.debug("The request are ready :\n" + sqlFields + "\n" + sql + "\n");
            rsFields = st.executeQuery(sqlFields);
            log.debug("execution of the request to find the fields");
            String fieldsNames = "|  ";

            
            while(rsFields.next()){
                for (int i=0; i <= fieldList.length - 1; i++){
                    fieldList[i] = fieldList[i].toLowerCase();
                    fieldList[i] = fieldList[i].replace(" ", "");
                    if (rsFields.getString(1).equals(fieldList[i])){
                        fieldsNames = fieldsNames + rsFields.getString(1) + "  |  ";
                    }
                }
            }
            System.out.println(fieldsNames);
            
            rsRequestAnswer = st.executeQuery(sql);
            log.debug("execution of the user's request");
            String line;
            while(rsRequestAnswer.next()){
                line = "-> ";
		for (int i=0; i <= fieldList.length - 1; i++){
                    fieldList[i] = fieldList[i].toLowerCase();
                    line = line + rsRequestAnswer.getString(fieldList[i]) + "  |  ";
                }
                System.out.println(line);
            }
	} catch(SQLException e){
            e.printStackTrace();
            System.out.println("\n\n-------------------------------------------------------------------------------------------------");
            System.out.println("There is an error in the request ! \nEither is the request invalid (field doesn't exist, table/column doesn'ot exist)");
            System.out.println("-------------------------------------------------------------------------------------------------");
	} catch(ClassNotFoundException e){
            e.printStackTrace();
	} finally {
            try{
		cn.close();
		st.close();
            } catch(SQLException e){
		e.printStackTrace();
            }
	}
    }
    
    
    public void insert_update_delete(){
	String url = this.dataBaseURL;
	String login = this.dbUser;
	String passwd = this.dbPwd;
	Connection cn = null;
	Statement st = null;
	int rs;
		
	try{
            Class.forName(this.driverName);
            cn = DriverManager.getConnection(url, login, passwd);
            log.debug("Connexion to the JDBC driver successfull");
            st = cn.createStatement();
            String sql = this.dbQuery;
            log.debug("request ready :\n" + sql + "\n");
            rs = st.executeUpdate(sql);
            System.out.println("mon RS: "+ rs);
            log.debug("Throw the answer depending on the result of the request");
            if(rs == 0){
		if(this.dbQuery.regionMatches(0, "UPDATE", 0, 5) == true) {
                    System.out.println("No modification was made.");
		}else if(this.dbQuery.regionMatches(0, "DELETE", 0, 5) == true) {
                    System.out.println("No line deleted.");
		}else if(this.dbQuery.regionMatches(0, "INSERT", 0, 5) == true) {
                    System.out.println("No line modified.");
		}else if(this.dbQuery.regionMatches(0, "CREATE", 0, 5) == true) {
                    System.out.println("table(s) was(were) created successfully");
		}else if(this.dbQuery.regionMatches(0, "DROP ", 0, 5) == true) {
                    System.out.println("table(s) was(were) deleted successfully");
		}
            }else{
		if(this.dbQuery.regionMatches(0, "UPDATE", 0, 5) == true) {
                    System.out.println("Number of updated line(s) : " + rs);
		}else if(this.dbQuery.regionMatches(0, "DELETE", 0, 5) == true) {
                    System.out.println("Number of deleted line(s) : " + rs);
                }else if(this.dbQuery.regionMatches(0, "INSERT", 0, 5) == true) {
                    System.out.println("Number of modified line(s) : " + rs);
		}
            }
	} catch(SQLException e){
            //e.printStackTrace();
            System.out.println("\n\n-------------------------------------------------------------------------------------------------");
            System.out.println("There is an error in the request ! \nEither is the request invalid (field doesn't exist, table/column doesn'ot exist or modification cann't be made because of dependencies)");
            System.out.println("-------------------------------------------------------------------------------------------------");
	} catch(ClassNotFoundException e){
            e.printStackTrace();
	} finally {
            try{
		cn.close();
		st.close();
            } catch(SQLException e){
		e.printStackTrace();
            }
	}
    }
    
    
    /**
     * List of the different Getters (pas utiles
     */
    protected String getURL(){
        return dataBaseURL;
    }
    protected String getDriver(){
	return driverName;
    }
    protected String getUser(){
    	return dbUser;
    }
    protected String getPwd(){
	return dbPwd;
    }
    protected String getQuery(){
	return dbQuery;
    }
}