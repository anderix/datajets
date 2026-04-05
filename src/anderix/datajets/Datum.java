/*
DataJets, an object-relational framework.<br>
Copyright (c) 2003-2021,  Anderix

Contact Anderix on http://www.anderix.com or david@anderix.com.
*/
package anderix.datajets;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
A single piece of data retrieved from or to be stored in
a database table. It contains methods to facilitate type converstions and testing
object types. Every Datum contains an internal object of type 
Object and provides methods for testing the type and for casting 
to several other types. Use the following pattern to convert objects from one 
type to another:

<pre>
//Example: convert an int to several types
int i = 1;
Datum d = new Datum(i);
String s = d.toString();
long l = d.toLong();
short sh = d.toShort();
</pre>

The Datum class can convert to and from the following types:

<ul>
<li>boolean</li>
<li>byte</li>
<li>double</li>
<li>float</li>
<li>int</li>
<li>long</li>
<li>short</li>
<li>java.lang.String</li>
<li>java.util.Date</li>
</ul>

It also accepts and returns any Object, and its conversion methods 
may be useful for other types as well. However, this should be thoroughly 
tested for each type.
<p>
A Datum can also provide information about the type of internal object.
To test for a specific type, use the following pattern:

<pre>
//Example: test to determine type
int i = 1;
Datum d = new Datum(i);
boolean b = d.isInt(); //returns true
b = d.isString(); //returns false
</pre>

@author David M. Anderson
*/
public class Datum {
	private Object obj = null;

	/**
	Creates a Datum object that contains any Object.

	@param	theObject	the Object to convert
	*/
	public Datum(Object theObject) {
		obj = theObject;
	}

	/**
	Creates a Datum object that contains an int.

	@param	theObject	the int to convert
	*/
	public Datum(int theObject) {
		obj = (Integer)theObject;
	}

	/**
	Creates a Datum object that contains a boolean.

	@param	theObject	the boolean to convert
	*/
	public Datum(boolean theObject) {
		obj = (Boolean)theObject;
	}

	/**
	Creates a Datum object that contains a boolean.

	@param	theObject	the short to convert
	*/	
	public Datum(short theObject) {
		obj = (Short)theObject;
	}

	/**
	Creates a Datum object that contains a long.

	@param	theObject	the long to convert
	*/
	public Datum(long theObject) {
		obj = (Long)theObject;
	}

	/**
	Creates a Datum object that contains a float.

	@param	theObject	the float to convert
	*/
	public Datum(float theObject) {
		obj = (Float)theObject;
	}

	/**
	Creates a Datum object that contains a double.

	@param	theObject	the double to convert
	*/
	public Datum(double theObject) {
		obj = (Double)theObject;
	}

	/**
	Creates a Datum object that contains a byte.

	@param	theObject	the byte to convert
	*/
	public Datum(byte theObject) {
		obj = (Byte)theObject;
	}
			
	/**
	Returns the internal object as an Object.

	@return	the internal object as an Object
	*/
	public Object toObject() {
		return obj;
	}

