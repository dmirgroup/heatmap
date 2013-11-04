package de.uniwue.dmir.heatmap.core.processing.mapper;

import java.security.NoSuchAlgorithmException;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.util.HashUtils;

@AllArgsConstructor
public class StringHashMapper implements IMapper<String, String> {

	public static final String MD5 = "MD5";
	
	private String hashAlgorithm;
	
	public StringHashMapper() {
		this.hashAlgorithm = MD5;
	}
	
	@Override
	public String map(String object) {
		try {
			return HashUtils.digest(object, this.hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
