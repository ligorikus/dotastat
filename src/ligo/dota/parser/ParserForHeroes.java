package ligo.dota.parser;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ligo.dota.Hero;
import ligo.dota.Match;
import ligo.http.HttpConstructor;
import ligo.http.Json;

public class ParserForHeroes {

	private Connection mysql;
	
	public ParserForHeroes(Connection mysql)
	{
		this.mysql = mysql;
	}

	public int parse()
	{
		HttpConstructor opendota = new HttpConstructor();
		ArrayList<Json> heroesJson = null;
		heroesJson = opendota.getHeroes().getArray();
		
		ArrayList<Hero> heroes = new ArrayList<Hero>();
		for(Json hero : heroesJson)
		{
			heroes.add(new Hero(hero));
		}	

		try {
			Statement insertHeroes = this.mysql.createStatement();
			String sqlQueryInsertHeroes = "insert into `heroes` (`id`,`localized_name`,`primary_attr`,`attack_type`) values";
			for(Hero hero :heroes)
			{
				sqlQueryInsertHeroes += hero.toString()+",";
			}
			sqlQueryInsertHeroes = sqlQueryInsertHeroes.substring(0, sqlQueryInsertHeroes.length()-1); 

			insertHeroes.execute(sqlQueryInsertHeroes);
			insertHeroes.close();
			System.out.println("Heroes insert was completed");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
}
