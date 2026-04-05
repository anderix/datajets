
import java.sql.*;

import java.net.InetAddress;

/**
 * This is a sample program for RmiJdbc client/server jdbc Driver
 * RmiJdbc relies on Java RMI for jdbc objects distribution
 */
public class rjdemo {

  public static void main(String[] args) {
    try {

      // Register RmiJdbc Driver in jdbc DriverManager
      // On some platforms with some java VMs, newInstance() is necessary...
      Class.forName("org.objectweb.rmijdbc.Driver");//.newInstance();

      // Test with MS Access database (rjdemo ODBC data source)
      String url = "jdbc:odbc:hello";

      // RMI host will point to local host
      String rmiHost = new String(
       "//" + InetAddress.getLocalHost().getHostName());

      // RmiJdbc URL is of the form:
      // jdbc:rmi://<rmiHostName[:port]>/<jdbc-url>

      java.sql.Connection c = DriverManager.getConnection("jdbc:rmi:"
       + rmiHost + "/" + url);

      java.sql.Statement st = c.createStatement();
      java.sql.ResultSet rs = st.executeQuery("select * from hello");
		rs.next();
		System.out.println(rs.getString(2));
      rs.close();

    } catch(Exception e) {
      e.printStackTrace();
    }
  }
};

