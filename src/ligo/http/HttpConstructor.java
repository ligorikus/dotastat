package ligo.http;

import java.net.*;
import java.io.*;

public class HttpConstructor {
	
	private String opendotaDefault = "https://api.opendota.com/api/";
	
	public HttpConstructor(){}
	
	public Json getMatches(String last_match)
	{
		String extension = "";
		if(last_match != "")
		{
			extension += "?less_than_match_id="+last_match;
		}
		
		try {
			URL opendota = new URL(opendotaDefault+"publicMatches"+extension);
			BufferedReader response = new BufferedReader(
	        		new InputStreamReader(opendota.openStream())
	        	);
			String inputLine;
	        StringBuilder result = new StringBuilder();
	        while ((inputLine = response.readLine()) != null)
	            result.append(inputLine);
	        response.close();
	        return new Json(new String(result));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
