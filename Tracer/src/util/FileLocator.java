package util;

import java.io.File;
import java.io.IOException;

public class FileLocator
{
	private static String IMAGE_DIR_PATH = "\\img\\";
	private static String pathToLogDirectory = "\\debugging\\";
	private String basePath;
	
	public FileLocator() throws IOException
	{
		File dir = new File(".");
		basePath = dir.getCanonicalPath();
	}
	
	public String getImageDirectory()
	{
		return basePath + IMAGE_DIR_PATH;
	}
	
	public String getDebuggingDirectory()
	{
		return basePath + pathToLogDirectory;
	}
}
