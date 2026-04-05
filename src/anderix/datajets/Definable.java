/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Specifies that a DataJet will expose methods to create
a physical table corresponding to the DataJet.

@author David M. Anderson
*/
public interface Definable {

	/*
	Returns the TableDefinition object used to define the physical table.

	@return TableDefinition used to define table

	public TableDefinition getTableDefinition();
	*/
	
	/**
	Creates a physical database table to correspond to the DataJet.
	
	@param dropTableFirst set to true to drop existing table before creating table 
	@throws ConnectionException		if the connection information provided to 
									the DataJet object is incorrect
	@throws DataJetException		if the DataJet has not been initialized
	*/
	public void createTable(boolean dropTableFirst) throws ConnectionException, DataJetException;
}