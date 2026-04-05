/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Specifies that a DataJet will expose a public <code>remove</code> 
method. You may want to implement this interface when creating a class that 
extends DataJet. This interface is intended to provide a common method for 
removing DataJets, regardless of their actual type. If your DataJet will provide 
this functionality, it is recommended that you implement this interface.

@author David M. Anderson
*/
public interface Removable {

	/**
	Removes the record that is currently loaded by the DataJet object. 
	The easiest way to provide this functionality is to override a 
	protected <code>remove</code> method in DataJet.
	
	@throws ConnectionException		if the connection information provided to 
									the DataJet object is incorrect
	@throws DataJetException		if the DataJet has not been initialized
	@see DataJet#remove
	*/
	public void remove() throws ConnectionException, DataJetException;
}