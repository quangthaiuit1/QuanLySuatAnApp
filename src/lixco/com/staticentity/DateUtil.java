package lixco.com.staticentity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public static boolean compareHHMM(Date date, String hhmm) {
		Date dateCurrent = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String getCurrentTime = sdf.format(dateCurrent.getTime());
		String getTestTime = hhmm;
		if (getCurrentTime.compareTo(getTestTime) > 0) {
			return true;
		}
		if (getCurrentTime.compareTo(getTestTime) < 0) {
			return false;
		} else {
			return true;
		}
	}

	public static Date SET_HHMMSS_00(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}

	public static Date DATE_WITHOUT_TIME(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return formatter.parse(formatter.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date HANDLE_START_TIME(int hour, int minutes, int ca) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MINUTE, -45);
		if (ca == 3) {
			cal.add(Calendar.DATE, +1);
		}
		return cal.getTime();
	}

	public static Date HANDLE_END_TIME(int hour, int minutes, int ca) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MINUTE, +105);
		if (ca == 3) {
			cal.add(Calendar.DATE, +1);
		}
		return cal.getTime();
	}

	public static java.sql.Timestamp GET_CURRENT_TIMESTAMP() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

}
