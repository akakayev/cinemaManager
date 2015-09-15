package com.cinemaManager.control;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cinemaManager.config.Parameter;
import com.cinemaManager.config.ParameterParser;
import com.cinemaManager.config.SessionDTO;
import com.cinemaManager.dataBase.DBConnector;
import com.cinemaManager.report.ReportWriter;

public class ReportController {

	private static final String DB_URL = "jdbc:mysql://localhost/Cinema";
	private static final String REPORT_FILE_PATH = "report1.xls";
	// Database credentials
	private static final String USER = "root";
	private static final String PASS = "qwerty10253";

	private static final Logger LOG = Logger.getLogger(ReportController.class);

	public ReportController(Map<String, List<Parameter>> parameters) {
		LOG.info("getting parameters of the report");
		ParameterParser parser = new ParameterParser();
		String sql = parser.parse(parameters);
		DBConnector connector = DBConnector.CONNECTOR;
		connector.connect(DB_URL, USER, PASS);
		List<SessionDTO> resultList = connector.getSessionList(sql);
		connector.close();
		ReportWriter writer = new ReportWriter(new File(REPORT_FILE_PATH), resultList);
		writer.write(parameters.get("fields"), parameters);
	}

	public String getReportFilePath() {
		return REPORT_FILE_PATH;
	}
}