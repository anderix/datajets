/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Used as a parameter in <code>orderBy</code> methods in SqlBuilder. 
The purpose is to define the sort order for the SQL SELECT statement returned by 
SqlBuilder. Alternatively, you may use the <code>orderByAscending</code> or 
<code>orderByDescending</code> methods to specify sort order.

@author David M. Anderson
@see SqlBuilder#orderBy
*/
public class OrderDirection {
	private final String value;

	private OrderDirection(String value) { this.value = value; }

	/**
	Returns a String representation of the sort order direction. This String is
	intended for use in a SQL statement.

	@return		the sort order direction as a String
	*/
	public String toString() { return value; }

	/**
	A sort order to define ascending alphabetical or numerical data. This is the 
	<code>ASC</code> keyword in SQL.
	*/
	public static final OrderDirection ASCENDING = new OrderDirection(" ASC ");

	/**
	A sort order to define descending alphabetical or numerical data. This is the 
	<code>DESC</code> keyword in SQL.
	*/
	public static final OrderDirection DESCENDING = new OrderDirection(" DESC ");
}