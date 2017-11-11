package com.objis.demojdbc;

import org.apache.log4j.*;

public class SQLExec {
    protected static Logger log =  Logger.getLogger(SQLExec.class);
    
    public static void main(String[] args) {
		if (args.length == 5){
			Connexion requestInfo = new Connexion(args[0], args[1], args[2], args[3], args[4]);
			if(requestInfo.getQuery().regionMatches(0, "SELECT", 0, 5) == true) {
				requestInfo.select();
			}else {
				requestInfo.insert_update_delete();
			}
		}else {
			System.out.format("\n\n======================================================\n");
			System.out.format("parameters error...\n");
			System.out.format("Please enter the right parameters after the java className as follow in the example :\n\n");
			System.out.format("java -cp C:\\Users\\vinro\\.m2\\repository\\junit\\junit\\3.8.1\\junit-3.8.1.jar;C:\\Users\\vinro\\.m2\\repository\\log4j\\log4j\\1.2.17\\log4j-1.2.17.jar;C:\\Users\\vinro\\.m2\\repository\\mysql\\mysql-connector-java\\5.1.6\\mysql-connector-java-5.1.6.jarC:\\Users\\vinro\\.m2\\repository\\junit\\junit\\3.8.1\\junit-3.8.1.jar;C:\\Users\\vinro\\.m2\\repository\\log4j\\log4j\\1.2.17\\log4j-1.2.17.jar;C:\\Users\\vinro\\.m2\\repository\\mysql\\mysql-connector-java\\5.1.6\\mysql-connector-java-5.1.6.jar;target\\classes SQLExec \"jdbc:mysql://localhost/sakila\" \"com.mysql.jdbc.Driver\" root root \"SELECT last_name FROM actor;\"\n\n");
			System.out.format("======================================================\n\n");
		}
	}
}