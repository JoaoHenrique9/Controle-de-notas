package gui.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.exceptions.ValidationException;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Window currentStageForm(ActionEvent event) {
		return ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Date TryParseToDate(String str) {
		ValidationException exception = new ValidationException("Validation error");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if ((str.charAt(2) != '/') || (str.charAt(5) != '/')) {
			exception.addError("DataErrada", "Data incorreta.");
		} else {
			try {
				Date date = new Date();
				date = sdf.parse(str);

				return date;

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return null;

	}

	public static Time TryParseToTime(String time) {
		ValidationException exception = new ValidationException("Validation error");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (time.charAt(2) != ':') {
			exception.addError("HoraErrada", "Hora incorreta.");

		} else {
			try {
				java.util.Date date = sdf.parse(time + ":00");
				Time sqlDate = new java.sql.Time(date.getTime());

				return sqlDate;

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return null;

	}

}
