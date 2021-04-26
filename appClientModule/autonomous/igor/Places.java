package autonomous.igor;


import com.google.gson.Gson;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Places(
        @JsonProperty("place name") String getPlaceName,
        @JsonProperty("longitude") String getLongitude,
        @JsonProperty("state") String getState,
        @JsonProperty("state abbreviation") String getStateAbbreviation,
        @JsonProperty("latitude") String getLatitude) { 

	@Override
	public String toString() {
		Gson gson = new Gson();    
		return gson.toJson(this);

	}
	
}