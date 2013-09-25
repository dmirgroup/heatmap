package de.uniwue.dmir.heatmap.impl.core.data.type.external;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import de.uniwue.dmir.heatmap.core.data.type.AbstactExternalData;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ValuePixel extends AbstactExternalData {

	private double value;
	
	public ValuePixel(int x, int y, double value) {
		super(x, y);
		this.value = value;
	}
	
}