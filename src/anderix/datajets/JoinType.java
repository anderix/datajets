/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2006,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

/**
Used as a parameter in <code>join</code> methods in SqlBuilder. 
The purpose is to define the type of join for the SQL SELECT statement 
returned by SqlBuilder.

@author David M. Anderson
@see SqlBuilder#join
*/
public class JoinType {
	private final String value;

	private JoinType(String value) { this.value = value; }

	/**
	Returns a String representation of the join. This String is
	intended for use in a SQL statement.

	@return		the join type as a String
	*/
	public String toString() { return value; }

	/**
	A join type to define an inner join. This translates to
	<code>INNER</code> in SQL.
	*/
	public static final JoinType INNER = new JoinType(" INNER ");


	/**
	A join type to define a left outer join. This translates to
	<code>LEFT OUTER</code> in SQL.
	*/
	public static final JoinType LEFT_OUTER = new JoinType(" LEFT OUTER ");


	/**
	A join type to define a left outer join. This translates to
	<code>RIGHT OUTER</code> in SQL.
	*/
	public static final JoinType RIGHT_OUTER = new JoinType(" RIGHT OUTER ");

}