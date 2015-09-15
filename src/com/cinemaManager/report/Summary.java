package com.cinemaManager.report;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cinemaManager.config.SessionDTO;

public class Summary {

	private static final String INCOME_LABEL = "total income";
	private static final String ATTENDANCE_LABEL = "total attendance";
	private static final String FILM_COUNTER_LABEL = "films number";
	private static final String SESSION_COUNTER_LABEL = "sessions number";
	private static final String DAYS_COUNTER_LABEL = "calendar days number";
	private static final String CINEMAS_COUNTER_LABEL = "cinemas number";
	private double totalIncome = 0;
	private int totalAttendance = 0;
	private int filmCounter;
	private int sessionCounter;
	private int daysCounter;
	private int cinemasCounter;
	public Map<String, Double> fields;

	public Summary(List<SessionDTO> queryResult) {
		calculateSummary(queryResult);
		fields = new HashMap<String, Double>();
		fields.put(INCOME_LABEL, totalIncome);
		fields.put(ATTENDANCE_LABEL, Double.valueOf(totalAttendance));
		fields.put(FILM_COUNTER_LABEL, Double.valueOf(filmCounter));
		fields.put(SESSION_COUNTER_LABEL, Double.valueOf(sessionCounter));
		fields.put(DAYS_COUNTER_LABEL, Double.valueOf(daysCounter));
		fields.put(CINEMAS_COUNTER_LABEL, Double.valueOf(cinemasCounter));
	}

	private void calculateSummary(List<SessionDTO> queryResult) {
		sessionCounter = queryResult.size();
		Set<String> films = new HashSet<String>();
		Set<Date> days = new HashSet<Date>();
		Set<String> cinemas = new HashSet<String>();
		for (SessionDTO entry : queryResult) {
			films.add(entry.getFilm());
			days.add(entry.getDate());
			cinemas.add(entry.getCinema());
			totalIncome += entry.getIncome();
			totalAttendance += entry.getAttendance();
		}
		cinemasCounter = cinemas.size();
		filmCounter = films.size();
		daysCounter = days.size();
	}

	public Map<String, Double> getFields() {
		return fields;
	}

}
