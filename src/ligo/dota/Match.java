package ligo.dota;


import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;

import ligo.http.Json;

public class Match {
	public BigInteger match_id = null;
	public Boolean radiant_win = null;
	public Integer duration = null;
	public String avg_mmr = "";
	public String start_time = null;
	public ArrayList<Integer> radiant_team = null;
	public ArrayList<Integer> dire_team = null;
	
	public Match(Json match_response)
	{
		Map<String, Object> match = match_response.getJsonMap();
		
		this.match_id = new BigInteger((String) match.get("match_id"));
		this.radiant_win = Boolean.parseBoolean((String) match.get("radiant_win"));
		this.duration = Integer.parseInt((String) match.get("duration"));
		this.avg_mmr = (String) match.get("avg_mmr");
       
		long startTime = Long.parseLong((String) match.get("start_time"))*1000L;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( startTime );
		this.start_time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cal.getTime());
		
		this.radiant_team = getTeam((String) match.get("radiant_team"));	
		this.dire_team = getTeam((String) match.get("dire_team"));
	}
	
	
	public String toString()
	{
		return "("+this.match_id+","+this.getRadiantWin()+","+this.duration+",\""+this.avg_mmr+"\",\""+this.start_time+"\")";
	}
	
	public int getRadiantWin()
	{
		if(this.radiant_win)
			return 1;
		return 0;
	}
	
	public ArrayList<Integer> getTeam(String team)
	{
		ArrayList<Integer> teamInt = new ArrayList<Integer>();
		for(String hero : team.split(","))
		{
			teamInt.add(Integer.parseInt(hero));
		}
		return teamInt;
	}
}
