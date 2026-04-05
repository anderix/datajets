/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
The class for initializing DataJet objects and methods. JetSet
describes the connection, table, field list, and (optionally) driver used for a DataJet.

@author David M. Anderson
@see DataJet
@see GenericDataJet
*/
public class JetSet {
	private String driver = null;
	private String connectionString = null;
	private ConnectionGenerator connectionGenerator = null;
	private boolean useConnectionGenerator = false;
	private String table = null;
	private String[] fieldList = null;

	private JetSet() {
	
	}

	/**
	Constructor.
	
	@param connectionString string representing the connection to the database
	@param table string name of the DataJet's table
	@param fieldList array of strings containing the field names, with 
			the identity field in the zeroth ordinal position
	*/
	public JetSet(String connectionString, String table, String[] fieldList) {
		setConnection(driver, connectionString);
		setTable(table);
		setFieldList(fieldList);
	}

	/**
	Constructor.
	
	@param driver string representing the database driver to use to create a connection
	@param connectionString string representing the connection to the database
	@param table string name of the DataJet's table
	@param fieldList array of strings containing the field names, with 
			the identity field in the zeroth ordinal position
	*/
	public JetSet(String driver, String connectionString, String table, String[] fieldList) {
		setConnection(driver, connectionString);
		setTable(table);
		setFieldList(fieldList);
	}
	
	/**
	Constructor.
	
	@param generator ConnectionGenerator that defines connection for the DataJet
	@param table string name of the DataJet's table
	@param fieldList array of strings containing the field names, with 
			the identity field in the zeroth ordinal position
	*/
	public JetSet(ConnectionGenerator generator, String table, String[] fieldList) {
		setConnection(generator);
		setTable(table);
		setFieldList(fieldList);
	}

	/**
	Constructor.
	
	@param driver string representing the database driver to use to create a connection
	@param connectionString string representing the connection to the database
	@param definition TableDefinition object that describes the physical database table.
	*/
	public JetSet(String driver, String connectionString, TableDefinition definition) {
		setConnection(driver, connectionString);
		setTable(definition.getTable());
		setFieldList(definition.getFieldNames());
	}
	
	/**
	Constructor.
	
	@param generator ConnectionGenerator that defines connection for the DataJet
	@param definition TableDefinition object that describes the physical database table.
	*/
	public JetSet(ConnectionGenerator generator, TableDefinition definition) {
		setConnection(generator);
		setTable(definition.getTable());
		setFieldList(definition.getFieldNames());
	}

	/**
	Finds the index of a field that matches <code>fieldName</code>. Returns zero 
	(0) if no match is found.
	
	@param fieldName name of the field to find
	@return int value representing index of matching field
	*/
	public int indexOf(String fieldName) {
		int rval = 0;
		for ( int i = 0; i < fieldList.length; i++ ) {
			if ( fieldList[i].equalsIgnoreCase(fieldName) ) {
				rval = i;
			}
		}
		return rval;
	}
	
	/**
	Returns the name of the field indentified by <code>fieldIndex</code>.
	
	@param fieldIndex the index of the field to return
	@return string name of the field
	*/
	public String getFieldName(int fieldIndex) {
		return fieldList[fieldIndex];
	}

	/**
	Sets the connection information for the DataJet.
	
	@param newvalue ConnectionGenerator containing connection information for the DataJet
	@see ConnectionGenerator
	*/
	public void setConnection(ConnectionGenerator newvalue) {
		connectionGenerator = newvalue;
		useConnectionGenerator = true;
	}

	/**
	Sets the connection information for the DataJet.
	
	@param driverValue string containing driver information for the DataJet
	@param connectionStringValue string containing connection url for the DataJet
	*/
	public void setConnection(String driverValue, String connectionStringValue) {
		driver = driverValue;
		connectionString = connectionStringValue;
	}

