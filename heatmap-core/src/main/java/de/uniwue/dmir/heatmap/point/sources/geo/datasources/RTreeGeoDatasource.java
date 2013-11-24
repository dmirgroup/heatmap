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
package de.uniwue.dmir.heatmap.point.sources.geo.datasources;

import java.util.List;

import org.springframework.util.StopWatch;

import com.newbrightidea.util.RTree;

import de.uniwue.dmir.heatmap.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IGeoDatasource;

public class RTreeGeoDatasource<S> extends AbstractCachedGeoDatasource<S> {
	
	private IMapper<S, GeoCoordinates> mapper;
	private RTree<S> rtree;
	
	public RTreeGeoDatasource(
			IGeoDatasource<S> dataSource,
			IMapper<S, GeoCoordinates> mapper) {
		
		super(dataSource);
		this.mapper = mapper;
		
		this.rtree = new RTree<S>();
		
		this.reload();
	}

	@Override
	public List<S> getData(GeoBoundingBox geoBoundingBox) {

		double minLon;
		double minLat;
		
		double maxLon;
		double maxLat;
		
		if (geoBoundingBox == null) {
			
			minLon = -180;
			minLat = - 90;
			
			maxLon = +180;
			maxLat = + 90;
			
		} else {
			
			minLon = geoBoundingBox.getMin().getLongitude();
			minLat = geoBoundingBox.getMin().getLatitude();
			
			maxLon = geoBoundingBox.getMax().getLongitude();
			maxLat = geoBoundingBox.getMax().getLatitude();
			
		}
		
		
		List<S> data = this.rtree.search(
				new float[] {
						(float) minLon,
						(float) minLat,
				}, 
				new float[] {
						(float) (maxLon - minLon),
						(float) (maxLat - minLat)
				});

		return data;
	}

	@Override
	public void reload() {
		
		this.rtree.clear();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("getting data");
		
		List<S> data = super.dataSource.getData(null);
		

		stopWatch.stop();
		super.logger.debug("getting data: {}", stopWatch.toString());
		
		stopWatch.stop();
		stopWatch.start("adding data");
		
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
