package de.uniwue.dmir.heatmap.core.filters.access.mappers;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

public abstract class AbstractGroupIdMapper<T> 
implements IMapper<T, List<String>> {

	public static final String DEFAULT_OVERALL_GROUP_ID = "OVERALL";
	
	@Getter
	@Setter
	private String overallGroupId;
	
	protected abstract List<String> mapInteral(T object);
	
	@Override
	public List<String> map(T object) {
		
		List<String> ids = this.mapInteral(object);
		if (this.overallGroupId != null) {
			if (ids == null) {
				ids = new ArrayList<String>();
			}
			ids.add(this.overallGroupId);
		}
		
		return ids;
	}

}
