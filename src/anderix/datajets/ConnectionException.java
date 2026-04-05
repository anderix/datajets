/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Thrown when a DataJet cannot connect to the specified database.
If the database is online and available, check your connection properties in 
<code>setConnection</code>.

@author David M. Anderson
*/
public class ConnectionException extends DataJetException {

	private static final long serialVersionUID = 1L;
	/**
	Class constructor specifying the message to add.
	
	@param msg the message to add
	*/
	public ConnectionException(String msg) {
		super("There was a problem connecting to the database. Check your driver and connection string. " + msg);
	}
}
