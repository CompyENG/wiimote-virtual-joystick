public class UInput {
	private static UInput u;
	public static UInput getInstance() {
		if(u == null) {
			u = new UInput();
		}
		return u;
	}

	//native methods
	private native void destroy();
	private native void init();
	//public functions
	public native void sendButtonDown(int wiimote, int key);
	public native void sendButtonUp(int wiimote, int key);

	// Load library
	static {
		System.loadLibrary("UInput");
		System.loadLibrary("suinput");
	}

	public UInput() {
		init();
	}
	
	public void kill() {
	    destroy();
    }
}
