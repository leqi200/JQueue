package com.tonsincs.interfaces;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface TestCallerDll extends StdCallLibrary {
	TestCallerDll INSTANCE=(TestCallerDll)Native.loadLibrary("Caller",TestCallerDll.class);
	
	public int Open();
	
}
