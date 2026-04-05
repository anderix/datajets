import com.datajets.*;

class HelloDataJet {
	public static void main(String[] args) throws Exception {
		String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
		String url = "jdbc:odbc:hello";
		String table = "hello";
		String[] fields = new String[]{"id", "text_field"};
		JetSet jetSet = new JetSet(driver, url, table, fields);
		GenericDataJet dj = GenericDataJet.loadJet(1, jetSet);
		System.out.println(dj.getFieldValue(1));
	}
}