package com.objis.demojdbc;

import java.sql.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

public class ReverseEngineering {
    protected static Logger log =  Logger.getLogger(ReverseEngineering.class);
    private String dataBaseURL;
    private String driverName;
    private String dbUser;
    private String dbPwd;
    
    public ReverseEngineering(String dabaBaseURL_p, String driverName_p, String dbUser_p, String dbPwd_p){
	this.dataBaseURL = dabaBaseURL_p;
	this.driverName = driverName_p;
	this.dbUser = dbUser_p;
	this.dbPwd = dbPwd_p;
    }
    
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/sakila";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "root";
	ReverseEngineering connexion = new ReverseEngineering(url, driver, user, password);
        connexion.ConnexionToServer();
    }
    
    
    public void ConnexionToServer(){
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
            GetDataFromDB(cn);
	} catch(SQLException e){
            e.printStackTrace();
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
     * Method to get the structure + data from the database which is on the server
     */
    private static void GetDataFromDB(Connection cn) {	 
        try {
            DatabaseMetaData databaseMetaData = cn.getMetaData();
            String catalog = null;
            String schemaPattern = null;
            String tableNamePattern = null;
            String schema = null;
            String columnNamePattern = null;
            String[] types = null;

            deleteFile();
            ResultSet rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
            String tableName ="";
            
            System.out.println("+----------------------------------------------+\n");
            System.out.println("|     Global SQL Script for the dataBase :     |\n");
            System.out.println("+----------------------------------------------+\n\n");
            while(rs.next()) {
                String tableType = rs.getString("TABLE_TYPE");
                String sqlFullInstructionsScript;
                if (tableType.equals("VIEW")){
                    sqlFullInstructionsScript = "CREATE VIEW ";
                }else {
                    sqlFullInstructionsScript = "CREATE TABLE ";
                }
                tableName = rs.getString(3);

                sqlFullInstructionsScript += (tableType.equals("VIEW"))? tableName + " AS \n" : tableName + " ( \n";				
                ResultSet cresult = databaseMetaData.getColumns(catalog, schemaPattern,  tableName, columnNamePattern);

                if (tableType.equals("VIEW")) {
                    sqlFullInstructionsScript += "SELECT ";
                    int j = 0;
                    while(cresult.next()){
                        String columnName = cresult.getString(4);
                        sqlFullInstructionsScript += (j == 0)? columnName : ", " + columnName ;
                        j++;
                    }
                    sqlFullInstructionsScript += "\n";					
                }else {
                    while(cresult.next()){
                        String columnName = cresult.getString(4);
                        String datatype = cresult.getString("DATA_TYPE");
                        String typeName = cresult.getString("TYPE_NAME");
                        String columnsize = cresult.getString("COLUMN_SIZE");
                        String decimaldigits = cresult.getString("DECIMAL_DIGITS");
                        String typeNameNumbers = (columnsize != null)? "(" + columnsize + ")" : "";
                        String isNullable = cresult.getString("IS_NULLABLE");
                        String isNullableBool = (isNullable == "YES") ? "NULL" : "NOT NULL";
                        String is_autoIncrment = cresult.getString("IS_AUTOINCREMENT");
                        String isAutoIncrementBool = (is_autoIncrment == "YES")? " AUTO_INCREMENT": "";
                        sqlFullInstructionsScript += columnName + " " + typeName + typeNameNumbers + " " + isNullableBool + isAutoIncrementBool + ", \n";
                    }
					
                    ResultSet presult = databaseMetaData.getPrimaryKeys(catalog, schema, tableName);
                    ResultSet foreignKey = databaseMetaData.getImportedKeys(catalog, schema, tableName);
                    ResultSet uniqueKey = databaseMetaData.getIndexInfo(catalog, schema, tableName, true, true);

                    /**
                     *  get the PRIMARY KEYS
                     */
                    int i = 0; 
                    sqlFullInstructionsScript += "PRIMARY KEY (";
                    while(presult.next()){
                        String columnNamep = presult.getString(4);
                        sqlFullInstructionsScript += (i == 0)? columnNamep : ", " + columnNamep;
                        i++;
                    }	
                    sqlFullInstructionsScript += "), \n";

                    /**
                     * UNIQUE KEYS
                     */
                    int j = 0; 
                    sqlFullInstructionsScript += "UNIQUE (";

                    while(uniqueKey.next()){
                        String uniqueColumnName = uniqueKey.getString("COLUMN_NAME");
                        sqlFullInstructionsScript += (j == 0)? uniqueColumnName : ", " + uniqueColumnName;
                        j++;
                    }
                    sqlFullInstructionsScript += "), \n";

                    /**
                     * get the FOREIGN KEYS
                     */
                    int k = 0; 
                    while(foreignKey.next()){
                        String uniqueColumnName = "FOREIGN KEY ("+ foreignKey.getString("FKCOLUMN_NAME") + ") REFERENCES " + foreignKey.getString("PKTABLE_NAME") + "(" + foreignKey.getString("PKCOLUMN_NAME") + ")";
                        sqlFullInstructionsScript += (k == 0)? uniqueColumnName : ", \n" + uniqueColumnName;
                        k++;
                    }
                    sqlFullInstructionsScript += "\n); \n";
                }
		writeFile(sqlFullInstructionsScript);
                System.out.println(sqlFullInstructionsScript);
            }
	} catch (SQLException e) {
            e.printStackTrace();
	}
    }
    
    /**
     * Method to save the output in a .sql file
     * @param sqlFullInstructions
     */
    public static void writeFile(String sqlFullInstructions){
        try{
            File dbFile = new File("sakilaDataBase.sql");
            FileWriter writer = new FileWriter(dbFile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.newLine();
            bw.write(sqlFullInstructions);
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Method to check if the file already exits, if so it asks the user the instructions
     */
    public static void deleteFile(){
	try {
            File file = new File("sakilaDataBase.sql");
            if (file.exists()) {
                Scanner sc = new Scanner(System.in);
                System.out.println("The dataBase file already exist here, do you want to replace it with the new one ? \n\n(type \"YES\" + \"Enter\" to replace it or any other string to cancel the operation)");
                String answer = sc.nextLine();
                if (answer.equals("YES")){
                    file.delete();
                }else{
                    System.out.println("Operation cancelled.");
                }
	    }
	}catch(Exception e) {
            e.printStackTrace();
	}
    }
}
