package de.uniwue.dmir.heatmap.core.filters.apic;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.ApicPoint;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

@AllArgsConstructor
public class PointToGroupMapper
implements IMapper<ApicPoint, String>{

	private IMapper<String, String> userToGroupMapper;
	private IMapper<String, String> deviceToGroupMapper;
	private IMapper<String, String> macToGroupMapper;
	
	@Override
	public String map(ApicPoint object) {
		
		String group = null;
		
		if (group == null) {
			group = this.userToGroupMapper.map(object.getUserId());
		}
		
		if (group == null) {
			group = this.deviceToGroupMapper.map(object.getDeviceId());
		}
		
		if (group == null) {
			group = this.macToGroupMapper.map(object.getMac());
		}
		
		return group;
	}

}
