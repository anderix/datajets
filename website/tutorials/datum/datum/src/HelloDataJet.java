import com.datajets.*;

class HelloDataJet {
	public static void main(String[] args) throws Exception {
		String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
		String url = "jdbc:odbc:hello";
		String table = "hello";
		String[] fields = new String[]{"id", "text_field"};
		JetSet jetSet = new JetSet(driver, url, table, fields);
		GenericDataJet dj = GenericDataJet.loadJet(1, jetSet);
		Datum d = dj.getFieldValue(1);
		System.out.println(d.isString());
		System.out.println(d.isInt());
		System.out.println(d.isBoolean());
		System.out.println(d.isDate());
		System.out.println(d.toString());
		System.out.println(d.toInt());
		System.out.println(d.toBoolean());
		System.out.println(d.toDate());
	}
}