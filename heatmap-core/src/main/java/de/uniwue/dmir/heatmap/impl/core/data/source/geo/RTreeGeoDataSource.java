package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import com.newbrightidea.util.RTree;

import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;
import de.uniwue.dmir.heatmap.core.data.source.geo.IToGeoCoordinatesMapper;

public class RTreeGeoDataSource<S> extends AbstractCachedGeoDataSource<S> {
	
	private IToGeoCoordinatesMapper<S> mapper;
	private RTree<S> rtree;
	
	public RTreeGeoDataSource(
			IGeoDataSource<S> dataSource,
			IToGeoCoordinatesMapper<S> mapper) {
		
		super(dataSource);
		this.mapper = mapper;
		
		this.rtree = new RTree<S>();
		
		this.reload();
	}

	@Override
	public List<S> getData(
			double westLon, 
			double northLat, 
			double eastLon,
			double southLat) {
		
		List<S> data = this.rtree.search(
				new float[] {
						(float) westLon,
						(float) southLat,
				}, 
				new float[] {
						(float) (eastLon - westLon),
						(float) (northLat - southLat)
				});

		return data;
	}

	@Override
	public void reload() {
		
		this.rtree.clear();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		List<S> data = super.dataSource.getData(-180, 90, 180, -90);
		

		stopWatch.stop();
		super.logger.debug("getting data: {}", stopWatch.toString());
		
		stopWatch.reset();
		stopWatch.start();
		
		for (S s : data) {
			GeoCoordinates geoCoordinates = this.mapper.map(s);
			this.rtree.insert(
					new float[] {
							(float) geoCoordinates.getLongitude(),
							(float) geoCoordinates.getLatitude()
					},
					s);
		}
		
		stopWatch.stop();
		super.logger.debug("building r-tree: {}", stopWatch.toString());
	}

}
