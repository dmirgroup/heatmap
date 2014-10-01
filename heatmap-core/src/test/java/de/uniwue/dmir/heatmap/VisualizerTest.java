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
package de.uniwue.dmir.heatmap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.junit.Test;

import de.uniwue.dmir.heatmap.processors.pixelmappers.WeightedSumToAverageMapper;
import de.uniwue.dmir.heatmap.processors.pixelmappers.WeightedSumToOnOffSizeMapper;
import de.uniwue.dmir.heatmap.processors.visualizers.AlphaMaskProxyVisualizer;
import de.uniwue.dmir.heatmap.processors.visualizers.GenericSimpleRbfVisualizer;
import de.uniwue.dmir.heatmap.processors.visualizers.color.ImageColorScheme;
import de.uniwue.dmir.heatmap.processors.visualizers.color.SimpleAlphaColorScheme;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.aggregators.MaxRbfAggregator;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.aggregators.QuadraticRbfAggregator;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.tiles.pixels.WeightedSquaredSumPixel;
import de.uniwue.dmir.heatmap.tiles.pixels.WeightedSumPixel;
import de.uniwue.dmir.heatmap.util.iterator.MapKeyValueIteratorFactory;

public class VisualizerTest {

	public static final String DATA = 
			"{"
		+ "\"RelativeCoordinates(x=248, y=258)\":{\"size\":5.0,\"sumOfWeights\":5.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=259)\":{\"size\":191.0,\"sumOfWeights\":191.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=258)\":{\"size\":423.0,\"sumOfWeights\":423.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=257)\":{\"size\":33.0,\"sumOfWeights\":33.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=256)\":{\"size\":53.0,\"sumOfWeights\":53.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=260)\":{\"size\":147.0,\"sumOfWeights\":147.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=255)\":{\"size\":126.0,\"sumOfWeights\":126.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=255)\":{\"size\":105.0,\"sumOfWeights\":105.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=254)\":{\"size\":1652.0,\"sumOfWeights\":1652.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=260)\":{\"size\":8.0,\"sumOfWeights\":8.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=257)\":{\"size\":2317.0,\"sumOfWeights\":2317.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=259)\":{\"size\":7.0,\"sumOfWeights\":7.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=256)\":{\"size\":6966.0,\"sumOfWeights\":6966.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=251)\":{\"size\":1047.0,\"sumOfWeights\":1047.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=253)\":{\"size\":526.0,\"sumOfWeights\":526.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=247, y=252)\":{\"size\":480.0,\"sumOfWeights\":480.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=254)\":{\"size\":23.0,\"sumOfWeights\":23.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=253)\":{\"size\":38.0,\"sumOfWeights\":38.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=252)\":{\"size\":92.0,\"sumOfWeights\":92.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=248, y=251)\":{\"size\":137.0,\"sumOfWeights\":137.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0},"
		+ "\"RelativeCoordinates(x=26, y=197)\":{\"size\":94.0,\"sumOfWeights\":94.0,\"sumOfValues\":0.0,\"sumOfWeightedValues\":0.0,\"sumOfSquaredValues\":0.0,\"sumOfWeightedSquaredValues\":0.0}}";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		
		SimpleModule module = new SimpleModule("EnhancedDatesModule", new Version(0, 1, 0, "alpha"));
		module.addKeyDeserializer(RelativeCoordinates.class, new KeyDeserializer() {

			@Override
			public Object deserializeKey(
					String key, 
					DeserializationContext ctxt)
					throws IOException, JsonProcessingException {
				
				String coordinates = key.replaceAll("[^\\d ]", "");
				
				Scanner scanner = new Scanner(coordinates);
				RelativeCoordinates relativeCoordinates = 
						new RelativeCoordinates(
								scanner.nextInt(), 
								scanner.nextInt());
				scanner.close();
				
				return relativeCoordinates;
			}
			
		});
		
		this.objectMapper.registerModule(module);
		
		
		Map<RelativeCoordinates, WeightedSumPixel> tile = 
				this.objectMapper.readValue(
				DATA, 
				this.objectMapper.getTypeFactory().constructMapLikeType(
						HashMap.class, 
						RelativeCoordinates.class, 
						WeightedSquaredSumPixel.class));
		
		ImageColorScheme colorScheme = new ImageColorScheme(
				ImageIO.read(new File("src/main/resources/color-schemes/everyaware.png")), 
				0.5, 
				10);
		
		ITileSizeProvider tileSizeProvider = new SameTileSizeProvider();
		
		GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSumPixel>, WeightedSumPixel> averageVisualizer =
				new GenericSimpleRbfVisualizer<Map<RelativeCoordinates,WeightedSumPixel>, WeightedSumPixel>(
						tileSizeProvider,
						new MapKeyValueIteratorFactory<RelativeCoordinates, WeightedSumPixel>(), 
						new QuadraticRbfAggregator.Factory<WeightedSumPixel>(
								new WeightedSumToAverageMapper(), 
								10), 
						colorScheme);
		averageVisualizer.setUseRtree(true);
		averageVisualizer.setWidth(60);
		averageVisualizer.setHeight(60);
		averageVisualizer.setCenterX(30);
		averageVisualizer.setCenterY(30);
		
		GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSumPixel>, WeightedSumPixel> alphaVisualizer =
				new GenericSimpleRbfVisualizer<Map<RelativeCoordinates,WeightedSumPixel>, WeightedSumPixel>(
						tileSizeProvider,
						new MapKeyValueIteratorFactory<RelativeCoordinates, WeightedSumPixel>(), 
						new MaxRbfAggregator.Factory<WeightedSumPixel>(
								new WeightedSumToOnOffSizeMapper(), 
								10), 
						new SimpleAlphaColorScheme());
		averageVisualizer.setUseRtree(true);
		averageVisualizer.setWidth(60);
		averageVisualizer.setHeight(60);
		averageVisualizer.setCenterX(30);
		averageVisualizer.setCenterY(30);
		
		AlphaMaskProxyVisualizer<Map<RelativeCoordinates, WeightedSumPixel>> maskVisualizer =
				new AlphaMaskProxyVisualizer<Map<RelativeCoordinates, WeightedSumPixel>>(
						tileSizeProvider,
						averageVisualizer, 
						alphaVisualizer);
		
//		BufferedImage image = 
				maskVisualizer.visualize(
					tile, 
					new TileCoordinates(0,0,0));
		
//		ImageUtil.displayImage(image);
		
//		ImageIO.write(image, "png", new File("out/test.png"));
	}

}
