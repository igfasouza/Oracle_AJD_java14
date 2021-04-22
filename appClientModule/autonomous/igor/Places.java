package autonomous.igor;


import com.google.gson.Gson;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Places(
        @JsonProperty("place name") String placeName,
        @JsonProperty("longitude") String longitude,
        @JsonProperty("state") String state,
        @JsonProperty("state abbreviation") String stateAbbreviation,
        @JsonProperty("latitude") String latitude) { 

	@Override
	public String toString() {
		Gson gson = new Gson();    
		return gson.toJson(this);

	}
	
}