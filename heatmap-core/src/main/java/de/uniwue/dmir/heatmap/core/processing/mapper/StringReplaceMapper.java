package de.uniwue.dmir.heatmap.core.processing.mapper;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;

@AllArgsConstructor
public class StringReplaceMapper
implements IMapper<String, String> {

	private String regex;
	private String replacement;
	
	@Override
	public String map(String object) {
		return object.replaceAll(this.regex, this.replacement);
	}

}
