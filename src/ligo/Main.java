package ligo;


import java.io.*;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

import ligo.dota.Hero;
import ligo.dota.Match;
import ligo.dota.parser.ParserForHeroes;
import ligo.dota.parser.ParserForMatches;
import ligo.http.HttpConstructor;
import ligo.http.Json;
import ligo.sql.SQLConnection;

public class Main {
	public static void main(String[] args) throws IOException, SQLException, InterruptedException {
		
		Connection mysql = new SQLConnection().connect();
		/*ParserForHeroes heroes = new ParserForHeroes(mysql);
		heroes.parse();*/

		BigInteger lastMatch = new BigInteger("3759915805");
		ParserForMatches opendota = new ParserForMatches(8000, mysql, lastMatch);
		opendota.parse();
		System.in.read();
		System.out.println("operations: "+opendota.operations+", exceptions: "+opendota.exceptions);
		mysql.close();
	}
} 	