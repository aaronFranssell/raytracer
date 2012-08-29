package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class LogToFile
{
	private static Writer out = null;
	private static String filePath = null;
	public static final int bufferSize = 2000000;
	private static StringBuffer buffer = new StringBuffer();
	public static void logln(String output)
	{
		if(out == null)
		{
			try
			{
				out = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		try
		{
			buffer.append(output +"\n");
			if(buffer.length() > bufferSize)
			{
				out.write(buffer.toString());
				buffer = new StringBuffer();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		if(filePath == null)
		{
			FileLocator loc;
			try
			{
				loc = new FileLocator();
				filePath = loc.getDebuggingDirectory() + "output.txt";
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public static void close()
	{
		if(out == null)
		{
			return;
		}
		try
		{
			out.write(buffer.toString());
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

}
