/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2022,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.util.ArrayList;

/**
A concrete entension of DataJet that implements Storable and Removable and
can be used by composition or inheritance. GenericDataJet provides methods 
for all DataJet operations (create, read, update, and delete). GenericDataJet 
can be extended to create other DataJet classes, and combined with SqlBuilder 
to return database records in many different ways.
<p>
GenericDataJet, like all classes that extend DataJet, must be initialized with 
connection, table and field information. The connection information can be 
supplied as connection string, driver and connection string, or as an object that implements 
ConnectionGenerator. Table and fields must both be defined. If any of this 
initialization information is missing or incorrect, GenericDataJet will throw 
exceptions when calling its various methods -- ConnectionException if the 
connection information is incorrect and DataJetException if the table or field
information is incorrect.
<p>
When working with GenericDataJet by composition, initialize the class by 
either calling a static <code>jetSet</code> method, or by instantiating
the object with a constructor that also initializes the DataJet. When extending
GenericDataJet, it is usually a good idea to create a new <code>jetSet</code>
method that encapsulates the initialization information, and explicitly call
that method in the default constructor.
<p>
The examples below show how to work with GenericDataJet by composition.
<p>
To create a new record, follow this example:

<pre>
//Example: Creating a new record
String connectionString = [insert connection url];
String table = "my_table";
String[] fields = new String[]{"id", "field1", "field2"};
JetSet jetSet = new JetSet(connectionString, table, fields);
GenericDataJet dj = new GenericDataJet(jetSet);
dj.setFieldValue(1, "new value 1");
dj.setFieldValue(2, "new value 2");
dj.store();
</pre>

To load an existing record and then delete it, follow this example:

<pre>
//Example: Load an existing record, then delete it
String connectionString = [insert connection url];
String table = "my_table";
String[] fields = new String[]{"id", "field1", "field2"};
JetSet jetSet = new JetSet(connectionString, table, fields);
GenericDataJet dj = GenericDataJet.load(1, jetSet); //load record with id = 1;
System.out.println(dj.getFieldValue(1)); //display a field from the record
dj.remove(); //deletes the record from the database
</pre>

The following listing shows how to create a basic class that extends GenericDataJet:

<pre>
import anderix.datajets.*;

class MyDataJet extends GenericDataJet {

	public static JetSet jetSet() {
		String driver = [insert database driver];
		String connectionString = [insert connection url];
		String table = "my_table";
		String[] fields = new String[]{"id", "field1", "field2"};			
		return new JetSet(driver, connectionString, table, fields);
	}
	
	public MyDataJet() {
		super(jetSet());
	}
	
	public MyDataJet load(int idToLoad) throws DataJetException {
		return (MyDataJet)load(idToLoad, new MyDataJet(), jetSet()); //call load method from DataJet
	}
}
</pre>

The above DataJet will be able to create, read, update and delete records. For example:

<pre>
//Example: create new record
MyDataJet dj = new MyDataJet();
dj.setFieldValue(1, "new value 1");
dj.setFieldValue(2, "new value 2");
dj.store();
</pre>

@author David M. Anderson
@see ConnectionGenerator
@see SqlBuilder
*/ 
public class GenericDataJet extends DataJet implements Storable, Removable {

	/**
	Constructor that sets the id for GenericDataJet to use. Please note that 
	this creation method does not load the record into the DataJet from the 
	database. Use the static <code>loadJet</code> method for this, or override
	<code>DataJet.load</code>.
	
	@param id integer representing the identity field value
	@param jetSet JetSet containing properties for the DataJet
	@see GenericDataJet#loadJet
	@see DataJet#load
	*/
	public GenericDataJet(int id, JetSet jetSet) {
		super(id, jetSet);
	}
	
