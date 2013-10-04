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
package de.uniwue.dmir.heatmap.core;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import de.uniwue.dmir.heatmap.core.processing.ITileProcessor;

public class EntryPoint {

	public static final String SETTINGS_FILE = "settings.xml";
	
	public static final String HEATMAP_BEAN = "heatmap";
	public static final String WRITER_BEAN = "writer";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		FileSystemXmlApplicationContext appContext = 
				new FileSystemXmlApplicationContext(SETTINGS_FILE);
		
		IHeatmap heatmap = 
				appContext.getBean(HEATMAP_BEAN, IHeatmap.class);

		ITileProcessor tileProcessor = 
				appContext.getBean(WRITER_BEAN, ITileProcessor.class);
		
		heatmap.processTiles(tileProcessor);

		appContext.close();
	}
}
