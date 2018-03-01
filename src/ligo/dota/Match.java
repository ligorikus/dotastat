package ligo.dota;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ligo.http.Json;

public class Match {
	public String match_id = "";
	public Boolean radiant_win = null;
	public Integer duration = null;
	public String avg_mmr = "";
	public String radiant_team = "";
	public String dire_team = "";
	
	public Match(Json match_response)
	{
		Map<String, Object> match = match_response.getJsonMap();
		
		this.match_id = (String) match.get("match_id");
		this.radiant_win = Boolean.parseBoolean((String) match.get("radiant_win"));
		this.duration = Integer.parseInt( (String) match.get("duration"));
		this.avg_mmr = (String) match.get("avg_mmr");
		this.radiant_team = (String) match.get("radiant_team");	
		this.dire_team = (String) match.get("dire_team");
	}
	
	public String toString()
	{
		return "(\""+this.match_id+"\","+this.getRadiantWin()+","+this.duration+",\""+this.avg_mmr+"\",\"" +
				this.getTeam(this.radiant_team)+"\",\""+this.getTeam(this.dire_team)+"\")";
	}
	
	public int getRadiantWin()
	{
		if(this.radiant_win)
			return 1;
		return 0;
	}
	
	public String getTeam(String team)
	{
		ArrayList<Integer> teamInt = new ArrayList<Integer>();
		for(String hero : team.split(","))
		{
			teamInt.add(Integer.parseInt(hero));
		}
		Collections.sort(teamInt);
		String result = "";
		for(Integer hero : teamInt)
		{
			result += "FF"+ String.format("%1$02X", hero);
		}
		return result;
	}
}