	/**
	Constructor that accepts a JetSet.
	
	@param jetSet JetSet containing properties for the DataJet
	*/
	public GenericDataJet(JetSet jetSet) {
		super(jetSet);
	}
	
	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue int value to set the field to
	*/
	public void setFieldValue(int fieldIndex, int newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue boolean value to set the field to
	*/
	public void setFieldValue(int fieldIndex, boolean newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue short value to set the field to
	*/
	public void setFieldValue(int fieldIndex, short newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue long value to set the field to
	*/
	public void setFieldValue(int fieldIndex, long newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}
	
	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue float value to set the field to
	*/
	public void setFieldValue(int fieldIndex, float newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue double value to set the field to
	*/
	public void setFieldValue(int fieldIndex, double newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue byte value to set the field to
	*/
	public void setFieldValue(int fieldIndex, byte newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue Object value to set the field to
	*/
	public void setFieldValue(int fieldIndex, Object newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Sets the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@param newvalue Datum value to set the field to
	@see Datum
	*/
	public void setFieldValue(int fieldIndex, Datum newvalue) {
		super.setFieldValue(fieldIndex, newvalue);
	}

	/**
	Returns the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@return Datum representation of the current value.
	@see Datum
	*/
	public Datum getFieldValue(int fieldIndex) {
		return super.getFieldValue(fieldIndex);
	}

	/**
	Returns the value of a field.
	
	@param fieldIndex zero-based ordinal position of field in <code>fieldList</code> 
			(defined when initializing the DataJet)
	@return Object representation of the current value
	*/
	public Object getFieldValueAsObject(int fieldIndex) {
		return super.getFieldValueAsObject(fieldIndex);
	}

	/**
	Loads a GenericDataJet with identity field equal to <code>id</code> from the database.
	
	@param id the unique value of the identity field
	@param jetSet JetSet containing properties for the DataJet
	@return GenericDataJet containing data from the database
	@throws DataJetException if the connection to the database can not be established
							 or if the DataJet has not been initialized
	*/
	public static GenericDataJet loadJet(int id, JetSet jetSet) throws DataJetException {
		GenericDataJet dj = new GenericDataJet(jetSet);
		load(id, dj, jetSet);
		return dj;
	}

	/**
	Loads an array of GenericDataJets based on a SqlBuilder object.
	
	@param sql SqlBuilder defining the records to load.
	@param jetSet JetSet containing properties for the DataJet
	@return array of GenericDataJet objects representing all records in the database
	@throws DataJetException if the connection to the database can not be established
							 or if the DataJet has not been initialized
	*/
	public static GenericDataJet[] loadJetsBySql(SqlBuilder sql, JetSet jetSet) throws DataJetException {
		GenericDataJet dj = new GenericDataJet(jetSet);
		ArrayList<DataJet> list = GenericDataJet.loadMultiple(sql, dj, jetSet);
		return list.toArray(new GenericDataJet[0]);
	}

	/**
	Loads an array of GenericDataJets based on an SQL statement.
	
	@param sql String containing SQL specifying the records to load.
	@param jetSet JetSet containing properties for the DataJet
	@return array of GenericDataJet objects representing all records in the database
	@throws DataJetException if the connection to the database can not be established
							 or if the DataJet has not been initialized
	*/
	public static GenericDataJet[] loadJetsBySql(String sql, JetSet jetSet) throws DataJetException {
		GenericDataJet dj = new GenericDataJet(jetSet);
		ArrayList<DataJet> list = GenericDataJet.loadMultiple(sql, dj, jetSet);
		return list.toArray(new GenericDataJet[0]);
	}

	/**
	Loads an array of all GenericDataJets from the database.
	
	@param jetSet JetSet containing properties for the DataJet
	@return array of GenericDataJet objects representing all records in the database
	@throws DataJetException if the connection to the database can not be established
							 or if the DataJet has not been initialized
	*/
	public static GenericDataJet[] loadAllJets(JetSet jetSet) throws DataJetException {
		GenericDataJet dj = new GenericDataJet(jetSet);
		SqlBuilder sql = new SqlBuilder(jetSet.getTable(), jetSet.getFieldList());
		ArrayList<DataJet> list = GenericDataJet.loadMultiple(sql, dj, jetSet);
		return list.toArray(new GenericDataJet[0]);
	}

	/**
	Stores the current GenericDataJet in the database. This method can be used to
	add a new GenericDataJet or to update an existing one.
	
	@throws DataJetException if the connection to the database can not be established
							 or if the DataJet has not been initialized
	*/
	public void store() throws DataJetException {
		super.store();
	}
	
	/**
	Removes the current GenericDataJet from the database.
	
	@throws DataJetException 	if the connection to the database can not be established
								if the DataJet has not been initialized
	*/
	public void remove() throws DataJetException {
		super.remove();
	}

}