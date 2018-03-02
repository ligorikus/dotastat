package ligo.dota.parser;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ligo.dota.Match;
import ligo.http.HttpConstructor;
import ligo.http.Json;

public class ParserForMatches {
	private int countRequests = 1000;
	private String lastMatch = "";
	Connection mysql = null;
	
	public int operations = 0;
	public int exceptions = 0;
	
	public ParserForMatches(int count_requests, Connection mysql, String last_match)
	{
		this.countRequests = count_requests;
		this.lastMatch = last_match;
		this.mysql = mysql;
	}
	
	public int parse() throws InterruptedException
	{
		HttpConstructor opendota = new HttpConstructor();
		int operations = 0, exceptions = 0;
		
		for(int i=0; i < this.countRequests; i++)
		{
			ArrayList<Json> matchesJson = null;
			try{
				matchesJson = opendota.getMatches(this.lastMatch).getArray();
			} catch(Exception e) {
				System.out.println("Exception "+exceptions++);
				continue;
			}
			ArrayList<Match> matches = new ArrayList<Match>();
			
			for(Json match : matchesJson)
			{
				matches.add(new Match(match));
			}
			
			try {
				Statement insertMatches = this.mysql.createStatement();
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
				return -1;
			}
			this.lastMatch = matches.get(99).match_id;
			System.out.println("Completed "+i);
			operations++;
			Thread.sleep(300);
		}
		this.operations = operations;
		this.exceptions = exceptions;
		return 0;
	}
}
