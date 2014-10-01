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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class EntryPoint {

	public static final Logger LOGGER = LoggerFactory.getLogger(EntryPoint.class);
	
	public static final String SETTINGS_FILE = "settings.xml";

	public static final String HEATMAP_BEAN = "heatmap";
	public static final String WRITER_BEAN = "writer";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {

		String settingsFile;
		if (args.length > 0 && args[0] != null) {
			settingsFile = args[0];
		} else {
			settingsFile = SETTINGS_FILE;
		}
		
		LOGGER.debug("Reading settings file: {}", settingsFile);
		
		
		FileSystemXmlApplicationContext appContext = 
				new FileSystemXmlApplicationContext(settingsFile);
		
		IHeatmap heatmap = 
				appContext.getBean(HEATMAP_BEAN, IHeatmap.class);

		ITileProcessor tileProcessor = 
				appContext.getBean(WRITER_BEAN, ITileProcessor.class);
		
		// TODO: adjust
		heatmap.processTiles(tileProcessor, null, null, null);

		tileProcessor.close();
		appContext.close();
	}
}
