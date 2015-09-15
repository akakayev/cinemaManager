package com.cinemaManager.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cinemaManager.config.SessionDTO;

public enum DBConnector {

	CONNECTOR;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String SESSION = "time";
	private static final String FILM = "film_id";
	private static final String ATTENDANCE = "attendance";
	private static final String INCOME = "income";
	private static final String CINEMA = "cinema_id";
	private static final String NAME = "name";

	private static final String CINEMAS_TABLE = "Cinemas";
	private static final String FILMS_TABLE = "films";
	private static final String SQL_PATTERN = "SELECT * FROM `%s` WHERE %s like '%s'";

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private static final Logger LOG = Logger.getLogger(DBConnector.class);

	public DBConnector connect(String dbURL, String user, String password) {
		connection = null;
		try {
			LOG.info("trying to connect to database");
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(dbURL, user, password);
		} catch (Exception e) {
			LOG.error("connection to database is failed", e);
		}
		return this;
	}

	public void close() {
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
	}

	public List<SessionDTO> getSessionList(String sql) {
		List<SessionDTO> sessions = new ArrayList<SessionDTO>();
		try {
			LOG.info("sending sql query to database");
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				SessionDTO session = new SessionDTO();
				session.setId(resultSet.getInt(ID));
				int cinemaId = resultSet.getInt(CINEMA);
				int filmId = resultSet.getInt(FILM);
				session.setCinema(getNameById(CINEMAS_TABLE, NAME, cinemaId));
				session.setFilm(getNameById(FILMS_TABLE, NAME, filmId));
				session.setDate(resultSet.getDate(DATE));
				session.setTime(resultSet.getTime(SESSION));
				session.setAttendance(resultSet.getInt(ATTENDANCE));
				session.setIncome(resultSet.getDouble(INCOME));
				sessions.add(session);
			}
		} catch (SQLException e) {
			LOG.error(e);
		}
		LOG.info(sessions.toString());
		return sessions;
	}

	public String getIdByName(String tableName, String name) {
		return getField(tableName, ID, NAME, name);
	}

	private String getNameById(String tableName, String field, int id) {
		return getField(tableName, NAME, ID, String.valueOf(id));
	}

	private String getField(String tableName, String resultField, String field, String value) {
		String sql = String.format(SQL_PATTERN, tableName, field, value);
		String result = null;
		try (Statement stat = connection.createStatement(); ResultSet set = stat.executeQuery(sql);) {
			LOG.info(sql);
			if (set.next())
				result = set.getString(resultField);
			LOG.info(result);
		} catch (Exception e) {
			LOG.error(e);
		}
		return result;
	}
}