

import com.google.gson.Gson;

import java.util.List;

public record Location(
		String postCode,
        String country,
        String countryAbbreviation,
        List<Places>places) { 
	
	
	@Override
	public String toString() {
		Gson gson = new Gson();    
		return gson.toJson(this);

	}
	
}