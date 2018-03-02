package ligo;


import java.io.*;
import java.sql.*;

import ligo.dota.parser.ParserForMatches;
import ligo.sql.SQLConnection;


public class Main {
	public static void main(String[] args) throws IOException, SQLException, InterruptedException {
		
		Connection mysql = new SQLConnection().connect();
		
		String lastMatch = "3746470900";
		ParserForMatches opendota = new ParserForMatches(1000, mysql, lastMatch);
		opendota.parse();
		System.in.read();
		System.out.println("operations: "+opendota.operations+", exceptions: "+opendota.exceptions);
		mysql.close();
	}
} 	