package edu.arizona.biosemantics.oto.lite.db;
/**
 * @author Partha Pratim Sanyal
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import edu.arizona.biosemantics.oto.lite.Configuration;

/**
 * This class is the base class for database access. All classes for accessing the database should extend this class
 * @author Partha, Fengqiong
 *
 */
public abstract class AbstractDBAccess {


	private static final Logger LOGGER = Logger.getLogger(AbstractDBAccess.class);
	/* These hard coded values should be moved to the ApplicationProperties file later*/
	private static String dbUrl;
	
	/**
	 * This static block registers the sql driver
	 */
	static {
		try {
			Configuration configuration = Configuration.getInstance();
			String databaseName = configuration.getDatabaseName();
			String databaseUser = configuration.getDatabaseUser();
			String databasePassword = configuration.getDatabasePassword();
			dbUrl = "jdbc:mysql://localhost/" + databaseName + "?user=" + databaseUser + "" + "&password=" + databasePassword;
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.error("Couldn't find Class in DatabaseAccess" + e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("Couldn't acceess config.properties " + e);
			e.printStackTrace();
		} 
	}
	
	/**
	 * This method returns a connection
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}
	
	/**
	 * This method needs to be final because the subclasses needn't ever modify it.
	 * It closes the connection gracefully
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected final boolean closeConnection(Connection conn) throws SQLException{
		
		boolean returnValue = false;
		if (conn != null) {
			conn.close();
			returnValue = true;
		}

		return returnValue;
	}
	
	/**
	 * This method will close a Statement and a connection
	 * @param stmt
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected final boolean closeConnection(Statement stmt, 
			Connection conn) throws SQLException {
		
		boolean returnValue = false;
		if (stmt != null) {
			stmt.close();
			returnValue = closeConnection(conn) && true;
		}
		return returnValue;
	}
	
	/**
	 * This method closes a PreparedStatement and a Connection
	 * @param pstmt
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected final boolean closeConnection(PreparedStatement pstmt, 
			Connection conn) throws SQLException {
		boolean returnValue = false;
		if (pstmt != null) {
			pstmt.close();
			returnValue = closeConnection(conn) && true;
		}
		return returnValue;
	}
	
	/**
	 * This method closes a Statement, a ResultSet and a Connection
	 * @param stmt
	 * @param rset
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected final boolean closeConnection(Statement stmt, 
			ResultSet rset, Connection conn) throws SQLException {
		boolean returnValue = false;
		if (rset != null) {
			rset.close();
			returnValue = closeConnection(stmt, conn) && true;
		}
		return returnValue;
	}
	
	/**
	 * This method closes a PreparedStatement, a ResultSet, and a Connection
	 * @param pstmt
	 * @param rset
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected final boolean closeConnection(PreparedStatement pstmt, 
			ResultSet rset, Connection conn) throws SQLException {
		boolean returnValue = false;
		if (rset != null) {
			rset.close();
			returnValue = closeConnection(pstmt, conn) && true;
		}
		return returnValue;
	}

}
