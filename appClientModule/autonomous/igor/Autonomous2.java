package autonomous.igor;

import static io.restassured.RestAssured.given;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import javax.json.Json;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

import org.eclipse.yasson.YassonJsonb;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;

import oracle.soda.OracleCollection;
import oracle.soda.OracleCursor;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleDocument;
import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonGenerator;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;


public class Autonomous2 {


	public static void main(String[] args) throws Exception {

		Connection conn = null;
		
		PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
		pds.setConnectionFactoryClassName("oracle.jdbc.replay.OracleConnectionPoolDataSourceImpl");
		pds.setURL("jdbc:oracle:thin:@dbname_high?TNS_ADMIN=/path/for/your/Wallet_xxx");
		pds.setUser("admin");
		pds.setPassword("ZZZzzz");
		pds.setConnectionPoolName("JDBC_UCP_POOL");

		conn = pds.getConnection();

		OracleRDBMSClient cl = new OracleRDBMSClient();

		OracleDatabase db = cl.getDatabase(conn);

		OracleCollection col = db.admin().createCollection("MyJSONCollection7");

		var laAPI = given()
				.get("http://api.zippopotam.us/us/90210")
				.as(Location.class);


		OracleJsonFactory factory = new OracleJsonFactory();
		
		// JSON-B
		YassonJsonb jsonb = (YassonJsonb) JsonbBuilder.create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JsonGenerator gen = factory.createJsonBinaryGenerator(out).wrap(JsonGenerator.class);
		jsonb.toJson(laAPI, gen);
		gen.close();
		
		//Jackson
		JsonFactory factoryJackson = new JsonFactory();
		ByteArrayOutputStream outJackson = new ByteArrayOutputStream();
        OracleJsonGenerator genJackson = factory.createJsonBinaryGenerator(outJackson);
        com.fasterxml.jackson.core.JsonParser jacksonParser = factoryJackson.createParser(laAPI.toString());
        JsonToken token;
        while ((token = ((com.fasterxml.jackson.core.JsonParser) jacksonParser).nextToken()) != null) {
            switch(token) {
            case START_OBJECT:
            	genJackson.writeStartObject();
                break;
            case START_ARRAY:
            	genJackson.writeStartArray();
                break;
            case END_ARRAY:
            case END_OBJECT:
            	genJackson.writeEnd();
                break;
            case FIELD_NAME:
            	genJackson.writeKey(((com.fasterxml.jackson.core.JsonParser) jacksonParser).currentName());
                break;
            case VALUE_FALSE:
            	genJackson.write(false);
                break;
            case VALUE_TRUE:
            	genJackson.write(true);
                break;                
            case VALUE_NULL:
            	genJackson.writeNull();
                break;
            case VALUE_NUMBER_FLOAT:
            case VALUE_NUMBER_INT:
            	genJackson.write(((com.fasterxml.jackson.core.JsonParser)jacksonParser).getDecimalValue());
                break;
            case VALUE_STRING:
            	genJackson.write(((com.fasterxml.jackson.core.JsonParser)jacksonParser).getText());
                break;
            default:
                throw new IllegalStateException(token.toString());
            }
        }
        jacksonParser.close();
        genJackson.close();
		
		
		//OracleDocument doc = db.createDocumentFrom(out.toByteArray());
		OracleDocument doc = db.createDocumentFrom(outJackson.toByteArray());
		
		col.insert(doc);

		
		OracleCursor c = null;
		try{
			c = col.find().getCursor();
			OracleDocument resultDoc;

			while (c.hasNext()){
				resultDoc = c.next();

				System.out.println ("Key:         " + resultDoc.getKey());
				System.out.println ("Content:     " + resultDoc.getContentAsString());
				System.out.println ("Version:     " + resultDoc.getVersion());
				System.out.println ("Last modified: " + resultDoc.getLastModified());
				System.out.println ("Created on:    " + resultDoc.getCreatedOn());
				System.out.println ("Media:         " + resultDoc.getMediaType());
				System.out.println ("\n");
			}
		}
		finally{
			if (c != null) c.close();
		}


	}

}
