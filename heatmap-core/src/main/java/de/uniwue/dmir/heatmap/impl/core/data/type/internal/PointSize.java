package de.uniwue.dmir.heatmap.impl.core.data.type.internal;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PointSize extends Size {
	private Date maxDate;
	private double points;
}
