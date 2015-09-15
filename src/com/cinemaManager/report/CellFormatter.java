package com.cinemaManager.report;

import com.cinemaManager.config.Parameter;
import com.cinemaManager.config.SessionDTO;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class CellFormatter {

	private static final String DATE_PATTERN = "YYYY-MM-DD";
	private static final String TIME_PATTERN = "HH:MM";

	public static WritableCellFormat headerStyle() throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.TIMES, 16);
		cellFont.setColour(Colour.BLACK);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		cellFormat.setBackground(Colour.YELLOW);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		return cellFormat;
	}

	public static WritableCellFormat tableStyle() throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.TIMES, 10);
		cellFont.setColour(Colour.BLACK);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		return cellFormat;
	}

	public static WritableCellFormat boldStyle() throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
		cellFont.setColour(Colour.BLACK);
		cellFont.setBoldStyle(WritableFont.BOLD);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		return cellFormat;
	}

	public static WritableCell makeCell(Parameter field, SessionDTO session, int column, int row)
			throws WriteException {
		WritableCell cell;
		String key = field.getValue();
		switch (key) {
		case "id":
			cell = new Number(column, row, session.getId(), CellFormatter.tableStyle());
			break;
		case "date":
			cell = new DateTime(column, row, session.getDate(), DTformat(DATE_PATTERN));
			break;
		case "time":
			cell = new DateTime(column, row, session.getTime(), DTformat(TIME_PATTERN));
			break;
		case "attendance":
			cell = new Number(column, row, session.getAttendance(), CellFormatter.tableStyle());
			break;
		case "income":
			cell = new Number(column, row, session.getIncome(), CellFormatter.tableStyle());
			break;
		case "film":
			cell = new Label(column, row, session.getFilm(), CellFormatter.tableStyle());
			break;
		case "cinema":
			cell = new Label(column, row, session.getCinema(), CellFormatter.tableStyle());
			break;
		default:
			cell = new Label(column, row, "???", CellFormatter.tableStyle());
			break;
		}
		return cell;
	}

	public static WritableCellFormat DTformat(String pattern) throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.TIMES, 14);
		DateFormat dateFormat = new DateFormat(pattern);
		WritableCellFormat cellFormat = new WritableCellFormat(dateFormat);
		cellFont.setColour(Colour.BLACK);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		return cellFormat;
	}

	public static WritableCellFormat numberFormat(String pattern) throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.TIMES, 10);
		NumberFormat dateFormat = new NumberFormat(pattern);
		WritableCellFormat cellFormat = new WritableCellFormat(dateFormat);
		cellFont.setColour(Colour.BLACK);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		return cellFormat;
	}
}
