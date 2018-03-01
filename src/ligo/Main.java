package ligo;


import java.io.*;
import java.sql.*;
import java.util.*;

import ligo.dota.Match;
import ligo.http.*;
import ligo.sql.SQLConnection;


public class Main {
	public static void main(String[] args) throws IOException, SQLException {
		
		Connection mysql = new SQLConnection().connect();
		
		HttpConstructor opendota = new HttpConstructor();
		
		String lastMatch = "";
		
		for(int i=0; i < 1; i++)
		{

			ArrayList<Json> matchesJson = opendota.getMatches(lastMatch).getArray();
			ArrayList<Match> matches = new ArrayList<Match>();
			
			for(Json match : matchesJson)
			{
				matches.add(new Match(match));
			}
			
			try {
				Statement insertMatches = mysql.createStatement();
				String sqlQueryInsertMatches = "insert into `matches` (`match_id`,`radiant_win`,`duration`,`avg_mmr`,`radiant_team`, `dire_team`) values";
				for(Match match :matches)
				{
					sqlQueryInsertMatches += match.toString()+",";
				}
				sqlQueryInsertMatches = sqlQueryInsertMatches.substring(0, sqlQueryInsertMatches.length()-1); 
				
				insertMatches.execute(sqlQueryInsertMatches);
				insertMatches.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			lastMatch =	matches.get(99).match_id;
			System.out.println("Completed "+i);
		}
		mysql.close();
	}
} 	