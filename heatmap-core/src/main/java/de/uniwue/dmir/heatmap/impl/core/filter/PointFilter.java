package de.uniwue.dmir.heatmap.impl.core.filter;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.filter.IPixelAccess;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.GroupValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.PointSize;

public class PointFilter<T>
extends AbstractConfigurableFilter<GroupValuePixel, T> {

	/**
	 * Half an hour in milliseconds.
	 */
	public static final long HALF_AN_HOUR = 60 * 30 * 1000;
	
	public static final double POINTS = 1;
	
	private IPixelAccess<PointSize, T> pixelAccess;

	public PointFilter(IPixelAccess<PointSize, T> pixelAccess) {
		this.pixelAccess = pixelAccess;
	}
	
	@Override
	public void filter(
			GroupValuePixel dataPoint, 
			T tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		PointSize pointSize = this.pixelAccess.get(
				dataPoint.getCoordinates(), 
				tile, 
				tileSize);
		
		if (pointSize == null) {
		
			// create new point if not point has been created so far
			
			pointSize = new PointSize();
			pointSize.setCoordinates(dataPoint.getCoordinates());
			pointSize.setMaxDate(dataPoint.getTimestamp());
			pointSize.setPoints(POINTS);
			pointSize.setSize(1);
			
			// set new point
			this.pixelAccess.set(
					pointSize, 
					dataPoint.getCoordinates(), 
					tile, 
					tileSize);
			
		} else {
			
			// calculate time difference between current data point and last counted measurement
			long timeDifference = 
					dataPoint.getTimestamp().getTime() 
					- pointSize.getMaxDate().getTime();
			
			// if last counted measurement is old enough, 
			// then update points and timestamp
			if (timeDifference > HALF_AN_HOUR) {
			
				
				double newPoints = pointSize.getPoints() + POINTS;
				pointSize.setPoints(newPoints);
				pointSize.setMaxDate(dataPoint.getTimestamp());
			}

			// increase size
			pointSize.setSize(pointSize.getSize() + 1);
			
		}

	}
	

}
