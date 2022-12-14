package com.tyron.compiler;

import ir.webesho.org.ApplicationLoader;
import ir.webesho.org.util.Decompress;
import com.tyron.compiler.exception.CompilerException;
import java.io.IOException;

import java.io.File;

public abstract class Compiler {
	
	public interface OnProgressUpdateListener {
		void onProgressUpdate(String... update);
	}
	
	protected OnProgressUpdateListener listener;
	
	public void setProgressListener(OnProgressUpdateListener listener) {
		this.listener = listener;
	}
	
	public void onProgressUpdate(String... update) {
		if (listener != null) {
			listener.onProgressUpdate(update);
		}
	}
	
	abstract public void prepare();
	
	abstract public void run() throws CompilerException, IOException;
	
	public File getAndroidJarFile() {
		File check = new File(ApplicationLoader.applicationContext.getFilesDir() + "/temp/android.jar");
		
		if (check.exists()) {
			return check;
		}
		
		Decompress.unzipFromAssets(ApplicationLoader.applicationContext, "android.jar.zip", check.getParentFile().getAbsolutePath());
		
		return check;
	}
}