	/**
	Tests whether the internal object is null or not.

	@return	true if the object is null;
			false otherwise
	*/
	public boolean isNull() {
		if ( obj == null ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	Tests whether the internal object is a String or not.

	@return	true if the object is of type String;
			false otherwise
	*/
	public boolean isString() {
		if ( obj instanceof String ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is an Integer or not.

	@return	true if the object is of type Integer;
			false otherwise
	*/
	public boolean isInt() {
		if ( obj instanceof Integer ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is a Date or not.

	@return	true if the object is of type Date;
			false otherwise
	*/
	public boolean isDate() {
		if ( obj instanceof java.util.Date ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	Tests whether the internal object is a Boolean or not.

	@return	true if the object is of type Boolean;
			false otherwise
	*/
	public boolean isBoolean() {
		if ( obj instanceof Boolean ) {
				return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is a Short or not.

	@return	true if the object is of type Short;
			false otherwise
	*/
	public boolean isShort() {
		if ( obj instanceof Short ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is a Long or not.

	@return	true if the object is of type Long;
			false otherwise
	*/
	public boolean isLong() {
		if ( obj instanceof Long ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is a Float or not.

	@return	true if the object is of type Float;
			false otherwise
	*/
	public boolean isFloat() {
		if ( obj instanceof Float ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is a Double or not.

	@return	true if the object is of type Double;
			false otherwise
	*/
	public boolean isDouble() {
		if ( obj instanceof Double ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	Tests whether the internal object is a Byte or not.

	@return	true if the object is of type Byte;
			false otherwise
	*/
	public boolean isByte() {
		if ( obj instanceof Byte ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	Converts the internal object to a String object. This
	conversion relies on the .toString() method of the 
	object. If the object cannot be converted,
	an empty string is returned.

	@return	a String representation of the internal object
	*/
	public String toString() {
		String rval = "";
		if ( !isNull() ) {
			if ( isString() ) {
				rval = (String)obj;
			} else {
				rval = obj.toString();
			}
		}
		return rval;
	}

	/**
	Converts the internal object to an int. If the object cannot be converted,
	a value of 0 is returned.

	@return	the internal object as an int
	*/
	public int toInt() {
		int rval = 0;
		if ( !isNull() ) {
			if ( isString() ) {
				try {
					rval = Integer.parseInt(((String)obj).trim());
				} catch ( Exception ex ) { }
			} else if ( isInt() ) {
				rval = ((Integer)obj).intValue();
			} else if ( isDate() ){
				rval = (int)((Date)obj).getTime();
			} else if ( isBoolean() ) {
				rval = ((Boolean)obj).booleanValue()?1:0;
			} else if ( isShort() ) {
				rval = ((Short)obj).intValue();
			} else if ( isLong() ) {
				rval = ((Long)obj).intValue();
			} else if ( isFloat() ){
				rval = ((Float)obj).intValue();
			} else if ( isDouble() ) {
				rval = ((Double)obj).intValue();
			} else if ( isByte() ) {
				rval = ((Byte)obj).intValue();
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a java.util.Date. If the object cannot be converted,
	a value representing "the epoch", namely January 1, 1970, 00:00:00 GMT, is returned.

	@return	the internal object as a Date
	*/
	public Date toDate() {
		Date rval = null;
		if ( !isNull() ) {
			if ( isDate() ) {
			 	rval = (Date)obj;
			} else if ( isString() ) {
				try {
					rval = new SimpleDateFormat("E MMM d HH:mm:ss z yyyy").parse((String)obj); //JavaScript date format
				} catch ( ParseException pex ) {
					rval = new Date( toLong() );
				}
			} else {
				rval = new Date( toLong() );
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a boolean.

	@return	the internal object as a boolean If the object cannot be converted,
	a value of false is returned.
	*/
	public boolean toBoolean() {
		boolean rval = false;
		if ( !isNull() ) {
			if ( isBoolean() ) {
		 		rval = ((Boolean)obj).booleanValue();
			} else if ( isString() ) {
				if ( ((String)obj).equalsIgnoreCase("true") ) rval = true;
			} else {
		 		rval = toInt()!=0;
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a short. If the object cannot be converted,
	a value of 0 is returned.

	@return	the internal object as a short
	*/
	public short toShort() {
		short rval = 0;
		if ( !isNull() ) {
			if ( isShort() ) {
				rval = ((Short)obj).shortValue();
			} else if ( isDate() ){
				rval = (short)toLong();
			} else if ( isBoolean() ) {
				rval = (short)(toBoolean()?1:0);
			} else if ( isString() ) {
				rval = Short.parseShort((String)obj);
			} else{
				rval = (short)toInt();
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a long. If the object cannot be converted,
	a value of 0 is returned.

	@return	the internal object as a long
	*/
	public long toLong() {
		long rval = 0;
		if ( !isNull() ) {
			if ( isLong() ) {
				rval = ((Long)obj).longValue();
			} else if ( isString() ) {
				rval = Long.parseLong((String)obj);
			} else if ( isInt() ) {
				rval = (long)toInt();
			} else if ( isDate() ){
				rval = ((Date)obj).getTime();
			} else if ( isShort() ) {
				rval = (long)toShort();
			} else if ( isBoolean() ) {
				rval = (toBoolean()?1:0);
			} else if ( isFloat() ){
				rval = ((Float)obj).longValue();
			} else if ( isDouble() ) {
				rval = ((Double)obj).longValue();
			} else if ( isByte() ) {
				rval = ((Byte)obj).longValue();
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a float. If the object cannot be converted,
	a value of 0 is returned.

	@return	the internal object as a float
	*/
	public float toFloat() {
		float rval = 0;
		if ( !isNull() ) {
			if ( isFloat() ) {
			 	rval = ((Float)obj).floatValue();
			} else if ( isInt() ) {
				rval = ((Integer)obj).floatValue();
			} else if ( isLong() ){
				rval = ((Long)obj).floatValue();
			} else if ( isDate() ) {
				rval = ((Long)toLong()).floatValue();
			} else if ( isString() ) {
				rval = Float.parseFloat((String)obj);
			} else if ( isShort() ) {
				rval = ((Short)obj).floatValue();
			} else if ( isBoolean() ) {
				rval = (toBoolean()?1:0);
			} else if ( isDouble() ) {
				rval = ((Double)obj).floatValue();
			} else if ( isByte() ) {
				rval = ((Byte)obj).floatValue();
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a double. If the object cannot be converted,
	a value of 0 is returned.

	@return	the internal object as a double
	*/
	public double toDouble() {
		double rval = 0;
		if ( !isNull() ) {
			if ( isDouble() ) {
			 	rval = ((Double)obj).doubleValue();
			} else if ( isFloat() ) {
			 	rval = ((Float)obj).doubleValue();
			} else if ( isInt() ) {
				rval = ((Integer)obj).doubleValue();
			} else if ( isLong() ){
				rval = ((Long)obj).doubleValue();
			} else if ( isDate() ){
				rval = ((Long)toLong()).doubleValue();
			} else if ( isString() ) {
				rval = Double.parseDouble((String)obj);
			} else if ( isShort() ) {
				rval = ((Short)obj).doubleValue();
			} else if ( isBoolean() ) {
				rval = (toBoolean()?1:0);
			} else if ( isByte() ) {
				rval = ((Byte)obj).doubleValue();
			}
		}
		return rval;
	}

	/**
	Converts the internal object to a byte. If the object cannot be converted,
	a value of 0 is returned.

	@return	the internal object as a byte
	*/
	public byte toByte() {
		 byte rval = 0;
		if ( !isNull() ) {
			 if ( isByte() ) {
			 	rval = ((Byte)obj).byteValue();
			 } else {
			 	rval = (byte)toInt();
			 }
		}
		 return rval;
	}

}