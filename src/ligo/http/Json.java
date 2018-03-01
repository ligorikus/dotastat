package ligo.http;

import java.util.*;

public class Json {
	private String json_response = "";
	
	public Json(String result)
	{
		this.json_response = new String(result);
	}
	
	public String getJson()
	{
		return this.json_response;
	}
	
	public ArrayList<Json> getArray()
	{
		if(this.json_response.charAt(0) == '[' && this.json_response.charAt(this.json_response.length()-1) == ']')
		{
			ArrayList<Json> json_array = new ArrayList<Json>();
			int counter = 0;
			String tempJsonString = "";
			for(int i = 1; i < this.json_response.length()-1; i++)
			{
				char tempChar = json_response.charAt(i);
				if(counter == 0 && tempChar == ',')
					continue;
				if(tempChar == '{')
					counter++;
				else if(tempChar == '}')
					counter--;
				tempJsonString += tempChar;
				if(counter == 0)
				{
					json_array.add(new Json(tempJsonString));
					tempJsonString = "";
				}
			}
			return json_array;
		}
		//TODO: get this object
		return null;
	}
	
	public Map<String, Object> getJsonMap()
	{
		if(this.json_response.charAt(0) == '{' && this.json_response.charAt(json_response.length()-1) == '}')
		{
			Map<String, Object> map = new HashMap<String, Object>();
			int counter = 0;
			boolean key_flag = true;
			String key = "";
			String value = "";
			boolean quotes = false;
			
			for(int i = 1; i < this.json_response.length(); i++)
			{
				char tempChar = json_response.charAt(i);
				
				//System.out.print(tempChar);
				switch(tempChar)
				{
				case ':':
					key_flag = false;
					break;
				case ',':
					if(quotes)
					{
						value+=tempChar;
						break;
					}
					
					Object valueObj = null;
					
					if(value.charAt(0) == '{' && value.charAt(value.length()-1) == '}')
						valueObj = new Json(value).getJsonMap();
					else
						valueObj = new String(value);

					map.put(key, valueObj);
					key = "";
					value = "";
					key_flag = true;
				case '"':
					if(key_flag)
						break;
					if(quotes)
						quotes = false;
					else
						quotes = true;
					break;
				case '{':
					counter++;
					value += tempChar;
					break;
				case '}':
					counter--;
					if(counter != -1)
					{
						value += tempChar;
					}
					break;
				default:
					
					if(key_flag)
						key += tempChar;					
					else
						value += tempChar;
					break;
				}
				
				
				if(tempChar == '}' && counter == -1)
				{
					Object valueObj = null;
					if(value.charAt(0) == '{' && value.charAt(value.length()-1) == '}')
						valueObj = new Json(value).getJsonMap();
					else
						valueObj = new String(value);
					map.put(key, valueObj);
					break;
				}
			}
			return map;
		}
		return null;
	}
}
