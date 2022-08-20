package ir.webesho.org.util;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import java.io.File;
import android.util.Log;

import androidx.core.content.FileProvider;


public class ApkInstaller {
    
    public static void installApk(Context context, String path) {
        
        
        try {
	                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							Uri uri = androidx.core.content.FileProvider.getUriForFile(context,
									context.getPackageName() + ".provider", new java.io.File(path));
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setDataAndType(uri, "application/vnd.android.package-archive");
							context.startActivity(intent);
		
					} else {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile( new java.io.File(path)),
									"application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
					}
	
            } catch (Exception e) {
	            
            }
        
        
        
    }
    
}
