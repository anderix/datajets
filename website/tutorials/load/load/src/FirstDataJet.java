import com.datajets.*;

class FirstDataJet extends GenericDataJet {
	
	public static JetSet jetSet() {
		String table = "hello";
		String[] fields = new String[]{"id", "text_field"};
		String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
		String connectionUrl = "jdbc:odbc:hello";
		return new JetSet(driver, connectionUrl, table, fields);
	}
	
	public FirstDataJet() {
		super(jetSet());
	}

	public FirstDataJet(int id) {
		super(id, jetSet());
	}

	public void setTextField(String textField) {
		setFieldValue(1, textField);
	}

	public String getTextField() {
		return getFieldValue(1).toString();
	}

	public static FirstDataJet load(int id) throws ConnectionException, DataJetException {
		FirstDataJet dj = new FirstDataJet();
		load(id, dj, jetSet());
		return dj;
	}

	public static FirstDataJet[] loadAll() throws ConnectionException, DataJetException {
		JetSet jset = jetSet();
		SqlBuilder sql = new SqlBuilder(jset.getTable(), jset.getFieldList());
		return (FirstDataJet[])loadMultiple(sql, new FirstDataJet[]{new FirstDataJet()}, jset);
	}

	public static void main(String[] args) throws Exception {
/*
		FirstDataJet dj = FirstDataJet.load(1);
		System.out.println(dj.getTextField());
*/
		FirstDataJet[] jets = FirstDataJet.loadAll();
		for ( int i = 0; i < jets.length; i++ ) {
			System.out.println(jets[i].getTextField());
		}
	}
	
}
