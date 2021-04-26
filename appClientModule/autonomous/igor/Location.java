package autonomous.igor;


import com.google.gson.Gson;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.json.bind.annotation.JsonbCreator;
import java.util.List;

public record Location(
        @JsonProperty("post code") String getPostCode,
        @JsonProperty("country") String getCountry,
        @JsonProperty("country abbreviation") String getCountryAbbreviation,
        @JsonProperty("places") List<Places> getPlaces) {
	
    @JsonbCreator
    public Location {}
	
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}