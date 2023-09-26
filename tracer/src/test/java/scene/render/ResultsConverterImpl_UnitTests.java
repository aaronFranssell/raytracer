package scene.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ResultsConverterImpl_UnitTests
{
	private final int[][][] combinedArray = new int[3][2][3];
	
	@SuppressWarnings("unchecked")
	@Test
	public void getImageFromResults_WithMocks_ExpectImage() throws InterruptedException, ExecutionException
	{
		
		combinedArray[0][0][0] = 76;
		combinedArray[0][0][1] = 76;
		combinedArray[0][0][2] = 76;
		combinedArray[1][0][0] = 127;
		combinedArray[1][0][1] = 127;
		combinedArray[1][0][2] = 127;
		combinedArray[2][0][0] = 178;
		combinedArray[2][0][1] = 178;
		combinedArray[2][0][2] = 178;
		combinedArray[0][1][0] = 51;
		combinedArray[0][1][1] = 51;
		combinedArray[0][1][2] = 51;
		combinedArray[1][1][0] = 102;
		combinedArray[1][1][1] = 102;
		combinedArray[1][1][2] = 102;
		combinedArray[2][1][0] = 153;
		combinedArray[2][1][1] = 153;
		combinedArray[2][1][2] = 153;
		
		int width = 3;
		int height = 2;
		double[][][] imageArray1 = new double[width][1][3];
		imageArray1[0][0][0] = 0.3;
		imageArray1[0][0][1] = 0.3;
		imageArray1[0][0][2] = 0.3;
		imageArray1[1][0][0] = 0.5;
		imageArray1[1][0][1] = 0.5;
		imageArray1[1][0][2] = 0.5;
		imageArray1[2][0][0] = 0.7;
		imageArray1[2][0][1] = 0.7;
		imageArray1[2][0][2] = 0.7;
		Future<double[][][]> result1 = Mockito.mock(Future.class);
		Mockito.when(result1.get()).thenReturn(imageArray1);
		
		double[][][] imageArray2 = new double[width][1][3];
		imageArray2[0][0][0] = 0.2;
		imageArray2[0][0][1] = 0.2;
		imageArray2[0][0][2] = 0.2;
		imageArray2[1][0][0] = 0.4;
		imageArray2[1][0][1] = 0.4;
		imageArray2[1][0][2] = 0.4;
		imageArray2[2][0][0] = 0.6;
		imageArray2[2][0][1] = 0.6;
		imageArray2[2][0][2] = 0.6;
		Future<double[][][]> result2 = Mockito.mock(Future.class);
		Mockito.when(result2.get()).thenReturn(imageArray2);
		
		ArrayList<Future<double[][][]>> resultsList = new ArrayList<Future<double[][][]>>();
		resultsList.add(result1);
		resultsList.add(result2);
		
		WritableRaster spyRaster = Mockito.mock(WritableRaster.class);
		BufferedImage mockImage = Mockito.mock(BufferedImage.class);
		Mockito.when(mockImage.getRaster()).thenReturn(spyRaster);
		
		ResultsScaler mockScaler = Mockito.mock(ResultsScaler.class);
		Mockito.when(mockScaler.scale(Mockito.any(int[][][].class))).thenReturn(combinedArray);
		
		ResultsConverterImpl classUnderTest = new ResultsConverterImpl(resultsList, width, height, mockScaler, mockImage);
		
		
		BufferedImage retImage = classUnderTest.getImageFromResults();
		
		
		Mockito.verify(result1, Mockito.times(1)).get();
		Mockito.verify(result2, Mockito.times(1)).get();
		Mockito.verify(mockScaler, Mockito.times(1)).scale(argThat(new ResultsConvertImplArugmentMatcher(combinedArray)));
		Mockito.verify(spyRaster, Mockito.times(1)).setPixel(0, 1, combinedArray[0][0]);
		Mockito.verify(spyRaster, Mockito.times(1)).setPixel(1, 1, combinedArray[1][0]);
		Mockito.verify(spyRaster, Mockito.times(1)).setPixel(2, 1, combinedArray[2][0]);
		Mockito.verify(spyRaster, Mockito.times(1)).setPixel(0, 0, combinedArray[0][1]);
		Mockito.verify(spyRaster, Mockito.times(1)).setPixel(1, 0, combinedArray[1][1]);
		Mockito.verify(spyRaster, Mockito.times(1)).setPixel(2, 0, combinedArray[2][1]);
		assertEquals(mockImage, retImage);
	}
}
