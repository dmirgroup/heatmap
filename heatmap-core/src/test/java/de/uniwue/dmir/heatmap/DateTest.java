package de.uniwue.dmir.heatmap;

import java.util.Calendar;
import java.util.TimeZone;

public class DateTest {
	public static void main(String[] args) {
		Calendar cal = 
//				Calendar.getInstance();
				Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
		// UTC
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 06);

		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		System.out.println("----------------");
		System.out.println(cal.getTime());
//		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println(cal.getTime());
		System.out.println(cal.getTime().getTime());
		System.out.println(cal.getTimeInMillis());
	}

}
