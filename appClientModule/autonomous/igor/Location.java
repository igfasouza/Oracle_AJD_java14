package autonomous.igor;


import com.google.gson.Gson;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.json.bind.annotation.JsonbCreator;
import java.util.List;

public record Location(
        @JsonProperty("post code") String postCode,
        @JsonProperty("country") String country,
        @JsonProperty("country abbreviation") String countryAbbreviation,
        @JsonProperty("places") List<Places>places) { 
	
    @JsonbCreator
    public Location {}
	
	
	@Override
	public String toString() {
		Gson gson = new Gson();    
		return gson.toJson(this);
	}
	
}