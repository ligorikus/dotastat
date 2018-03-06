package ligo.dota.parser;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ligo.dota.Match;
import ligo.http.HttpConstructor;
import ligo.http.Json;

public class ParserForMatches {
	private int countRequests = 10000;
	private BigInteger lastMatch;
	Connection mysql = null;
	
	public int operations = 0;
	public int exceptions = 0;
	
	public ParserForMatches(int count_requests, Connection mysql, BigInteger last_match)
	{
		this.countRequests = count_requests;
		this.lastMatch = last_match;
		this.mysql = mysql;
	}
	
	public int parse() throws InterruptedException
	{
		HttpConstructor opendota = new HttpConstructor();
		int operations = 0, exceptions = 0;
		String strLastMatch = "";
		if(this.lastMatch.intValue() == 0)
		{
			strLastMatch = "";
		}
		else{
			strLastMatch += this.lastMatch;
		}

		for(int i=0; i < this.countRequests; i++)
		{
			ArrayList<Json> matchesJson = null;
			try{
				matchesJson = opendota.getMatches(strLastMatch).getArray();
			} catch(Exception e) {
				System.out.println("Exception "+exceptions++);
				continue;
			}
			ArrayList<Match> matches = new ArrayList<Match>();
			
			for(Json match : matchesJson)
			{
				matches.add(new Match(match));
			}
			
			this.insertMatches(matches);
			
			strLastMatch = matches.get(99).match_id.toString();
			System.out.println("Completed "+i);
			operations++;
			Thread.sleep(300);
		}
		this.operations = operations;
		this.exceptions = exceptions;
		return 0;
	}
	
	public int insertMatches(ArrayList<Match> matches)
	{
		try {
			Statement insertMatches = this.mysql.createStatement();
			Statement insertMatchHeroes = this.mysql.createStatement();
			
			String sqlQueryInsertMatches = "insert into `matches` (`match_id`,`radiant_win`,`duration`,`avg_mmr`,`start_time`) values";
			String sqlQueryInsertMatchHeroes = "insert into `match_heroes` (`match_id`, `hero_id`, `is_radiant`) values";
			
			for(Match match :matches)
			{
				sqlQueryInsertMatches += match.toString()+", ";
				
				for(Integer hero : match.radiant_team)
				{
					sqlQueryInsertMatchHeroes += "("+match.match_id+","+ hero +",1),";
				}
				for(Integer hero : match.dire_team)
				{
					sqlQueryInsertMatchHeroes += "("+match.match_id+","+ hero +",0),";
				}
			}
			//insert matches
			sqlQueryInsertMatches = sqlQueryInsertMatches.substring(0, sqlQueryInsertMatches.length()-2); 
			sqlQueryInsertMatchHeroes = sqlQueryInsertMatchHeroes.substring(0, sqlQueryInsertMatchHeroes.length()-1);
			//System.out.println(sqlQueryInsertMatchHeroes);
			insertMatches.execute(sqlQueryInsertMatches);
			insertMatchHeroes.execute(sqlQueryInsertMatchHeroes);
			
			insertMatches.close();
			insertMatchHeroes.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
}
