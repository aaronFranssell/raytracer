package etc;

import java.io.IOException;

@SuppressWarnings("serial")
public class RaytracerException extends Exception {
	public RaytracerException(String message) {
		super(message);
	}

	public RaytracerException(IOException e) {
		super(e);
	}
}
