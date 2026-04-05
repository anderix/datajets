/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2022,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Used as a parameter for several SqlBuilder methods. 
The methods that use this class are:
<ul>
<li><code>andWhere</code></li>
<li><code>orWhere</code></li>
<li><code>andHaving</code></li>
<li><code>orHaving</code></li>
</ul>
The purpose of these methods is to add conditional statements to the SQL statement
represented by the SqlBuilder.

@author David M. Anderson
@see SqlBuilder#andWhere
@see SqlBuilder#orWhere
@see SqlBuilder#andHaving
@see SqlBuilder#orHaving
*/
public class Comparison {
	private final String name;

	private Comparison(String name) { this.name = name; }

	/**
	Returns a String representation of the comparison operator. This String is
	intended for use in a SQL statement.

	@return		the comparison operator as a String
	*/
	public String toString() { return name; }

	/**
	A comparison representing equality. This is the <code>=&lt;/code>
	operator in SQL.
	*/
	public static final Comparison EQUAL = new Comparison(" = ");

	/**
	A comparison representing inequality. This is the <code>!=</code>
	operator in SQL.
	*/
	public static final Comparison NOT_EQUAL = new Comparison(" != ");

	/**
	A comparison representing greater than. This is the <code>&gt;</code>
	operator in SQL.
	*/
	public static final Comparison GREATER_THAN = new Comparison(" > ");

	/**
	A comparison representing less than. This is the <code>&lt;</code>
	operator in SQL.
	*/
	public static final Comparison LESS_THAN = new Comparison(" < ");

	/**
	A comparison representing greater than or equal. This is the <code>&gt;=</code>
	operator in SQL.
	*/
	public static final Comparison GREATER_THAN_EQUAL = new Comparison(" >= ");

	/**
	A comparison representing less than or equal. This is the <code>&lt;=</code>
	operator in SQL.
	*/
	public static final Comparison LESS_THAN_EQUAL = new Comparison(" <= ");

	/**
	A comparison representing similarity. This is the <code>LIKE</code>
	operator in SQL.
	*/
	public static final Comparison LIKE = new Comparison(" LIKE ");
}
