package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import helper.TestsHelper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class UtilImpl_IntegrationTests {
  @Test
  public void readImage_WithTestImage_ExpectImage() throws IOException, URISyntaxException {
    UtilImpl classUnderTest = new UtilImpl();
    int[][][] expected = new int[1][1][3];
    expected[0][0][0] = 255;
    expected[0][0][1] = 242;
    expected[0][0][2] = 0;
    URL resource = this.getClass().getClassLoader().getResource("testReadImage.png");
    int[][][] actual = classUnderTest.readImage(Paths.get(resource.toURI()).toString());

    assertEquals(expected.length, actual.length);
    assertEquals(expected[0].length, actual[0].length);
    assertEquals(expected[0][0][0], actual[0][0][0]);
    assertEquals(expected[0][0][1], actual[0][0][1]);
    assertEquals(expected[0][0][2], actual[0][0][2]);
  }

  @Test
  public void readTextFile_WithTextFile_ExpectTest() throws IOException, URISyntaxException {
    URL resource = this.getClass().getClassLoader().getResource("textFile.txt");
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("asdf asdf");
    expected.add(";lkj ;lkj");

    UtilImpl classUnderTest = new UtilImpl();

    ArrayList<String> actual = classUnderTest.readTextFile(Paths.get(resource.toURI()).toString());

    TestsHelper.arrayListSubsets(expected, actual);
  }
}
