package com.cinemaManager.config;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONConfigParser {

	private static final Logger LOG = Logger.getLogger(JSONConfigParser.class);
	
	private String jsonString;
	private Map<String, List<Parameter>> parameters;

	public JSONConfigParser(String json) {
		jsonString = json;
	}

	public Map<String, List<Parameter>>  parse() {
		ObjectMapper mapper = new ObjectMapper();
		LOG.info("trying to deserialize json config");
		try {
			parameters = mapper.readValue(jsonString, new TypeReference<Map<String, List<Parameter>>>() {
			});
		} catch (Exception e) {
			LOG.error("Deserialize error", e);
		}
		return parameters;
	}
}
