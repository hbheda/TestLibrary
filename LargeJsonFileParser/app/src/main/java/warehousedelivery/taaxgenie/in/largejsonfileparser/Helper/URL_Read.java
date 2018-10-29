package warehousedelivery.taaxgenie.in.largejsonfileparser.Helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class URL_Read {
	
	public static String ReadFromURL(InputStream in){
		String output="";
		  StringBuilder sb=null;
	  	  BufferedReader reader = null;
	  	  try {
	  	    reader = new BufferedReader(new InputStreamReader(in));
	  	    String line = "";
	  	  sb = new StringBuilder();
	  	    while ((line = reader.readLine()) != null) {
	  	    	output+=line;
	  	    }}
	  	    catch(Exception e)
	  	    {
	  	    	e.printStackTrace();
	  	    }
		return output;
	}
}
