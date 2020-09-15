

import java.sql.Connection;
import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleCursor;
import oracle.soda.OracleCollection;
import oracle.soda.OracleDocument;
import oracle.soda.OracleException;
import java.util.List;

public class Autonomous{

	public static void main(String[] arg){

		Connection conn = null;

		try{

			PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
			pds.setConnectionFactoryClassName("oracle.jdbc.replay.OracleConnectionPoolDataSourceImpl");
			pds.setURL("jdbc:oracle:thin:@bdname_high?TNS_ADMIN=/path/for/Wallet_dbname");
			pds.setUser("admin");
			pds.setPassword("ZZZZzzzz");
			pds.setConnectionPoolName("JDBC_UCP_POOL");

			conn = pds.getConnection();

			OracleRDBMSClient cl = new OracleRDBMSClient();

			OracleDatabase db = cl.getDatabase(conn);

			OracleCollection col = db.admin().createCollection("MyJSONCollection2");

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

			OracleDocument doc = db.createDocumentFromString(la.toString());

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

			if (arg.length > 0 && arg[0].equals("drop")) {
				col.admin().drop();
				System.out.println ("\nCollection dropped");
			}
		}
		catch (OracleException e) { e.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		finally {
			try { if (conn != null)  conn.close(); }
			catch (Exception e) { }
		}
	}
}