

import com.google.gson.Gson;

public record Places(
		String placeName,
		String longitude,
		String state,
		String stateAbbreviation,
		String latitude) {

	@Override
	public String toString() {
		Gson gson = new Gson();    
		return gson.toJson(this);

	}
}