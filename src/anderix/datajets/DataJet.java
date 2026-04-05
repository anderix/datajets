/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2022,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
The base class for all DataJets. DataJet provides protected methods for all 
basic database operations (create, read, update, and delete), but it exposes 
very little of that functionality publicly. To create a useful class, write 
new methods that expose the functionality.
<p>
To use DataJet's methods, the class must first be initialized with connection, 
table and field information. The connection information can be supplied as 
connection string, driver and connection string, or as an object that implements 
ConnectionGenerator. Table and fields must both be defined. If any of this 
initialization information is missing or incorrect, DataJet will throw 
exceptions when calling its various methods -- ConnectionException if the 
connection information is incorrect and DataJetException if the table or field
information is incorrect.

The following listing shows how to create a basic class that extends
DataJet and provides methods for loading all records in the database:

<pre>
import anderix.datajets.*;

class MyDataJet extends DataJet {
	public static JetSet jetSet() {
		return new JetSet( [insert connection url], 
			"my_table",
			new String[]{"id", "field1", "field2"}
		);
	}
	
	public MyDataJet() {
		super(jetSet());
	}

	public String getField1() {
		return getFieldValue(1).toString();
	}

	public int getField2() {
		return getFieldValue(2).toInt();
	}
	
	public static MyDataJet[] loadAll() throws DataJetException {
		MyDataJet[] dj = new MyDataJet[]{new MyDataJet()};
		JetSet jset = jetSet();
		SqlBuilder sql = new SqlBuilder(jset.getTable(), jset.getFieldList());
		return (MyDataJet[])MyDataJet.loadMultiple(sql, dj, jset);
	}
}
</pre>

The above DataJet will be able to read all records from the database. For example:

<pre>
//Example: read all records from the database
MyDataJet[] jets = MyDataJet.loadAll();
for ( int i = 0; i &lt; jets.length; i++ ) {
	System.out.println( jets[i].getField1() + " " + jets[i].getField2() );
}
</pre>

@author David M. Anderson
@see GenericDataJet
@see Storable
@see Removable
*/
public abstract class DataJet implements Cloneable {
	private Object[] fieldValues = null;
	private JetSet jset = null;

	/**
	Constructor that sets the id for the DataJet to use. Please note that this
	creation method does not load the record into the DataJet from the 
	database. Use the static <code>load</code> method for this.
	
	@param id integer representing the identity field value
	@param jset JetSet containing properties for the DataJet
	@see DataJet#load
	*/
	protected DataJet(int id, JetSet jset) {
		this.jset = jset;
		initializeFieldValues();
		fieldValues[0] = (Integer)id;
	}

	/**
	Constructor.
	
	@param jset JetSet containing properties for the DataJet
	*/
	protected DataJet(JetSet jset) {
		this.jset = jset;
	}

	/**
	Returns the value of the identity field for the currently-loaded DataJet. 
	If no record is loaded, <code>getId</code> returns zero (0);
	
	@return int value of identity field
	*/
	public int getId() {
		if ( fieldValues == null ) {
			return 0;
		} else {
			if ( fieldValues[0] == null ) {
				return 0;
			} else {
				//return ((Integer)fieldValues[0]).intValue();
				return (new Datum(fieldValues[0])).toInt();
			}
		}
	}

	/**
	Returns the value of the identity field for the currently-loaded DataJet as a Datum. 
	If no record is loaded, <code>getId</code> returns null;
	
	@return int value of identity field
	*/
	public Datum getIdAsDatum() {
		if ( fieldValues == null ) {
			return null;
		} else {
			if ( fieldValues[0] == null ) {
				return null;
			} else {
				return new Datum(fieldValues[0]);
			}
		}
	}

