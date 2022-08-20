package com.tyron.compiler;

import android.os.AsyncTask;
import android.content.Context;
import android.app.Dialog;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.net.Uri;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;

import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.SealedApkException;
import ir.webesho.org.SketchwareUtil;
import ir.webesho.org.FileUtil;

import ir.webesho.org.R;
import ir.webesho.org.model.*;
import ir.webesho.org.util.ApkInstaller;
import com.tyron.compiler.exception.*;

import java.lang.ref.WeakReference;
import java.io.IOException;
import java.io.File;

public class CompilerAsyncTask extends AsyncTask<Project, String, CompilerResult> {
	
	private final WeakReference<Context> mContext;
	
	private TextView progress;
	private Dialog dialog;
	private ProgressDialog dialog2;
	
	private long startTime;
	private AsyncTask sign_apk_file;
	private Project project;
	
	public CompilerAsyncTask(Context context) {
		mContext = new WeakReference<>(context);
	}
	
	@Override
	public void onPreExecute() {
		Context context = mContext.get();
		
		startTime = System.currentTimeMillis();
		dialog2 = new ProgressDialog(context);
        dialog2.setTitle("صبر کنید...");
        dialog2.setMessage("درحال ساخت اپلیکیشن");
        dialog2.setCancelable(false);
        dialog2.show();
	}
	
	@Override
	public CompilerResult doInBackground(Project... params) {
	
	    project = params[0];
	    try {
			
	        Compiler aapt2Compiler = new AAPT2Compiler(project);
			aapt2Compiler.setProgressListener(args -> {
				publishProgress(args);
			});
	        aapt2Compiler.prepare();
	        aapt2Compiler.run();
			
			Compiler ecjCompiler = new ECJCompiler(project);
			ecjCompiler.setProgressListener(args -> {
				publishProgress(args);
			});
			ecjCompiler.prepare();
			ecjCompiler.run();
			
			Compiler d8Compiler = new D8Compiler(project);
			d8Compiler.setProgressListener(args -> {
				publishProgress(args);
			});
			d8Compiler.prepare();
			d8Compiler.run();
		    
		    publishProgress("Packaging APK...");
			project.getLogger().d("APK Builder", "Packaging APK");
			
		    File binDir = new File(project.getOutputFile(), "bin");
		    File apkPath = new File(binDir, "gen.apk");
		    apkPath.createNewFile();
		    
		    File resPath = new File(binDir, "generated.apk.res");
		    
		    File dexFile = new File(binDir, "classes.dex");
		    ApkBuilder builder = new ApkBuilder(apkPath, resPath, dexFile, null, null);
			
			File[] binFiles = binDir.listFiles();
			for (File file : binFiles) {
			    if (!file.getName().equals("classes.dex") && file.getName().endsWith(".dex")) {
			        builder.addFile(file, Uri.parse(file.getAbsolutePath()).getLastPathSegment());
					project.getLogger().d("APK Builder", "Adding dex file " + file.getName() + " to APK.");
			    }
			}
			for (Library library : project.getLibraries()) {
			    builder.addResourcesFromJar(new File(library.getPath(), "classes.jar"));
				
				project.getLogger().d("APK Builder", "Adding resources of " + library.getName() + " to the APK");
			}
			builder.setDebugMode(false);
			builder.sealApk();
			
			long time = System.currentTimeMillis() - startTime;
			
			project.getLogger().d("APK Signer", "Signing Apk");
			String path_ = project.getOutputFile() + "/bin/gen.apk";
			signFile(new java.io.File(path_));
			
	    } catch (Exception e) {
		      return new CompilerResult(e.getMessage(), true);
		}
	    return new CompilerResult("Success", false);
	}
	
	@Override
	public void onProgressUpdate(String... update) {
	 //   progress.setText(update[0]);
	}
	
	@Override
	public void onPostExecute(CompilerResult result) {
		
		if (result.isError()) {
            dialog2.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get());
			builder.setTitle("خطا در ساخت برنامه");
			builder.setMessage(result.getMessage());
			builder.setPositiveButton("بستن", null);
			builder.create().show();
		}
	}

    private void signFile(final File file){
        class sign_apk_file extends AsyncTask<Void, Void, Void> { 
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String outFile =file.getAbsolutePath();
                String out = project.getOutputFile() + "/bin/generated.apk";
                apksigner.Main.sign(file,out);
                               
            } catch (Exception e) {
                long time = System.currentTimeMillis() - startTime;
                project.getLogger().d("APK Signer", "Sign error");
            }
        return null;
         }

        protected void onPreExecute() {

        return ;
        }

        protected void onPostExecute(Void result) {
            long time = System.currentTimeMillis() - startTime;
            dialog2.dismiss();
            Context context = mContext.get();
            ApkInstaller.installApk(context, project.getOutputFile() + "/bin/generated.apk");
            project.getLogger().d("APK Builder", "Build success, took " + time + "ms");
            try {
                FileUtil.deleteFile(project.getOutputFile() + "/bin/gen.apk");
            } catch (Exception e) {
	 
            }
        return ;
        }
        }
        new sign_apk_file().execute();
    }
}
