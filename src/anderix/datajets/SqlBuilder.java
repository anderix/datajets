/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2022,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
Facilitates creation of SQL statements in an object-oriented manner. The basic 
use of a SqlBuilder object follows this pattern:

<ol>
<li>Instantiate SqlBuilder object</li>
<li>Define table to connect to</li>
<li>Define fields in that table</li>
<li>Define identity field (if applicable)</li>
<li>Set field values (if applicable)</li>
<li>Return completed SQL statement</li>
</ol>

For example, this code creates a <code>SELECT</code> statement:

<pre>
//Example: creating a SELECT statement
SqlBuilder sql = new SqlBuilder;
String table = "my_table";
sql.setTable(table);
String[] fields = new String[]{"field1", "field2"};
sql.setFieldList(fields);
System.out.println(sql.selectStatement());
</pre>

SqlBuilder contains methods for adding <code>WHERE</code> clauses to 
<code>SELECT</code>, <code>INSERT</code>, <code>UPDATE</code>, and 
<code>DELETE</code> statements. There are also methods for adding
<code>JOIN</code>, <code>GROUP BY</code>, <code>HAVING</code>, 
and <code>ORDER BY</code> clauses to <code>SELECT</code> statements. 
Calling these methods for <code>INSERT</code>, <code>UPDATE</code> or 
<code>DELETE</code> statements will have no effect.
<p>
Some statments require that the identity field be specified. This can be done 
in the <code>setFieldList</code> method or in an appropriate constructor. The 
methods that require an identity field are:
<ul>
<li><code>selectByIdStatement</code></li>
<li><code>updateByIdStatement</code></li>
<li><code>deleteByIdStatement</code></li>
</ul>

When creating an <code>INSERT</code> or <code>UPDATE</code> statment, you may
populate parameter values by calling <code>setFieldValue</code> or 
<code>setFieldValueToNull</code>. If you do not set field values, the statement
will be returned with ? as a placeholder value.
<p>
If SqlBuilder does not have enough information to create the requested SQL
statement, it will return an empty string ("");

@author David M. Anderson
*/
public class SqlBuilder {
	private String[] fields;
	private String[] fieldValues;
	private String[] fieldProps;
	private String idField;
	private String table;
	private String[] joinStatements = new String[0];
	private String[] whereStatements = new String[0];
	private String[] groupByStatements = new String[0];
	private String[] havingStatements = new String[0];
	private String[] orderByStatements = new String[0];

	/**
	Constructor that establishes table, fields, field properties and identity field.
	
	@param table string name of the table in the database
	@param fields array of strings representing each field
	@param fieldProperties array of strings representing fieldProperties
	@param idFieldIndex zero-based ordinal position of field in <code>fields[]</code>
	*/
	public SqlBuilder(String table, String[] fields, String[] fieldProperties, int idFieldIndex) {
		this.fields = fields;
		this.fieldValues = new String[fields.length];
		this.fieldProps = fieldProperties;
		this.table = table;
		this.idField = fields[idFieldIndex];
	}

	/**
	Constructor that establishes table, fields, and identity field.
	
	@param table string name of the table in the database
	@param fields array of strings representing each field
	@param idFieldIndex zero-based ordinal position of field in <code>fields[]</code>
	*/
	public SqlBuilder(String table, String[] fields, int idFieldIndex) {
		this.fields = fields;
		this.fieldValues = new String[fields.length];
		this.fieldProps = new String[fields.length];
		this.table = table;
		this.idField = fields[idFieldIndex];
	}
	
	/**
	Constructor that establishes table, fields, and identity field.
	
	@param table string name of the table in the database
	@param fields array of strings representing each field
	@param idField name of identity field
	*/
	public SqlBuilder(String table, String[] fields, String idField) {
		this.fields = fields;
		this.fieldValues = new String[fields.length];
		this.fieldProps = new String[fields.length];
		this.table = table;
		this.idField = idField;
	}
	
	/**
	Constructor that establishes table and fields.
	
	@param table string name of the table in the database
	@param fields array of strings representing each field
	*/
	public SqlBuilder(String table, String[] fields) {
		this.fields = fields;
		this.fieldValues = new String[fields.length];
		this.fieldProps = new String[fields.length];
		this.table = table;
	}

