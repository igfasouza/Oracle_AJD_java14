package autonomous;

import autonomous.igor.Location;
import autonomous.igor.Places;
import java.util.List;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTests {

	@Test
	public void testRecord(){
		var la = new Location(
				"90210",
				"United States",
				"US",
				List.of(new Places(
						"Beverly Hills",
						"-118.4065",
						"California",
						"CA",
						"34.0901")));


		var expectedLocation = given()
				.get("http://api.zippopotam.us/us/90210")
				.as(Location.class);

		assertEquals(la, expectedLocation);
	}

	@Test
	public void testJsonB(){

		var laAPI = given()
				.get("http://api.zippopotam.us/us/90210")
				.as(Location.class);


		var json = """
			{
					"post code": "90210",
					"country": "United States",
					"country abbreviation": "US",
					"places": [
					           {
					        	   "place name": "Beverly Hills",
					        	   "longitude": "-118.4065",
					        	   "state": "California",
					        	   "state abbreviation": "CA",
					        	   "latitude": "34.0901"
					           }
					           ]
			}""";

		var jsonb = JsonbBuilder.create(
				new JsonbConfig()
				.withFormatting(true));

		var serialized = jsonb.toJson(laAPI);
		var deserialized = jsonb.fromJson(json, Location.class);

		assertEquals(deserialized, laAPI);
		assertEquals(serialized, json);

	}

	@Test
	public void testJackson(){

		var laAPI = given()
				.get("http://api.zippopotam.us/us/90210")
				.as(Location.class);

		var json = """
			{
					"post code": "90210",
					"country": "United States",
					"country abbreviation": "US",
					"places": [
					           {
					        	   "place name": "Beverly Hills",
					        	   "longitude": "-118.4065",
					        	   "state": "California",
					        	   "state abbreviation": "CA",
					        	   "latitude": "34.0901"
					           }
					           ]
			}""";

		final ObjectMapper mapper = new ObjectMapper()
			      .enable(SerializationFeature.INDENT_OUTPUT);
		
	    var serialized = mapper.writeValueAsString(laAPI);
	    var deserialized = mapper.readValue(json, Location.class);
	    
	    assertEquals(deserialized, laAPI);
	    assertEquals(serialized, json);

	}

}