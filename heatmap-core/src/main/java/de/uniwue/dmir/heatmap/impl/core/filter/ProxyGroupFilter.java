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
package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalGroupData;
import de.uniwue.dmir.heatmap.core.filter.IGroupAccess;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * Allows to create tiles which contain grouped data.
 * 
 * @author Martin Becker
 *
 * @param <E>
 * @param <I>
 */
public class ProxyGroupFilter<E extends IExternalGroupData, IInner, IOuter> 
extends AbstractProxyFilter<E, IInner, IOuter> {

	private IGroupAccess<IInner, IOuter> groupAccess;
	
	@Getter
	@Setter
	private String overallGroup;

	public ProxyGroupFilter(
			IGroupAccess<IInner, IOuter> groupAccess, 
			IFilter<E, IInner> filter) {
		
		super(filter);
		this.groupAccess = groupAccess;
		this.overallGroup = null;
	}
	
	@Override
	public void filter(
			E dataPoint, 
			IOuter tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		String groupId = dataPoint.getGroupId();
		IInner groupData = this.groupAccess.get(
				groupId, 
				tile, 
				tileSize, 
				tileCoordinates);
		
		super.filter.filter(
				dataPoint, 
				groupData, 
				tileSize, 
				tileCoordinates);
		
		if (this.overallGroup != null) {
			
			IInner defaultData = this.groupAccess.get(
					this.overallGroup, 
					tile, 
					tileSize, 
					tileCoordinates);
			
			this.filter.filter(
					dataPoint, 
					defaultData, 
					tileSize, 
					tileCoordinates);
		}
	}

}
