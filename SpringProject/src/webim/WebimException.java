package webim;

@SuppressWarnings("serial")
public class WebimException extends Exception {

	private int code;
	
	public WebimException(int code, String status) {
		super(status);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
