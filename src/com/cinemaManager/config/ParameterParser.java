package com.cinemaManager.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cinemaManager.dataBase.DBConnector;

public class ParameterParser {

    private static final String WHERE = " WHERE ";
    private static final String AND_SEPARATOR = "AND";
    private static final String OR_SEPARATOR = " OR ";
    private static final String SELECT = "SELECT * FROM Sessions ";
    private static final String VALUE_PATTERN = " %s LIKE '%s' ";
    private static final String NUMBER_PATTERN = " %s >= '%s' AND %s <= '%s' ";
    private static final String DATE_TIME_PATTERN = " %s BETWEEN '%s' AND '%s' ";
    private static final String SINGLE_CONDITION_PATTERN = "(%s)";
    private static final String DB_URL = "jdbc:mysql://localhost/Cinema";
    private static final String USER = "root";
    private static final String PASS = "qwerty10253";

    private final Map<String, String> patterns = new HashMap<String, String>();
    private final Map<String, String> tables = new HashMap<String, String>();

    private static final Logger LOG = Logger.getLogger(ParameterParser.class);

    private StringBuilder sql;

    public ParameterParser() {
        sql = new StringBuilder(SELECT);
        patterns.put("film_id", VALUE_PATTERN);
        patterns.put("cinema_id", VALUE_PATTERN);
        patterns.put("income", NUMBER_PATTERN);
        patterns.put("attendance", NUMBER_PATTERN);
        patterns.put("date", DATE_TIME_PATTERN);
        patterns.put("time", DATE_TIME_PATTERN);
        tables.put("cinema_id", "Cinemas");
        tables.put("film_id", "films");
    }

    public String parse(Map<String, List<Parameter>> parameters) {
        LOG.info("parsing parameters to sql query");
        List<String> conditions = new ArrayList<String>();
        List<String> conditionSequences = new ArrayList<String>();
        StringBuilder result = new StringBuilder();
        for (String parameterName : parameters.keySet()) {
            if ("fields".equals(parameterName))
                continue;
            if ("cinema_id".equals(parameterName) || "film_id".equals(parameterName)) {
                for (Parameter param : parameters.get(parameterName)) {
                    conditions.add(selectPattern(parameterName,
                            new Parameter(DBConnector.CONNECTOR.connect(DB_URL, USER, PASS).getIdByName(
                                    tables.get(parameterName), param.getValue()))));
                }
            } else
                for (Parameter param : parameters.get(parameterName)) {
                    conditions.add(selectPattern(parameterName, param));
                }
            String condition = buildConditionSequence(conditions, OR_SEPARATOR).toString();
            if (!condition.isEmpty())
                conditionSequences.add(String.format(SINGLE_CONDITION_PATTERN, condition));
            conditions.clear();
        }
        result.append(buildConditionSequence(conditionSequences, AND_SEPARATOR));
        if (!result.toString().isEmpty())
            sql.append(WHERE).append(result);
        LOG.info("SQL query: " + sql);
        return sql.toString();
    }

    private String selectPattern(String name, Parameter parameter) {
        String result;
        if (parameter.getValue() != null)
            result = applyPattern(VALUE_PATTERN, name, parameter.getValue());
        else
            result = applyPattern(patterns.get(name), name, parameter.getFrom(), parameter.getTo());
        return result;
    }

    private String applyPattern(String pattern, String fieldName, String value) {
        return String.format(pattern, fieldName, value);
    }

    private String applyPattern(String pattern, String fieldName, String minValue, String maxValue) {
        if (NUMBER_PATTERN.equals(pattern))
            return String.format(pattern, fieldName, minValue, fieldName, maxValue);
        else
            return String.format(pattern, fieldName, minValue, maxValue);
    }

    private StringBuilder buildConditionSequence(List<String> parameters, String separator) {
        StringBuilder result = new StringBuilder();
        if (!parameters.isEmpty())
            for (int i = parameters.size() - 1; i >= 0; i--) {
                if (i == parameters.size() - 1) {
                    result.append(parameters.get(i));
                } else {
                    result.append(separator).append(parameters.get(i));
                }
            }
        return result;
    }
}
