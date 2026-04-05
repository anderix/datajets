/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Thrown when a DataJet attempts to access a method before
it has been initialized. To completely initialize an object that inherits 
from DataJet, you must call all of the following static methods: 
<ul>
<li><code>setConnection</code></li>
<li><code>setTable</code></li>
<li><code>setFieldList</code></li>
</ul>
When calling <code>setConnection</code>, ensure that the driver and
connection string are valid, or supply a valid java.sql.Connection object.
<p>
To completely initialize an object that inherits from GenericDataJet, you may
call the methods exposed by DataJet (above) or call one of GenericDataJet's
<code>jetSet</code> methods.

@author David M. Anderson
*/
public class DataJetException extends java.lang.Exception {
	
	private static final long serialVersionUID = 1L;
	/**
	Class constructor specifying the message to add.
	
	@param msg the message to add
	*/
	public DataJetException(String msg) {
		super("The DataJet encountered a problem. " + msg);
	}

}
