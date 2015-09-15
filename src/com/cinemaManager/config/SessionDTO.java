package com.cinemaManager.config;

import java.sql.Date;
import java.sql.Time;

public class SessionDTO {
	private int id;
	private Date date;
	private Time time;
	private String film;
	private int attendance;
	private double income;
	private String cinema;

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public String getFilm() {
		return film;
	}

	public int getAttendance() {
		return attendance;
	}

	public double getIncome() {
		return income;
	}

	public String getCinema() {
		return cinema;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setFilm(String film) {
		this.film = film;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public void setCinema(String cinema) {
		this.cinema = cinema;
	}

	@Override
	public String toString() {
		return "SessionDTO [id=" + id + ", date=" + date + ", time=" + time + ", film=" + film + ", attendance="
				+ attendance + ", income=" + income + ", cinema=" + cinema + "]";
	}

}
