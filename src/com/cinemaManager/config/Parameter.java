package com.cinemaManager.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Parameter {

	private String value;
	private String from;
	private String to;

	public Parameter() {
		value = null;
		from = null;
		to = null;
	}

	public Parameter(String value) {
		this.value = value;
		from = null;
		to = null;
	}

	public Parameter(String from, String to) {
		this.from = from;
		this.to = to;
		value = null;
	}

	public String getValue() {
		return value;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	@JsonDeserialize
	public void setValue(String value) {
		this.value = value;
	}

	@JsonDeserialize
	public void setFrom(String from) {
		this.from = from;
	}

	@JsonDeserialize
	public void setTo(String to) {
		this.to = to;
	}

}
