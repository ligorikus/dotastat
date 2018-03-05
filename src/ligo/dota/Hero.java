package ligo.dota;

import java.util.*;

import ligo.http.Json;

public class Hero {
	public int hero_id;
	public String localized_name;
	public String primary_attr;
	public String attack_type;
	
	public Hero(Json response)
	{
		Map<String, Object> hero = response.getJsonMap();
		
		this.hero_id = Integer.parseInt((String) hero.get("id"));
		this.localized_name = (String) hero.get("localized_name");
		this.primary_attr = (String) hero.get("primary_attr");
		this.attack_type = (String) hero.get("attack_type");
	}
	
	public String toString()
	{
		return "("+this.hero_id+",\""+this.localized_name+"\",\""+this.primary_attr+"\"," +
				"\""+this.attack_type+"\")";
		
	}
}
