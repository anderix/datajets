/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2022,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.sql.Connection;

/**
Used to manage java.sql.Connection objects required by DataJets. The
DataJet class can create and manage its own Connection objects, or you can
manage them yourself using a ConnectionGenerator. Creating a class that
implements ConnectionGenerator allows you to do the following:

<ul>
<li>Share the same connection information between several DatJets</li>
<li>Use a Connection object obtained from a connection pool.</li>
</ul>

@author David M. Anderson
*/
public interface ConnectionGenerator {

	/**
	Establishes the connection to be used by a DataJet. 
	A typical implementation contains:
	<pre>
	String url = [insert connection url];
	Connection con = DriverManager.getConnection(url);
	return con;
	</pre>	
	
	@throws ConnectionException if there is a problem creating a connection to the database
	*/
	public Connection createConnection() throws ConnectionException;

	/**
	Frees resources after DataJet is finished using the Connection. 
	A typical implementation contains:
	<pre>
    try {
        con.close();
    } catch ( Exception ex ) {
    	//no action required - connection is already closed.
    }
	</pre>	
	*/
	public void closeConnection(Connection con);
}