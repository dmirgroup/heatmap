package de.uniwue.dmir.heatmap.impl.core.data.type.tile;


public interface UserTile<I> {

	public I getUserData(String userId);
	public void setUserData(String userId, I userData);
	
}
