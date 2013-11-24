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
package de.uniwue.dmir.heatmap.filters.groupaccess;

import java.util.Map;

import de.uniwue.dmir.heatmap.ITileFactory;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class MapGroupAccess<TGroupData> 
extends AbstractGroupAccess<TGroupData, Map<String, TGroupData>> {

	public MapGroupAccess(ITileFactory<TGroupData> tileFactory) {
		super(tileFactory);
	}

	@Override
	public TGroupData get(
			String groupId, 
			Map<String, TGroupData> tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		TGroupData groupData = tile.get(groupId);
		if (groupData == null) {
			groupData = super.groupDataFactory.newInstance(tileSize, tileCoordinates);
			tile.put(groupId, groupData);
		}
		return groupData;
	}

}
