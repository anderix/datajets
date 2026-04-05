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
	
	public static void main(String[] args) throws Exception {

		FirstDataJet dj = new FirstDataJet();
		dj.setTextField("my entry");
		dj.store();
		System.out.println("Value stored in database!");
/*
		FirstDataJet dj = new FirstDataJet(2);
		dj.setTextField("my updated entry");
		dj.store();
		System.out.println("Value updated in database!");
*/		
/*
		FirstDataJet dj = new FirstDataJet(2); //pass id value to constructor
		dj.remove();
		System.out.println("Record removed from database!");
*/
	}
	
}
