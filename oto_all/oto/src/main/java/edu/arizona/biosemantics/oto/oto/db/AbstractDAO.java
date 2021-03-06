package edu.arizona.biosemantics.oto.oto.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.arizona.biosemantics.oto.oto.Configuration;

public abstract class AbstractDAO {

	private String databaseName;
	private String databaseUser;
	private String databasePassword;
	protected Connection connection;

	public AbstractDAO() throws IOException, ClassNotFoundException {
		Configuration configuration = Configuration.getInstance();
		this.databaseName = configuration.getDatabaseName();
		this.databaseUser = configuration.getDatabaseUser();
		this.databasePassword = configuration.getDatabasePassword();
		Class.forName("com.mysql.jdbc.Driver");
	}
	
	public PreparedStatement executeSQL(String sql) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.execute();
		return preparedStatement;
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}
	
	public void openConnection() throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUser, databasePassword);
	}
	
	public void closeConnection() throws SQLException {
		this.connection.close();
	}
}