	/**
	Tests whether the currently-loaded DataJet has a corresponding record in the database.
	
	@return true if there is a corresponding record;
			false otherwise.
	*/
	protected boolean recordExists() {
		if ( getId() == 0 ) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue int value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, int newvalue) {
		setFieldValue(fieldIndex, (Integer)newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue boolean value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, boolean newvalue) {
		setFieldValue(fieldIndex, (Boolean)newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue short value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, short newvalue) {
		setFieldValue(fieldIndex, (Short)newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue long value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, long newvalue) {
		setFieldValue(fieldIndex, (Long)newvalue);
	}
	
	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue float value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, float newvalue) {
		setFieldValue(fieldIndex, (Float)newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue double value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, double newvalue) {
		setFieldValue(fieldIndex, (Double)newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue byte value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, byte newvalue) {
		setFieldValue(fieldIndex, (Byte)newvalue);
	}
	
	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue Datum value to set the field to
	@see Datum
	*/
	protected void setFieldValue(int fieldIndex, Datum newvalue) {
		setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue Datum value to set the field to
	*/
	protected void setFieldValue(int fieldIndex, Object newvalue) {
		if ( fieldValues == null ) {
			initializeFieldValues();
		}
		fieldValues[fieldIndex] = newvalue;
	}
	
	/**
	Returns the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@return Object representation of the current value
	*/
	protected Object getFieldValueAsObject(int fieldIndex) {
		if ( fieldValues == null ) {
			return null;
		} else {
			return fieldValues[fieldIndex];
		}
	}

	/**
	Returns the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@return Datum representation of the current value.
	@see Datum
	*/
	protected Datum getFieldValue(int fieldIndex) {
		return new Datum(getFieldValueAsObject(fieldIndex));
	}

	private void initializeFieldValues() {
		String[] fieldList = jset.getFieldList();
		if ( fieldList == null ) {
			fieldValues = new Object[0];
		} else {
			fieldValues = new Object[fieldList.length];
		}
	}

	/**
	Returns SQL statement the DataJet will use to load a record.
	
	@param idToLoad int value representing the identity of the record to load
	@param jset JetSet containing properties for the DataJet
	@return string SQL statement
	*/
	protected static String loadSql(int idToLoad, JetSet jset) {
		String table = jset.getTable();
		String[] fieldList = jset.getFieldList();
		SqlBuilder sql = new SqlBuilder(table, fieldList, fieldList[0]);
		return sql.selectByIdStatement(idToLoad);
	}
	
	/**
	Sets the field values of the supplied DataJet.
	
	@param dataJetToFill instance of a DataJet to set values of
	@param fieldValues values to set fields to
	*/
	protected static void fillDataJet(DataJet dataJetToFill, Object[] fieldValues) {
		dataJetToFill.fieldValues = fieldValues;		
	}

	
	/**
	Loads a single record from the database to the supplied DataJet.
	
	@param idToLoad value of identity field for record to load
	@param dataJetToFill instance of DataJet to use fill with values from the database
	@param jset JetSet containing properties for the DataJet
	@return DataJet representing the record loaded from the database
	@throws DataJetException 	if the DataJet cannot be loaded
								or if the connection information is incorrect
	*/
	protected static DataJet load(int idToLoad, DataJet dataJetToFill, JetSet jset) throws DataJetException {
		String[] fieldList = jset.getFieldList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = jset.createConnection();
			stmt = con.prepareStatement(loadSql(idToLoad, jset));
			rs = stmt.executeQuery();
			Object[] fieldVals = new Object[fieldList.length]; 
			if ( rs.next() ) {
				for ( int i = 0; i < fieldList.length; i++ ) {
					fieldVals[i] = rs.getObject(i+1);
				}
			} else {
				throw new DataJetException("Record not found.");
			}
			fillDataJet(dataJetToFill, fieldVals);
		} catch ( SQLException sqlex ) {
			throw new DataJetException(sqlex.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				jset.closeConnection(con);
			} catch ( Exception ex ) {

			}
		}
		return dataJetToFill;
	}

	/**
	Loads multiple records from the database to the supplied 
	array of DataJet objects.
	
	@param sql SQL statement to use to return the records from the database
	@param dataJetsToFill array of DataJets to fill with values from the database
	@param jset JetSet containing properties for the DataJet
	@return array of DataJet objects representing the records loaded from the database
	@throws DataJetException 	if the DataJets cannot be loaded
								or if the connection information is incorrect
	*/
	protected static DataJet[] loadMultiple(String sql, DataJet[] dataJetsToFill, JetSet jset) throws DataJetException {
		ArrayList<DataJet> list = loadMultiple(sql, dataJetsToFill[0], jset);
		dataJetsToFill = list.toArray(dataJetsToFill);
		return dataJetsToFill;
	}

	/**
	Loads multiple records from the database to the supplied 
	array of DataJet objects.
	
	@param sql SqulBuilder to use to return the records from the database
	@param dataJetsToFill array of DataJets to fill with values from the database
	@param jset JetSet containing properties for the DataJet
	@return array of DataJet objects representing the records loaded from the database
	@throws DataJetException 	if the DataJet has not been initialized
								or if the connection information is incorrect
	@see SqlBuilder
	*/
	protected static DataJet[] loadMultiple(SqlBuilder sql, DataJet[] dataJetsToFill, JetSet jset) throws DataJetException {
		ArrayList<DataJet> list = loadMultiple(sql.selectStatement(), dataJetsToFill[0], jset);
		dataJetsToFill = list.toArray(dataJetsToFill);
		return dataJetsToFill;
	}

	/**
	Loads multiple records from the database to the supplied 
	array of DataJet objects.
	
	@param sql SqulBuilder to use to return the records from the database
	@param dataJetToFill DataJet to fill with values from the database
	@param jset JetSet containing properties for the DataJet
	@return ArrayList containing DataJet objects representing the records loaded from the database
	@throws DataJetException 	if the DataJet has not been initialized
								or if the connection information is incorrect
	@see SqlBuilder
	*/
	protected static ArrayList<DataJet> loadMultiple(SqlBuilder sql, DataJet dataJetToFill, JetSet jset) throws DataJetException {
		return loadMultiple(sql.selectStatement(), dataJetToFill, jset);
	}
	
	/**
	Loads multiple records from the database to the supplied 
	array of DataJet objects.
	
	@param sql SQL statement to use to return the records from the database
	@param dataJetToFill DataJet to fill with values from the database
	@param jset JetSet containing properties for the DataJet
	@return ArrayList containing DataJet objects representing the records loaded from the database
	@throws DataJetException 	if the DataJet has not been initialized
								or if the connection information is incorrect
	*/
	protected static ArrayList<DataJet> loadMultiple(String sql, DataJet dataJetToFill, JetSet jset) throws DataJetException {
		String[] fieldList = jset.getFieldList();
		ArrayList<DataJet> jetList = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = jset.createConnection();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			jetList = new ArrayList<DataJet>();
			while ( rs.next() ) {
				DataJet currentJet = null;
				currentJet = (DataJet)dataJetToFill.clone();
				Object[] fieldVals = new Object[fieldList.length]; 
				for ( int i = 0; i < fieldList.length; i++ ) {
					fieldVals[i] = rs.getObject(i+1);
				}
				fillDataJet(currentJet, fieldVals);
				jetList.add(currentJet);
			}
		} catch ( SQLException sqlex ) {
			throw new DataJetException(sqlex.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				jset.closeConnection(con);
			} catch ( Exception ex ) {

			}
		}
		return jetList;
	}

	/**
	Stores the current DataJet in the database. This method can be used to
	add a new DataJet or to update an existing one.
	
	@param con java.sql.Connection object to use for the operation
	@throws DataJetException 	if the DataJet has not been initialized
								or if the connection to the database can not be established
	*/
	protected void store(Connection con) throws DataJetException {
	    PreparedStatement stmt = null;
		try {
			if ( recordExists() ) {
				stmt = con.prepareStatement(storeSql());
				stmt.executeUpdate();
			} else {
				ResultSet rs = null;
				try {
					stmt = con.prepareStatement(storeSql(), Statement.RETURN_GENERATED_KEYS);
					stmt.execute();
					rs = stmt.getGeneratedKeys();
					rs.next();
					fieldValues[0] = (Integer)rs.getInt(1);
				} catch ( UnsupportedOperationException uoex ) {
					try { //if I can't get it, then first make sure I can do the insert
						String insert = storeSql();
						stmt = con.prepareStatement(insert);
						stmt.executeUpdate();
					} catch ( SQLException isqlex2 ) {
						throw new DataJetException(isqlex2.getMessage());
					}
					// if that goes through, then try a couple of alternative methods for retriving the value
					if ( getId() == 0 ) { // make sure we don't have a value yet.
						try {
							//TODO: This needs to be tested
							//SQLite: SELECT last_insert_rowid()
							String selectId = "SELECT last_insert_rowid();";
							stmt = con.prepareStatement(selectId);
							rs = stmt.executeQuery();
							rs.next();
							fieldValues[0] = (Integer)rs.getInt(1);
						} catch ( SQLException isqlex3 ) {
							try {
								//SQL Server and Access
								String selectId = " SELECT @@IDENTITY;";
								stmt = con.prepareStatement(selectId);
								rs = stmt.executeQuery();
								rs.next();
								fieldValues[0] = (Integer)rs.getInt(1);
							} catch ( SQLException isqlex4 ) {
								try {
									// MySQL / MariaDB
									stmt = con.prepareStatement("SELECT LAST_INSERT_ID();");
									rs = stmt.executeQuery();
									rs.next();
									fieldValues[0] = (Integer)rs.getInt(1);	
								} catch ( SQLException isqlex5 ) { 
									//TODO: Oracle, Postgres
									//at this point, I give up. The record is in the database, but we don't have an id.
								}
							}
						}
					}
					
					
					
				} catch ( SQLException isqlex ) {
					throw new DataJetException("Operation not supported. " + isqlex.getMessage());
				} finally {
					try {
						rs.close();
					} catch ( Exception ex ) {
		
					}
				}
			}
		} catch ( SQLException sqlex ) {
			throw new DataJetException(sqlex.getMessage());
		} finally {
			try {
				stmt.close();
			} catch ( Exception ex ) {

			}
		}
	}
	
	/**
	Stores the current DataJet in the database. This method can be used to
	add a new DataJet or to update an existing one.
	
	@throws DataJetException if the DataJet has not been initialized
							or if the connection to the database can not be established
	*/
	protected void store() throws DataJetException {
		Connection con = null;
		try {
			con = jset.createConnection();
			store(con);
		} finally {
			jset.closeConnection(con);
		}
	}


	private SqlBuilder createStoreSqlBuilder() {
		String table = jset.getTable();
		String[] fieldList = jset.getFieldList();
		String[] fieldsToStore = new String[fieldList.length-1];
		for ( int i = 1; i < fieldList.length; i++ ) {
			fieldsToStore[i-1] = fieldList[i];
		}
		SqlBuilder sql = new SqlBuilder(table, fieldsToStore, fieldList[0]);
		for ( int i = 0; i < fieldValues.length-1; i++ ) {
			if ( fieldValues[i+1] == null ) {
				sql.setFieldValueToNull(i);
			} else {
				sql.setFieldValue(i, fieldValues[i+1]);
			}
		}
		return sql;
	}

	/**
	Returns SQL statement the DataJet will use to store a record.
	
	@return string SQL statement
	@throws DataJetException if the table or field list have not been specified
	*/	
	protected String storeSql() {
		SqlBuilder sql = createStoreSqlBuilder();
		if ( recordExists() ) {
			return sql.updateByIdStatement(getId()); 
		} else {
			return sql.insertStatement();
		}
	}

	/**
	Removes the current DataJet from the database.
	
	@param con java.sql.Connection object to use for the operation
	@throws DataJetException if the DataJet has not been initialized
						or if the connection to the database can not be established
	*/
	protected void remove(Connection con) throws DataJetException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(removeSql());
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


	/**
	Removes the current DataJet from the database.
	
	@throws DataJetException if the DataJet has not been initialized
					or if the connection to the database can not be established
	*/
	protected void remove() throws DataJetException {
		Connection con = null;
		try {
			con = jset.createConnection();
			remove(con);
		} finally {
			jset.closeConnection(con);
		}
	}

	/**
	Returns SQL statement the DataJet will use to remove a record.
	
	@return string SQL statement
	*/
	protected String removeSql() {
		String table = jset.getTable();
		String[] fieldList = jset.getFieldList();
		SqlBuilder sql = new SqlBuilder(table, fieldList, fieldList[0]);
		return sql.deleteByIdStatement(getId());
	}

	/**
	Returns a clone of the DataJet.
	
	@return Object containing clone of the DataJet
	*/
	public Object clone() {
		try {
			return super.clone();
		} catch ( Exception ex ) {
			throw new RuntimeException(ex.getMessage());
		}
	}

}