	/**
	Creates a new java.sql.Connection object based on the connection 
	information supplied to the DataJet.
	
	@return Connection object for the DataJet
	@throws DataJetException if the connection information has not been 
			supplied or is invalid
	*/
	public Connection createConnection() throws DataJetException {
		Connection con = null;
		if ( useConnectionGenerator ) {
			if ( connectionGenerator == null ) {
				throw new ConnectionException("ConnectionGenerator is null.");
			} else {
				con = connectionGenerator.createConnection();
			}
		} else {
			if ( connectionString == null ) {
				throw new ConnectionException("Connection string is null.");
			} else {
				try {
					if ( driver != null ) {
						Class.forName(driver); // the old way to load JDBC drivers. Optional since Java 1.6, JDBC 4.0 API
					}
					con = DriverManager.getConnection(connectionString);
				} catch ( ClassNotFoundException cnfex ) {
					throw new ConnectionException(cnfex.getMessage());
				} catch ( SQLException sqlex ) {
					throw new ConnectionException(sqlex.getMessage());
				}
			}
		}
		return con;
	}
	
	/**
	Closes the connection object.
	
	@param con Connection to close.
	*/
	public void closeConnection(Connection con) {
		if ( useConnectionGenerator ) {
			connectionGenerator.closeConnection(con);
		} else {
			try {
				con.close();
			} catch ( Exception ex ) {

			}
		}		
	}

	/**
	Returns the connection string that was supplied to the DataJet. If no 
	connection string has been specified, or if a ConnectionGenerator was
	specified, returns null.
	
	@return connection string
	*/
	public String getConnectionString() {
		return connectionString;
	}

	/**
	Returns the driver name that was supplied to the DataJet. If no 
	driver name has been specified, or if a ConnectionGenerator was
	specified, returns null.
	
	@return string representing driver name
	*/
	public String getDriver() {
		return driver;
	}

	/**
	Returns the ConnectionGenerator that was supplied to the DataJet. If no
	ConnectionGenerator was supplied, returns null.
	
	@return the DataJet's ConnectionGenerator
	@see ConnectionGenerator
	*/
	public ConnectionGenerator getConnectionGenerator() {
		return connectionGenerator;
	}

	/**
	Sets the table name and the field names for the DataJet.
	
	@param tableName string name of the table
	@param fieldNames array of strings containing the field names, with 
			the identity field in the zeroth ordinal position
	*/
	public void setTable(String tableName, String[] fieldNames) {
		table = tableName;
		fieldList = fieldNames;
	}

	/**
	Sets the table name and the field names for the DataJet using a 
	TableDefinition object.
	
	@param tableDef TableDefinition object to set table and field names
	*/
	public void setTable(TableDefinition tableDef) {
		table = tableDef.getTable();
		fieldList = tableDef.getFieldNames();
	}

	/**
	Sets the table name for the DataJet.
	
	@param newvalue string name of the table
	*/
	public void setTable(String newvalue) {
		table = newvalue;
	}

	/**
	Returns the table name for the DataJet.
	
	@return string table name
	@throws DataJetException if the table has not been specified.
	*/
	public String getTable() {
		return table;
	}

	/**
	Sets the field names for the DataJet.
	
	@param newvalues array of strings containing field names, with 
			the identity field in the zeroth ordinal position
	*/	
	public void setFieldList(String[] newvalues) {
		fieldList = newvalues;
	}

	/**
	Returns the field names for the DataJet.
	
	@return array of strings representing field names
	@throws DataJetException if the table has not been specified
	*/
	public String[] getFieldList() {
		return fieldList;
	}

	/**
	Creates a SqlBuilder based on the table and fields specified by DataJet.
	
	@return SqlBuilder object based on table and fields specified by DataJet
	@throws DataJetException if the table or field list have not been specified
	@see SqlBuilder
	*/
	public SqlBuilder createSqlBuilder() {
		return new SqlBuilder(getTable(), getFieldList(), 0);
	}


}