	/**
	Constructor that establishes table. After instantiation, 
	<code>setFieldList</code> must be called to populate field list and/or 
	identity field.
	
	@param table string name of the table in the database
	*/
	public SqlBuilder(String table) {
		this.table = table;
	}
	
	/**
	Constructor that does not establish table, fields or identity field.
	*/
	public SqlBuilder() {
	
	}

	private String createTemplate() {
		return "CREATE TABLE [table] ( [fieldDefinitions] )";
	}
	
	private String selectTemplate() {
		return "SELECT [fields] FROM [table] ";
	}
	
	private String insertTemplate() {
		return "INSERT INTO [table] ( [fields] ) VALUES ( [parameters] ) ";
	}
	
	private String updateTemplate() {
		return "UPDATE [table] SET [statements] ";
	}
	
	private String deleteTemplate() {
		return "DELETE FROM [table] ";
	}
	
	private String whereTemplate() {
		return "WHERE [statements] ";
	}
	
	private String groupByTemplate() {
		return "GROUP BY [statements] ";
	}
	
	private String havingTemplate() {
		return "HAVING [statements] "; 
	}
	
	private String orderByTemplate() {
		return "ORDER BY [statements] ";
	}

	/**
	Establishes fields and identity field.
	
	@param fields array of strings representing each field
	@param idFieldIndex zero-based ordinal position of field in <code>fields[]</code>
	*/
	public void setFieldList(String[] fields, int idFieldIndex) {
		this.fieldValues = new String[fields.length];
		this.fieldProps = new String[fields.length];
		this.fields = fields;
		this.idField = fields[idFieldIndex];
	}

	/**
	Establishes fields and identity field.
	
	@param fields array of strings representing each field
	@param idField name of identity field
	*/
	public void setFieldList(String[] fields, String idField) {
		this.fieldValues = new String[fields.length];
		this.fieldProps = new String[fields.length];
		this.fields = fields;
		this.idField = idField;
	}

	/**
	Establishes fields.
	
	@param fields array of strings representing each field
	*/
	public void setFieldList(String[] fields) {
		this.fieldValues = new String[fields.length];
		this.fieldProps = new String[fields.length];
		this.fields = fields;
	}
	
	/**
	Returns array of strings representing field names already established.

	@return fields as array of strings
	*/
	public String[] getFieldList() {
		return fields;
	}
	

	/**
	Adds a single field to the field list.
	
	@param fieldName the name of the field to add
	*/
	public void addField(String fieldName) {
		if ( this.fields == null ) {
			this.fields = new String[0];
		}
		String[] newFields = new String[this.fields.length+1];
		for ( int i = 0; i < this.fields.length; i++ ) {
			newFields[i] = this.fields[i];
		}
		newFields[this.fields.length] = fieldName;
		this.fields = newFields;
	}

	/**
	Prepends the field name with a table name. This is used to differentiate fields
	from different tables that have the same name in queries that joins those tables.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param tableName name of the table to which the field belongs
	*/
	public void prependField(int fieldIndex, String tableName) {
		this.fields[fieldIndex] = tableName + "." + this.fields[fieldIndex];
	}


	/**
	Establishes table.
	
	@param table string representing table name
	*/
	public void setTable(String table) {
		this.table = table;
	}
	
