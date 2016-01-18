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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import de.uniwue.dmir.heatmap.incremental.IProcessLimiter;
import de.uniwue.dmir.heatmap.incremental.IProcessManager;
import de.uniwue.dmir.heatmap.incremental.IProcessManager.ProcessManagerEntry;

public class EntryPointIncremental {

	public static final Logger LOGGER = LoggerFactory.getLogger(EntryPointIncremental.class);
	
	public static final String INCREMENTAL_FILE = "incremental.xml";
	public static final String HEATMAP_PROCESSOR__FILE = "heatmap-processor.xml";
	
	public static final String HEATMAP_BEAN = "heatmap";
	public static final String WRITER_BEAN = "writer";
	
	public static final String SEED_DIR = "seed";
	public static final String CURRENT_DIR = "current";
	public static final String BACKUP_DIR = "backup";

	public static final String BACKUP_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException, ParseException {

		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat backupDf = new SimpleDateFormat(BACKUP_DATE_FORMAT);
		
		String workDir = System.getProperty("workDir", ".");
		LOGGER.debug("Work dir: {}", workDir);
		String configDir = System.getProperty("configDir", ".");
		LOGGER.debug("Config dir: {}", configDir);
		
		File seedDir = new File(workDir, SEED_DIR);
		LOGGER.debug("Seed dir: {}", seedDir);
		File currentDir = new File(workDir, CURRENT_DIR);
		LOGGER.debug("Current dir: {}", currentDir);
		File backupDir = new File(workDir, BACKUP_DIR);
		LOGGER.debug("Backup dir: {}", backupDir);
		
		
		String initialMinTimeString = System.getProperty("minTime");
		LOGGER.debug("Initial minimal time parameter: {}", initialMinTimeString);
		Date initialMinTime = 
				initialMinTimeString == null 
				? new Date(0)
				: df.parse(initialMinTimeString);
		LOGGER.debug("Initial minimal time: {}", df.format(initialMinTime));
		
		String absoluteMaxTimeString = System.getProperty("maxTime");
		LOGGER.debug("Absolute maximal time parameter: {}", absoluteMaxTimeString);
		Date absoluteMaxTime = 
				absoluteMaxTimeString == null 
				? new Date() 
				: new SimpleDateFormat(DATE_FORMAT).parse(absoluteMaxTimeString);
		LOGGER.debug("Absolute maximal time: {}", df.format(absoluteMaxTime));
		
		String incrementalFile = new File("file:/" + configDir, INCREMENTAL_FILE).getPath();
		String settingsFile = new File("file:/" + configDir, HEATMAP_PROCESSOR__FILE).getPath();
		
		LOGGER.debug("Initializing incremental control file: {}", incrementalFile);
		FileSystemXmlApplicationContext incrementalContext = 
				new FileSystemXmlApplicationContext(incrementalFile);
		
		// get point limit
		int pointLimit = Integer.parseInt(
				incrementalContext.getBeanFactory().resolveEmbeddedValue(
						"${point.limit}"));
		LOGGER.debug("Print limit: {}", pointLimit);
		
		// get backups to keep
		int backupsToKeep = Integer.parseInt(
				incrementalContext.getBeanFactory().resolveEmbeddedValue(
						"${backups.to.keep}"));
		LOGGER.debug("Backups to keep: {}", pointLimit);
		
		LOGGER.debug("Initializing process components (manager and limiter).");
		IProcessManager processManager = incrementalContext.getBean(IProcessManager.class);
		IProcessLimiter processLimiter = incrementalContext.getBean(IProcessLimiter.class);

		LOGGER.debug("Starting incremental loop.");
		while (true) { // break as soon as no new points are available

			// cleanup --- just in case
			LOGGER.debug("Deleting \"current\" dir.");
			FileUtils.deleteDirectory(currentDir);
			
			// copy from seed to current
			LOGGER.debug("Copying seed.");
			seedDir.mkdirs();
			FileUtils.copyDirectory(seedDir, currentDir);
			

			
			// get min time
			LOGGER.debug("Getting minimal time ...");
			Date minTime = initialMinTime;
			ProcessManagerEntry entry = processManager.getEntry();
			if (entry != null && entry.getMaxTime() != null) {
				minTime = entry.getMaxTime();
			}
			LOGGER.debug("Minimal time: {}", new SimpleDateFormat(DATE_FORMAT).format(minTime));
			
			// break if we processed all available points (minTime is greater than or equal to absoluteMaxTime)
			if (minTime.getTime() >= absoluteMaxTime.getTime()) {
				LOGGER.debug("Processed all points.");
				break;
			}
			
			
			// get the maximal time
			LOGGER.debug("Get maximal time.");

			// get the time from the newest point in our point range (pointMaxTime) ...
			Date pointMaxTime = processLimiter.getMaxTime(minTime, pointLimit);

			// ... and possibly break the loop if no new points are available
			if (pointMaxTime == null) break;
			
			// set the max time and make sure we are not taking to many points 
			// (set max time to the minimum of pointMaxTime and absoluteMaxTime)
			Date maxTime = 
					pointMaxTime.getTime() > absoluteMaxTime.getTime() 
					? absoluteMaxTime 
					: pointMaxTime;

			LOGGER.debug("Maximal time: {}", new SimpleDateFormat(DATE_FORMAT).format(maxTime));
			
			// start process
			processManager.start(minTime);
			
			System.setProperty(
					"minTimestamp", new SimpleDateFormat(DATE_FORMAT).format(minTime));

			System.setProperty(
					"maxTimestamp", new SimpleDateFormat(DATE_FORMAT).format(maxTime));
			
			FileSystemXmlApplicationContext heatmapContext = 
					new FileSystemXmlApplicationContext(settingsFile);
			
			IHeatmap heatmap = 
					heatmapContext.getBean(HEATMAP_BEAN, IHeatmap.class);

			ITileProcessor tileProcessor = 
					heatmapContext.getBean(WRITER_BEAN, ITileProcessor.class);
			
			heatmap.processTiles(tileProcessor);

			tileProcessor.close();
			heatmapContext.close();
			
			// finish process
			processManager.finish(pointMaxTime);
			
			// move old seed
			if (backupsToKeep > 0) {
				FileUtils.moveDirectory(
					seedDir, 
					new File(backupDir, backupDf.format(maxTime)));
				
				// cleanup backups
				String[] backups = 
						backupDir.list(DirectoryFileFilter.DIRECTORY);
				File oldestBackup = null;
				if (backups.length > backupsToKeep) {
					for (String bs : backups) {
						File b = new File(backupDir, bs);
						if (oldestBackup == null || oldestBackup.lastModified() > b.lastModified()) {
							oldestBackup = b;
						}
					}
					FileUtils.deleteDirectory(oldestBackup);
				}
				
			} else {
				FileUtils.deleteDirectory(seedDir);
			}
			
			// move new seed
			FileUtils.moveDirectory(
					currentDir, 
					seedDir);
			
		}
		
		incrementalContext.close();
		
	}
}
