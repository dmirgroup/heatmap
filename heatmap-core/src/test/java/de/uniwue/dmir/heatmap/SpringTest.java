package de.uniwue.dmir.heatmap;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;

public class SpringTest {

	@Test
	public void testHeatmap() throws IOException {
		
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext(
						"spring/applicationContext.xml");
		
		@SuppressWarnings("unchecked")
		IGeoDataSource<GeoPoint> geoDataSource = 
				appContext.getBean(IGeoDataSource.class);
		
		List<GeoPoint> points = geoDataSource.getData(10, 10, 100, 100);
		for (int i = 0; i < 10; i++) {
			System.out.println(points.get(i));
		}
		
		appContext.close();
		
	}
}
