package com.counterparty.automation.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sybase.jdbcx.SybDriver;

public class DBUtility {

	private Connection con = null;
	SybDriver sybDriver = null;
	ResultSet rs = null;
	String tcp, database, username, password;

	public DBUtility(String tcp, String database, String username, String password) {
		this.tcp = tcp;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public DBUtility makeConnection() {
		try {
			this.sybDriver = (SybDriver) Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
			this.con = DriverManager.getConnection("jdbc:sybase:Tds:" + this.tcp + "/" + this.database, this.username, this.password);
		} catch (Exception e) {
			throw new RuntimeException("Could not connect to database.", e);
		}
		return this;
	}

	public Statement createStatement() {
		try {
			return this.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
			throw new RuntimeException("Could not connect to database.", e);
		}
	}

	public ResultSet executeQuery(Statement stmt, String sqlQuery) {
		try {
			return stmt.executeQuery(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException("Could not fetch data.", e);
		}
	}

	public void updateQuery(Statement stmt, String sqlQuery) {
		try {
			stmt.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException("Could not run executeUpdate query.", e);
		}
	}

	public DBUtility close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not Close Statement.", e);
		}
		return this;
	}

	public DBUtility close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not Close ResultSet.", e);
		}
		return this;
	}

	public void closeConnection() {
		try {
			if (con != null && rs != null) {
				con.close();
				rs.close();
			}

		} catch (SQLException e) {
			throw new RuntimeException("Could not Close connection.", e);
		}
	}
}
