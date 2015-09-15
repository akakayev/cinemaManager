package com.cinemaManager.report;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cinemaManager.config.Parameter;
import com.cinemaManager.config.SessionDTO;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReportWriter {

	private static final String SUMMARY_LABEL = "Summary";
	private static final String RESULT_TABLE_LABEL = "Session table";
	private static final String CONSTRAINTS_LABEL = "Specified constraints";
	private static final String EMPTY_LABEL = "-";
	private static final String RANGE_LABEL = "range";
	private static final String VALUE_LABEL = "specific value";
	private static final String MIN_VALUE_LABEL = "min-value";
	private static final String MAX_VALUE_LABEL = "max-value";
	private static final String FIELDS_LABEL = "field";
	private static final String CINEMA_FIELD="cinema_id";
	private static final String CINEMA_LABEL="cinema";
	private static final String FILM_FIELD="film_id";
	private static final String FILM_LABEL="film";
	private static final String TIME_FIELD="time";
	private static final String DATE_FIELD="date";
	private static final String ATTENDANCE_FIELD="attendance";
	private static final String INCOME_FIELD="income";
	
	private static final int FIELD_COLUMN=1;
	private static final int MIN_VALUE_COLUMN=2;
	private static final int MAX_VALUE_COLUMN=3; 
	private static final int VALUE_COLUMN=4;
	
	private File reportFile;
	private List<SessionDTO> queryResult;
	private WritableSheet sheet;
	private static final Logger LOG = Logger.getLogger(ReportWriter.class);
	private final Map<String, String> fields=new HashMap<String, String>();
	
	public ReportWriter(File file, List<SessionDTO> queryResult) {
		reportFile = file;
		this.queryResult = queryResult;
		fields.put(CINEMA_FIELD,CINEMA_LABEL);
		fields.put(FILM_FIELD, FILM_LABEL);
		fields.put(TIME_FIELD, TIME_FIELD);
		fields.put(DATE_FIELD, DATE_FIELD);
		fields.put(ATTENDANCE_FIELD, ATTENDANCE_FIELD);
		fields.put(INCOME_FIELD, INCOME_FIELD);
	}

	public void write(List<Parameter> fields, Map<String, List<Parameter>> parameters) {
		try {
			LOG.info("trying to write the report to excel");
			WritableWorkbook workbook = Workbook.createWorkbook(reportFile);
			sheet = workbook.createSheet("First Sheet", 0);
			writeResult(fields).writeSummary().writeConstraints(parameters);
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			LOG.error("write error", e);
		}
	}

	private ReportWriter writeConstraints(Map<String, List<Parameter>> parameters) throws WriteException {
		constrainsHeader();
		int row = 5;
		Label cell;
		for (String key : parameters.keySet()) {
			if (key.equals("fields"))
				continue;
			List<Parameter> par = parameters.get(key);
			if (!par.isEmpty()) {
				if (par.size() > 1)
					sheet.mergeCells(FIELD_COLUMN, row, FIELD_COLUMN, row + par.size() - 1);
				cell = new Label(1, row, fields.get(key), CellFormatter.tableStyle());
				sheet.addCell(cell);
				for (Parameter constr : par) {
					String minValue, maxValue, value;
					if (constr.getValue() != null) {
						minValue=maxValue=EMPTY_LABEL;
						value=constr.getValue();
					} else {
						value=EMPTY_LABEL;
						minValue=constr.getFrom();
						maxValue=constr.getTo();
					}
					Label valueCell = new Label(VALUE_COLUMN, row, value, CellFormatter.tableStyle());
					sheet.addCell(valueCell);
					Label minValueCell = new Label(MIN_VALUE_COLUMN, row, minValue, CellFormatter.tableStyle());
					sheet.addCell(minValueCell);
					Label maxValueCell = new Label(MAX_VALUE_COLUMN, row, maxValue, CellFormatter.tableStyle());
					sheet.addCell(maxValueCell);
					row++;
				}
			}

		}
		return this;
	}

	private ReportWriter writeResult(List<Parameter> fields) throws RowsExceededException, WriteException {
		final int resultStartColumn=6;
		final int resultEndColumn=11;
		final int resultStartRow=12;
		final int resultEndRow=13;
		addTableLabel(RESULT_TABLE_LABEL, resultStartColumn, resultStartRow, resultEndColumn, resultEndRow);
		CellView autosizeable = new CellView();
		autosizeable.setAutosize(true);
		int column = resultStartColumn;
		int row = resultEndRow+1;
		for (Parameter field : fields) {
			WritableCell cell = new Label(column, row, field.getValue(), CellFormatter.boldStyle());
			sheet.addCell(cell);
			for (SessionDTO session : queryResult) {
				cell = CellFormatter.makeCell(field, session, column, ++row);
				sheet.addCell(cell);
			}
			sheet.setColumnView(column, autosizeable);
			column++;
			row = resultEndRow+1;
		}
		return this;
	}

	private ReportWriter writeSummary() throws WriteException {
		final int summaryStartColumn=6;
		final int summaryEndColumn=7;
		final int summaryStartRow=1;
		final int summaryEndRow=2;
		addTableLabel(SUMMARY_LABEL, summaryStartColumn, summaryStartRow, summaryEndColumn, summaryEndRow);
		Summary summary = new Summary(queryResult);
		int i = summaryEndRow+1;
		for (String field : summary.getFields().keySet()) {
			WritableCell cell = new Label(summaryStartColumn, i, field, CellFormatter.boldStyle());
			sheet.addCell(cell);
			cell = new jxl.write.Number(summaryEndColumn, i++, summary.fields.get(field), CellFormatter.tableStyle());
			sheet.addCell(cell);
		}
		return this;
	}

	private void constrainsHeader() throws WriteException {
		final int constrainsStartColumn=1;
		final int constrainsEndColumn=1;
		final int constrainsStartRow=4;
		final int constrainsEndRow=2;
		final int fieldStartRow=3;
		final int fieldEndRow=4;
		final int rangeStartRow=2;
		final int rangeRowEnd=3;
		final int valueStartRow=3;
		final int valueEndRow=4;
		final int rangeRow=3;
		final int valuesRow=4;
		CellView autosizeable = new CellView();
		autosizeable.setAutosize(true);
		sheet.setColumnView(VALUE_COLUMN, autosizeable);
		sheet.setColumnView(MIN_VALUE_COLUMN, autosizeable);
		sheet.setColumnView(MAX_VALUE_COLUMN, autosizeable);
		addTableLabel(CONSTRAINTS_LABEL, constrainsStartColumn, constrainsEndColumn, constrainsStartRow, constrainsEndRow);
		sheet.mergeCells(FIELD_COLUMN, fieldStartRow, FIELD_COLUMN, fieldEndRow);
		sheet.mergeCells(rangeStartRow, rangeRow, rangeRowEnd, rangeRow);
		sheet.mergeCells(VALUE_COLUMN, valueStartRow, VALUE_COLUMN, valueEndRow);
		Label cell = new Label(FIELD_COLUMN, rangeRow, FIELDS_LABEL, CellFormatter.boldStyle());
		sheet.addCell(cell);
		cell = new Label(MIN_VALUE_COLUMN, rangeRow, RANGE_LABEL, CellFormatter.boldStyle());
		sheet.addCell(cell);
		cell = new Label(MIN_VALUE_COLUMN, valuesRow, MIN_VALUE_LABEL, CellFormatter.boldStyle());
		sheet.addCell(cell);
		cell = new Label(MAX_VALUE_COLUMN, valuesRow, MAX_VALUE_LABEL, CellFormatter.boldStyle());
		sheet.addCell(cell);
		cell = new Label(VALUE_COLUMN, rangeRow, VALUE_LABEL, CellFormatter.boldStyle());
		sheet.addCell(cell);
	}

	private void addTableLabel(String header, int r1, int c1, int r2, int c2) throws WriteException {
		sheet.mergeCells(r1, c1, r2, c2);
		Label lable = new Label(r1, c1, header, CellFormatter.headerStyle());
		sheet.addCell(lable);
	}
}