	/**
	Returns string representing table name already established.

	@return table name as string
	*/
	public String getTable() {
		return table;
	}

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as byte
	*/
	public void setFieldValue(int fieldIndex, byte fieldValue) {
		fieldValues[fieldIndex] = Integer.toString(fieldValue);
	}

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as double
	*/
	public void setFieldValue(int fieldIndex, double fieldValue) {
		fieldValues[fieldIndex] = Double.toString(fieldValue);
	}	

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as float
	*/
	public void setFieldValue(int fieldIndex, float fieldValue) {
		fieldValues[fieldIndex] = Float.toString(fieldValue);
	}	
	
	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as long 
	*/
	public void setFieldValue(int fieldIndex, long fieldValue) {
		fieldValues[fieldIndex] = Long.toString(fieldValue);
	}	
	
	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as short 
	*/
	public void setFieldValue(int fieldIndex, short fieldValue) {
		fieldValues[fieldIndex] = Short.toString(fieldValue);
	}	
	
	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as boolean 
	*/
	public void setFieldValue(int fieldIndex, boolean fieldValue) {
		String newFieldValue;
		if ( fieldValue ) {
			newFieldValue = "1";
		} else {
			newFieldValue = "0";
		}
		fieldValues[fieldIndex] = "'" + newFieldValue + "'";
	}
	

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as java.util.Date 
	*/
	public void setFieldValue(int fieldIndex, Date fieldValue) {
		String dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fieldValue);
		fieldValues[fieldIndex] = "'" + dateValue + "'";
	}

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as int
	*/
	public void setFieldValue(int fieldIndex, int fieldValue) {
		fieldValues[fieldIndex] = Integer.toString(fieldValue);
	}
	
	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements to NULL.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	*/
	public void setFieldValueToNull(int fieldIndex) {
		fieldValues[fieldIndex] = "NULL";
	}

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as java.lang.String
	*/
	public void setFieldValue(int fieldIndex, String fieldValue) {
		if ( fieldValue == null || fieldValue.toLowerCase() == "null" ) {
			setFieldValueToNull(fieldIndex);
		} else {
			fieldValues[fieldIndex] = "'" + encode(fieldValue) + "'";
		}
	}

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as java.lang.String
	*/
	public void setFieldValue(int fieldIndex, Object fieldValue) {
		if ( fieldValue instanceof String ) {
			setFieldValue(fieldIndex, (String)fieldValue);
		} else if ( fieldValue instanceof Integer ) {
			setFieldValue( fieldIndex, ((Integer)fieldValue).intValue() );
		} else if ( fieldValue instanceof Date ) {
			setFieldValue(fieldIndex, (Date)fieldValue);
		} else if ( fieldValue instanceof Boolean ) {
			setFieldValue( fieldIndex, ((Boolean)fieldValue).booleanValue() );
		} else if ( fieldValue instanceof Short ) {
			setFieldValue( fieldIndex, ((Short)fieldValue).shortValue() );
		} else if ( fieldValue instanceof Long ) {
			setFieldValue( fieldIndex, ((Long)fieldValue).longValue() );
		} else if ( fieldValue instanceof Float ) {
			setFieldValue( fieldIndex, ((Float)fieldValue).floatValue() );
		} else if ( fieldValue instanceof Double ) {
			setFieldValue( fieldIndex, ((Double)fieldValue).doubleValue() );
		} else if ( fieldValue instanceof Byte ) {
			setFieldValue( fieldIndex, ((Byte)fieldValue).byteValue() );
		} else {
		    setFieldValue(fieldIndex, fieldValue.toString());
		}
	}

	/**
	Sets value of field for <code>UPDATE</code> and <code>INSERT</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldValue value of field as java.lang.String
	*/
	public void setFieldValue(int fieldIndex, Datum fieldValue) {
		if ( fieldValue.isString() ) {
			setFieldValue(fieldIndex, fieldValue.toString());
		} else if ( fieldValue.isInt() ) {
			setFieldValue( fieldIndex, fieldValue.toInt() );
		} else if ( fieldValue.isDate() ) {
			setFieldValue(fieldIndex, fieldValue.toDate());
		} else if ( fieldValue.isBoolean() ) {
			setFieldValue( fieldIndex, fieldValue.toBoolean() );
		} else if ( fieldValue.isShort() ) {
			setFieldValue( fieldIndex, fieldValue.toShort() );
		} else if ( fieldValue.isLong() ) {
			setFieldValue( fieldIndex, fieldValue.toLong() );
		} else if ( fieldValue.isFloat() ) {
			setFieldValue( fieldIndex, fieldValue.toFloat() );
		} else if ( fieldValue.isDouble() ) {
			setFieldValue( fieldIndex, fieldValue.toDouble() );
		} else if ( fieldValue.isByte() ) {
			setFieldValue( fieldIndex, fieldValue.toByte() );
		} else {
		    setFieldValue(fieldIndex, fieldValue.toString());
		}
	}
	
	/**
	Sets properties of a field for <code>CREATE TABLE</code> statements.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>)
	@param fieldProperties properties of field as java.lang.String
	*/
	public void setFieldProperties(int fieldIndex, String fieldProperties) {
		fieldProps[fieldIndex] = fieldProperties;
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, String value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(encode(value));
		sb.append("'");
		//andWhere(fieldName + operator.toString() + "'" + encode(value) + "'");
		andWhere(sb.toString());
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, int value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, int value) {
		andWhere(fieldName + operator.toString() + Integer.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, Date value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, Date value) {
		String dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(dateValue);
		sb.append("'");
		andWhere(sb.toString());
		//andWhere(fieldName + operator.toString() + "'" + dateValue + "'");
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, boolean value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, boolean value) {
		String booleanValue;
		if ( value ) {
			booleanValue = "1";
		} else {
			booleanValue = "0";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(booleanValue);
		andWhere(sb.toString());
		//andWhere(fieldName + operator.toString() + "'" + booleanValue + "'");
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, short value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, short value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Short.toString(value));
		andWhere(sb.toString());
		//andWhere(fieldName + operator.toString() + Short.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
		
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, float value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, float value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Float.toString(value));
		andWhere(sb.toString());
		//andWhere(fieldName + operator.toString() + Float.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, double value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/
	public void andWhere(String fieldName, Comparison operator, double value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Double.toString(value));
		andWhere(sb.toString());
		//andWhere(fieldName + operator.toString() + Double.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/	
	public void andWhere(int fieldIndex, Comparison operator, byte value) {
		andWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/	
	public void andWhere(String fieldName, Comparison operator, byte value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Byte.toString(value));
		andWhere(sb.toString());
		//andWhere(fieldName + operator.toString() + Byte.toString(value));
	}
		
	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>AND</code>.
	
	@param whereStatement string containing complete <code>WHERE</code> clause
	*/	
	public void andWhere(String whereStatement) {
		addWhere(whereStatement, " AND ");
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, String value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(encode(value));
		sb.append("'");
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + "'" + encode(value) + "'");
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, int value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, int value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Integer.toString(value));
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + Integer.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, Date value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, Date value) {
		String dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(dateValue);
		sb.append("'");
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + "'" + dateValue + "'");
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, boolean value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, boolean value) {
		String booleanValue;
		if ( value ) {
			booleanValue = "1";
		} else {
			booleanValue = "0";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(booleanValue);
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + "'" + booleanValue + "'");
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, short value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, short value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Short.toString(value));
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + Short.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, float value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, float value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Float.toString(value));
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + Float.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, double value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, double value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Double.toString(value));
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + Double.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/	
	public void orWhere(int fieldIndex, Comparison operator, byte value) {
		orWhere(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/	
	public void orWhere(String fieldName, Comparison operator, byte value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Byte.toString(value));
		orWhere(sb.toString());
		//orWhere(fieldName + operator.toString() + Byte.toString(value));
	}

	/**
	Adds a <code>WHERE</code> clause to an SQL statement with <code>OR</code>.
	
	@param whereStatement string containing complete <code>WHERE</code> clause
	*/	
	public void orWhere(String whereStatement) {
		addWhere(whereStatement, " OR ");
	}

	private void addWhere(String whereStatement, String andOr) {
		int num = whereStatements.length + 1;
		String[] newWhereStatements = new String[num];
		for ( int i = 0; i < whereStatements.length; i++ ) {
			newWhereStatements[i] = whereStatements[i];
		}
		if ( whereStatements.length == 0 ) {
			newWhereStatements[--num] = whereStatement;
		} else {
			newWhereStatements[--num] = andOr + whereStatement;
		}
		whereStatements = newWhereStatements;
	}


	/**
	Adds an <code>INNER JOIN</code> clause to an SQL statement.
	
	@param tableToJoin name of the table to join
	@param fieldInFirstTable name of field in first table
	@param fieldInTableToJoin name of field in table specified in tableToJoin
	*/
	public void join(String tableToJoin, String fieldInFirstTable, String fieldInTableToJoin) {
		join(JoinType.INNER, tableToJoin, fieldInFirstTable, Comparison.EQUAL, fieldInTableToJoin);
	}

	/**
	Adds an <code>INNER JOIN</code> clause to an SQL statement.
	
	@param tableToJoin name of the table to join
	@param fieldInFirstTable name of field in first table
	@param operator comparison operator for join
	@param fieldInTableToJoin name of field in table specified in tableToJoin
	*/
	public void join(String tableToJoin, String fieldInFirstTable, 
		Comparison operator, String fieldInTableToJoin) {
		
		join(JoinType.INNER, tableToJoin, fieldInFirstTable, operator, fieldInTableToJoin);
	}

	/**
	Adds an <code>INNER JOIN</code> clause to an SQL statement.
	
	@param tableToJoin name of the table to join
	@param onStatement conditional statement to define the join
	*/
	public void join(String tableToJoin, String onStatement) {
		join(JoinType.INNER, tableToJoin, onStatement);
	}

	/**
	Adds a <code>JOIN</code> clause to an SQL statement.
	
	@param joinType the type of join to add
	@param tableToJoin name of the table to join
	@param fieldInFirstTable name of field in first table
	@param operator comparison operator for join
	@param fieldInTableToJoin name of field in table specified in tableToJoin
	*/
	public void join(JoinType joinType, String tableToJoin, 
		String fieldInFirstTable, Comparison operator, String fieldInTableToJoin) {
		
		join(joinType, tableToJoin, fieldInFirstTable + operator.toString() + fieldInTableToJoin);
	}


	/**
	Adds a <code>JOIN</code> clause to an SQL statement.
	
	@param joinType the type of join to add
	@param tableToJoin name of the table to join
	@param onStatement conditional statement to define the join
	*/
	public void join(JoinType joinType, String tableToJoin, String onStatement) {
		
		int num = joinStatements.length + 1;
		String[] newJoinStatements = new String[num];
		for ( int i = 0; i < joinStatements.length; i++ ) {
			newJoinStatements[i] = joinStatements[i];
		}
		StringBuffer sb = new StringBuffer();
		sb.append(joinType.toString());
		sb.append(" JOIN ");
		sb.append(tableToJoin);
		sb.append(" ON ");
		sb.append(onStatement);
		sb.append(" ");

		newJoinStatements[--num] = sb.toString();

//		newJoinStatements[--num] = joinType.toString() + 
//			" JOIN " + tableToJoin + " ON " + onStatement + " ";
		joinStatements = newJoinStatements;
	}
			
	/**
	Adds a <code>GROUP BY</code> clause to a <code>SELECT</code> statement.
	
	@param groupByFieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to group by
	*/		
	public void groupBy(int groupByFieldIndex) {
		groupBy(fields[groupByFieldIndex]);
	}

	/**
	Adds a <code>GROUP BY</code> clause to a <code>SELECT</code> statement.
	
	@param groupByField string name of field to group by
	*/	
	public void groupBy(String groupByField) {
		int num = groupByStatements.length + 1;
		String[] newGroupByStatements = new String[num];
		for ( int i = 0; i < groupByStatements.length; i++ ) {
			newGroupByStatements[i] = groupByStatements[i];
		}
		newGroupByStatements[--num] = groupByField;
		groupByStatements = newGroupByStatements;
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, String value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(encode(value));
		sb.append("'");
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + "'" + encode(value) + "'");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, int value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, int value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Integer.toString(value));
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + Integer.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, Date value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, Date value) {
		String dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(dateValue);
		sb.append("'");
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + "'" + dateValue + "'");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, boolean value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, boolean value) {
		String booleanValue;
		if ( value ) {
			booleanValue = "1";
		} else {
			booleanValue = "0";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(booleanValue);
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + "'" + booleanValue + "'");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, short value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, short value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Short.toString(value));
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + Short.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, float value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, float value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Float.toString(value));
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + Float.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, double value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, double value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Double.toString(value));
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + Double.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/
	public void andHaving(int fieldIndex, Comparison operator, byte value) {
		andHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/
	public void andHaving(String fieldName, Comparison operator, byte value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Byte.toString(value));
		andHaving(sb.toString());
		//andHaving(fieldName + operator.toString() + Byte.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>AND</code>.
	
	@param havingStatement string containing complete <code>HAVING</code> clause
	*/
	public void andHaving(String havingStatement) {
		addHaving(havingStatement, " AND ");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, String value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value string representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(encode(value));
		sb.append("'");
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + "'" + encode(value) + "'");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, int value) {
		orHaving(fields[fieldIndex], operator, value);
	}
	
	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value int representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, int value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Integer.toString(value));
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + Integer.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, Date value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value java.util.Date representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, Date value) {
		String dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append("'");
		sb.append(dateValue);
		sb.append("'");
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + "'" + dateValue + "'");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, boolean value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value boolean representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, boolean value) {
		String booleanValue;
		if ( value ) {
			booleanValue = "1";
		} else {
			booleanValue = "0";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(booleanValue);
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + "'" + booleanValue + "'");
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, short value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value short representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, short value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Short.toString(value));
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + Short.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, float value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value float representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, float value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Float.toString(value));
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + Float.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, double value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value double representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, double value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Double.toString(value));
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + Double.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/
	public void orHaving(int fieldIndex, Comparison operator, byte value) {
		orHaving(fields[fieldIndex], operator, value);
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param fieldName string name of field to test versus supplied value.
	@param operator Comparison representing the type of comparison operation
	@param value byte representing value to compare to field
	*/
	public void orHaving(String fieldName, Comparison operator, byte value) {
		StringBuffer sb = new StringBuffer();
		sb.append(fieldName);
		sb.append(operator.toString());
		sb.append(Byte.toString(value));
		orHaving(sb.toString());
		//orHaving(fieldName + operator.toString() + Byte.toString(value));
	}

	/**
	Adds a <code>HAVING</code> clause to a <code>SELECT</code> statement with <code>OR</code>.
	
	@param havingStatement string containing complete <code>HAVING</code> clause
	*/
	public void orHaving(String havingStatement) {
		addHaving(havingStatement, " OR ");
	}

	private void addHaving(String havingStatement, String andOr) {
		int num = havingStatements.length + 1;
		String[] newHavingStatements = new String[num];
		for ( int i = 0; i < havingStatements.length; i++ ) {
			newHavingStatements[i] = havingStatements[i];
		}
		if ( havingStatements.length == 0 ) {
			newHavingStatements[--num] = havingStatement;
		} else {
			newHavingStatements[--num] = andOr + havingStatement;
		}
		havingStatements = newHavingStatements;
	}
	
	/**
	Adds an <code>ORDER BY</code> clause to a <code>SELECT</code> statement  with 
	<code>ASC</code> as the direction.
	
	@param orderByFieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to sort by
	*/
	public void orderByAscending(int orderByFieldIndex) {
		orderBy(fields[orderByFieldIndex], OrderDirection.ASCENDING);
	}
	
	/**
	Adds a <code>ORDER BY</code> clause to a <code>SELECT</code> statement with 
	<code>ASC</code> as the direction.
	
	@param orderByField string name of field to sort by
	*/	
	public void orderByAscending(String orderByField) {
		orderBy(orderByField, OrderDirection.ASCENDING);
	}
	
	/**
	Adds a <code>ORDER BY</code> clause to a <code>SELECT</code> statement with 
	<code>DESC</code> as the direction.
	
	@param orderByField string name of field to sort by
	*/
	public void orderByDescending(String orderByField) {
		orderBy(orderByField, OrderDirection.DESCENDING);
	}
	
	/**
	Adds an <code>ORDER BY</code> clause to a <code>SELECT</code> statement  with 
	<code>DESC</code> as the direction.
	
	@param orderByFieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to sort by
	*/
	public void orderByDescending(int orderByFieldIndex) {
		orderBy(fields[orderByFieldIndex], OrderDirection.DESCENDING);
	}	

	/**
	Adds an <code>ORDER BY</code> clause to a <code>SELECT</code> statement  with 
	<code>direction</code> as the direction.
	
	@param orderByFieldIndex ordinal position of field in <code>fields[]</code> (as defined in
					constructor or <code>setFieldList</code>) to sort by
	*/
	public void orderBy(int orderByFieldIndex, OrderDirection direction) {
		orderBy(fields[orderByFieldIndex], direction);
	}

	/**
	Adds a <code>ORDER BY</code> clause to a <code>SELECT</code> statement with 
	<code>direction</code> as the direction.
	
	@param orderByField string name of field to sort by
	*/
	public void orderBy(String orderByField, OrderDirection direction) {
		int num = orderByStatements.length + 1;
		String[] newOrderByStatements = new String[num];
		for ( int i = 0; i < orderByStatements.length; i++ ) {
			newOrderByStatements[i] = orderByStatements[i];
		}
		newOrderByStatements[--num] = orderByField + direction.toString();
		orderByStatements = newOrderByStatements;
	}

	/**
	Creates a <code>SELECT</code> statement based on supplied values.
	
	@param idToSelect int representing identity value of specific record to return
	@return SQL <code>SELECT</code> statement
	*/
	public String selectByIdStatement(int idToSelect) {
		if ( table == "" || table == null || fields == null || idField == null || idField == "" ) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(this.idField);
			sb.append(Comparison.EQUAL.toString());
			sb.append(idToSelect);
			andWhere(sb.toString());
			//andWhere(this.idField + " = " + idToSelect);
			return selectStatement();
		}
	}

	/**
	Creates a <code>SELECT</code> statement based on supplied values.
	
	@return SQL <code>SELECT</code> statement
	*/
	public String selectStatement() {
		if ( table == "" || table == null || fields == null ) {
			return "";
		} else {
			String sql = selectTemplate().replaceAll("\\[fields\\]", fieldList());
			StringBuffer sb = new StringBuffer();
			sb.append( sql.replaceAll("\\[table\\]", table) );
			sb.append(joinClause());
			sb.append(whereClause());
			sb.append(groupByClause());
			sb.append(havingClause());
			sb.append(orderByClause());
			sb.append(";");
			return sb.toString();
		}
	}
	
	/**
	Creates a <code>INSERT</code> statement based on supplied values.
	
	@return SQL <code>INSERT</code> statement
	*/
	public String insertStatement() {
		if ( table == "" || table == null || fields == null ) {
			return "";
		} else {
			String sql = insertTemplate().replaceAll("\\[fields\\]", fieldList());
			sql = sql.replaceAll("\\[table\\]", table);
			StringBuffer sb = new StringBuffer();
			sb.append( sql.replaceAll("\\[parameters\\]", insertParameters()) );
			sb.append(whereClause());
			sb.append(";");
			sql = insertParameterValues(sb.toString());
			return sql;
		}
	}

	/**
	Creates an <code>UPDATE</code> statement based on supplied values.
	
	@param idToUpdate int representing identity value of specific record to update
	@return SQL <code>UPDATE</code> statement
	*/
	public String updateByIdStatement(int idToUpdate) {
		if ( table == "" || table == null || fields == null || idField == null || idField == "" ) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(this.idField);
			sb.append(Comparison.EQUAL.toString());
			sb.append(idToUpdate);
			andWhere(sb.toString());
			return updateStatement();
		}
	}
		
	/**
	Creates an <code>UPDATE</code> statement based on supplied values.
	
	@return SQL <code>UPDATE</code> statement
	*/
	public String updateStatement() {
		if ( table == "" || table == null || fields == null ) {
			return "";
		} else {
			String sql = updateTemplate().replaceAll("\\[table\\]", table);
			StringBuffer sb = new StringBuffer();
			sb.append( sql.replaceAll("\\[statements\\]", updateStatements()) );
			sb.append(whereClause());
			sb.append(";");
			sql = insertParameterValues(sb.toString());
			return sql;
		}
	}

	/**
	Creates a <code>CREATE TABLE</code> statement based on supplied values.
	
	@return SQL <code>CREATE TABLE</code> statement
	*/
	public String createTableStatement() {
		if ( table == "" || table == null || fields == null ) {
			return "";
		} else {
			String sql = createTemplate().replaceAll("\\[table\\]", table);
			StringBuffer sb = new StringBuffer();
			sb.append( sql.replaceAll("\\[fieldDefinitions\\]", createFieldDefinitions()) );
			sb.append(whereClause());
			sb.append(";");
			sql = insertFieldProps(sb.toString());
			return sql;
		}
	}

	/**
	Creates an <code>DELETE</code> statement based on supplied values.
	
	@param idToDelete int representing identity value of specific record to delete
	@return SQL <code>DELETE</code> statement
	*/
	public String deleteByIdStatement(int idToDelete) {
		if ( table == "" || table == null || idField == null || idField == "" ) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(this.idField);
			sb.append(Comparison.EQUAL.toString());
			sb.append(idToDelete);
			andWhere(sb.toString());
			return deleteStatement();
		}
	}
		
	/**
	Creates an <code>DELETE</code> statement based on supplied values.
	
	@return SQL <code>DELETE</code> statement
	*/
	public String deleteStatement() {
		if ( table == "" || table == null ) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( deleteTemplate().replaceAll("\\[table\\]", table) );
			sb.append( whereClause() );
			sb.append( ";" );
			return sb.toString();
		}
	}

	private String fieldList() {
		StringBuffer fieldList = new StringBuffer();
		for ( int i = 0; i < fields.length; i++ ) {
			if ( fieldList.length() != 0 ) fieldList.append(", ");
			fieldList.append(fields[i]);
		}			
		return fieldList.toString();
	}
	
	private String insertParameters() {
		StringBuffer parameterList = new StringBuffer();
		for ( int i = 0; i < fields.length; i++ ) {
			if ( parameterList.length() != 0 ) parameterList.append(", ");
			parameterList.append("?");
		}			
		return parameterList.toString();	
	}
	
	private String insertParameterValues(String sql) {
		String[] parameterizedSql = sql.split("\\?");
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < parameterizedSql.length; i++ ) {
			if ( i > 0 ) {
				sb.append( ((fieldValues[i-1] == null)?"?":fieldValues[i-1]) );
			}
			sb.append(parameterizedSql[i]);
		}
		return sb.toString();
	}

	private String insertFieldProps(String sql) {
		String[] parameterizedSql = sql.split("\\?");
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < parameterizedSql.length; i++ ) {
			if ( i > 0 ) {
				sb.append( ((fieldProps[i-1] == null)?"?":fieldProps[i-1]) );
			}
			sb.append(parameterizedSql[i]);
		}
		return sb.toString();
	}
	
	private String updateStatements() {
		if ( fields == null || fields.length == 0 ) {
			return "";
		} else {
			StringBuffer fieldList = new StringBuffer();
			for ( int i = 0; i < fields.length; i++ ) {
				if ( fieldList.length() != 0 ) fieldList.append(", ");
				fieldList.append(fields[i]);
				fieldList.append(" = ?");
			}			
			return fieldList.toString();
		}
	}

	private String createFieldDefinitions() {
		if ( fields == null || fields.length == 0 ) {
			return "";
		} else {
			StringBuffer fieldList = new StringBuffer();
			for ( int i = 0; i < fields.length; i++ ) {
				if ( fieldList.length() != 0 ) fieldList.append(", ");
				fieldList.append(fields[i]);
				fieldList.append(" ?");
			}			
			return fieldList.toString();
		}
	}


	private String joinClause() {
		if ( joinStatements == null || joinStatements.length == 0 ) {
			return "";
		} else {
			StringBuffer join = new StringBuffer();
			for ( int i = 0; i < joinStatements.length; i++ ) {
				join.append(joinStatements[i]);
			}
			return join.toString();
		}
	}
		
	private String whereClause() {
		if ( whereStatements == null || whereStatements.length == 0 ) {
			return "";
		} else {
			StringBuffer where = new StringBuffer();
			for ( int i = 0; i < whereStatements.length; i++ ) {
				where.append(whereStatements[i]);
			}
			return whereTemplate().replaceAll("\\[statements\\]", where.toString());
		}
	}
	
	private String groupByClause() {
		if ( groupByStatements == null || groupByStatements.length == 0 ) {
			return "";
		} else {
			StringBuffer groupBy = new StringBuffer();
			for ( int i = 0; i < groupByStatements.length; i++ ) {
				if ( groupBy.length() != 0 ) groupBy.append(", ");
				groupBy.append(groupByStatements[i]);
			}
			return groupByTemplate().replaceAll("\\[statements\\]", groupBy.toString());
		}
	}

	private String havingClause() {
		if ( havingStatements == null || havingStatements.length == 0 ) {
			return "";
		} else {
			StringBuffer having = new StringBuffer();
			for ( int i = 0; i < havingStatements.length; i++ ) {
				having.append(havingStatements[i]);
			}
			return havingTemplate().replaceAll("\\[statements\\]", having.toString());
		}
	}

	private String orderByClause() {
		if ( orderByStatements == null || orderByStatements.length == 0 ) {
			return "";
		} else {
			StringBuffer orderBy = new StringBuffer();
			for ( int i = 0; i < orderByStatements.length; i++ ) {
				if ( orderBy.length() != 0 ) orderBy.append(", ");
				orderBy.append(orderByStatements[i]);
			}
			return orderByTemplate().replaceAll("\\[statements\\]", orderBy.toString());
		}
	}
	
	/**
	Modifies a string so it will be processed as a complete string within SQL.
	
	@param toEncode the string to encode
	@return the encoded string
	*/
	public static String encode(String toEncode) {
		toEncode = toEncode.replaceAll("'", "''");
		return toEncode;	
	}

}