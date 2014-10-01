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
package de.uniwue.dmir.heatmap.processors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class ProxyScheduledExecutorProcessor<TTile>
extends AbstractProcessor<TTile> {

	public static final long SETTINGS_SHUTDOWN_TIMEOUT = 1;
	public static final TimeUnit SETTINGS_SHUTDOWN_TIME_UNIT = TimeUnit.SECONDS;
//	public static final TimeUnit SETTINGS_PROCESSOR_TIME_UNIT= TimeUnit.SECONDS;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ScheduledExecutorService executorService;
	private ITileProcessor<TTile> tileProcessor;
	
	public ProxyScheduledExecutorProcessor(
			ITileProcessor<TTile> tileProcessor, 
			int numberOfSimultaneousThreads) {
		
		this.tileProcessor = tileProcessor;
		
		this.logger.debug("Starting up executor service and setting shutdown hook.");
		
		this.executorService =
				Executors.newScheduledThreadPool(numberOfSimultaneousThreads);
		
		Thread mainThread = Thread.currentThread();
		ExecutorServiceShutdownHook shutdownHook =
				new ExecutorServiceShutdownHook(
						mainThread,
						this.executorService, 
						SETTINGS_SHUTDOWN_TIMEOUT,
						SETTINGS_SHUTDOWN_TIME_UNIT);
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		
	}
	
	@Override
	public void process(
			final TTile tile, 
			final TileSize tileSize,
			final TileCoordinates tileCoordinates) {
		
		this.logger.debug(
				"Scheduling thread for tile {} by processor {}.",
				tileCoordinates,
				this.tileProcessor.getClass());
		
		this.executorService.schedule(
				new ExceptionLoggingThread() {
					@Override
					public void runWithoutExceptionHandling() {
						ProxyScheduledExecutorProcessor.this.tileProcessor.process(
								tile, 
								tileSize,
								tileCoordinates);
					}
				},
				0,
				TimeUnit.SECONDS);
		
	}

	
	@AllArgsConstructor
	public static class ExecutorServiceShutdownHook extends Thread {
		
		protected final Logger logger = LoggerFactory.getLogger(this.getClass());
		
		private Thread mainThread;
		private ExecutorService executorService;
		
		private long timeout;
		private TimeUnit timeUnit;
		
		@Override
		public void run() {

			this.logger.info("Shutting down executor service.");
			
			// let the current threads finish but don't start new one even if they are already scheduled
			this.executorService.shutdownNow();
			

			this.logger.info("Waiting for threads to finish.");
			
			try {
				this.executorService.awaitTermination(
						this.timeout, 
						this.timeUnit);
				this.mainThread.join();

				this.logger.info("All threads finished.");
				
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static abstract class ExceptionLoggingThread extends Thread {
		
		protected final Logger logger = LoggerFactory.getLogger(this.getClass());
		
		@Override
		public void run() {
			try {
				this.runWithoutExceptionHandling();
			} catch (Exception e) {
				this.logger.error("Thread threw an error. ", e);
			}
		}
		
		public abstract void runWithoutExceptionHandling();
	}

	@Override
	public void close() {
		// shutting down, but letting all scheduled tasks finish
		try {
			this.executorService.shutdown();
			this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			this.tileProcessor.close();
		}
	}

}
