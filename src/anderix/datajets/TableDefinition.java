/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2021,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
Contains information about a physical database table. TableDefinition
allows a DataJet to contain all the information neccessary to build 
a corresponding table in which to store its data. When using a 
TableDefinition object, it is usually neccessary to include
information about your database platform that may not be portable
to other platforms.

@author David M. Anderson
@see Definable
*/
public class TableDefinition {
	private String tableName;
	private ArrayList<String> fieldNames = new ArrayList<String>();
	private ArrayList<String> fieldProps = new ArrayList<String>();
	
	/**
	Constructor that begins the physical table definition.
	
	@param table name of the physical database table
	@param idField name of the field to use as identity
	@param idFieldProperties string that defines the id field
	*/
	public TableDefinition(String table, String idField, String idFieldProperties) {
		tableName = table;
		fieldNames.add(idField);
		fieldProps.add(idFieldProperties);
	}

	/**
	Adds a field to the table definition.
	
	@param fieldName name of the field to add
	@param fieldProperties string that defines the field
	*/	
	public void addField(String fieldName, String fieldProperties) {
		fieldNames.add(fieldName);
		fieldProps.add(fieldProperties);
	}

	/**
	Returns the name of the table.
	
	@return name of the table
	*/
	public String getTable() {
		return tableName;
	}
	
	/**
	Finds the index of the field specified.
	
	@param fieldName name of field to find index of
	@return index of the field; or -1 if not found
	*/	
	public int indexOf(String fieldName) {
		return fieldNames.indexOf(fieldName);
	}
	
	/**
	Returns the name of the field located at the specified index.
	
	@param fieldIndex index of field to return
	@return name of the field
	*/	
	public String getFieldName(int fieldIndex) {
		return fieldNames.get(fieldIndex);
	}
	
	/**
	Returns the names of all field specified.
	
	@return array containing all the names of the fields
	*/	
	public String[] getFieldNames() {
		return fieldNames.toArray(new String[0]);
	}

	/**
	Returns the properties for the field located at the specified index.
	
	@param fieldIndex index of field to return
	@return properties of the field
	*/	
	public String getFieldProperties(int fieldIndex) {
		return fieldProps.get(fieldIndex);
	}

	/**
	Returns the properties of all field specified.
	
	@return array containing all the properties of the fields
	*/
	public String[] getFieldProperties() {
		return fieldProps.toArray(new String[0]);
	}
	
	/**
	Creates a SqlBuilder object based on the TableDefinition.
	
	@return new SqlBuilder object
	*/	
	public SqlBuilder createSqlBuilder() {
		SqlBuilder sql = new SqlBuilder(tableName, getFieldNames(), getFieldProperties(), 0);
		return sql;
	}

	/**
	Creates a physical database table based on the TableDefinition.
	
	@param generator ConnectionGenerator with database connection information
	@param dropTableFirst set to true to drop existing table before creating table 
	@throws DataJetException		if the connection information provided to 
									the DataJet object is incorrect or
									if the DataJet has not been initialized
	*/
	public void createTable(ConnectionGenerator generator, boolean dropTableFirst) throws DataJetException {
		Connection con = null;
		try {
			con = generator.createConnection();
			createTable(con, dropTableFirst);
		} finally {
			generator.closeConnection(con);
		}
	}

	/**
	Creates a physical database table based on the TableDefinition.
	
	@param driver string name of driver class to load
	@param connectionString database url to create Connection object
	@param dropTableFirst set to true to drop existing table before creating table
	@throws DataJetException		if the connection information provided to 
									the DataJet object is incorrect or
									if the DataJet has not been initialized
	*/
	public void createTable(String driver, String connectionString, boolean dropTableFirst) throws DataJetException {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(connectionString);
			createTable(con, dropTableFirst);
		} catch ( ClassNotFoundException cnfex ) {
			throw new ConnectionException(cnfex.getMessage());
		} catch ( SQLException sqlex ) {
			throw new ConnectionException(sqlex.getMessage());
		} finally {
			try {
				con.close();
			} catch ( Exception ex ) {
			
			}
		}
	}

	/**
	Creates a physical database table based on the TableDefinition.
	
	@param con existing connection to the database server
	@param dropTableFirst set to true to drop existing table before creating table
	@throws DataJetException		if the DataJet has not been initialized
	*/
	public void createTable(Connection con, boolean dropTableFirst) throws DataJetException {
		PreparedStatement stmt = null;
		SqlBuilder sql = createSqlBuilder();
		try {
			if ( dropTableFirst ) {
				stmt = con.prepareStatement("DROP TABLE " + tableName + " ;");
				stmt.executeUpdate();
			}
			stmt = con.prepareStatement(sql.createTableStatement());
			stmt.executeUpdate();
		} catch ( SQLException sqlex ) {
			throw new DataJetException(sqlex.getMessage());
		} finally {
			try {
				stmt.close();
			} catch ( Exception ex ) {
			
			}
		}
	}


}
