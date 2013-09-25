package de.uniwue.dmir.heatmap.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uniwue.dmir.heatmap.core.processing.ITileIterator;
import de.uniwue.dmir.heatmap.core.processing.ITileProcessor;

public class EntryPoint {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext("settings.xml");
		
		IHeatmap heatmap = 
				appContext.getBean(IHeatmap.class);
		
		ITileIterator tileIterator =
				appContext.getBean(ITileIterator.class);

		ITileProcessor tileProcessor =
				appContext.getBean(ITileProcessor.class);
		
		tileIterator.iterate(heatmap, tileProcessor);

		appContext.close();
	}
}
