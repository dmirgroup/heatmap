/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
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
