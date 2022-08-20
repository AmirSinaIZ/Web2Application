package ir.webesho.org;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.billingclient.*;
import com.anjlab.android.iab.v3.*;
import com.google.android.material.appbar.AppBarLayout;
import com.googlecode.d2j.*;
import de.hdodenhof.circleimageview.*;
import ir.tapsell.sdk.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.benf.cfr.reader.*;
import org.eclipse.jdt.*;
import org.json.*;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import ir.webesho.org.util.Decompress;
import ir.tapsell.sdk.bannerads.*;
import ir.tapsell.sdk.Tapsell;
import android.content.Context;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.net.URISyntaxException;
import java.net.URI;
import java.io.File;
import ir.webesho.org.model.Project;
import ir.webesho.org.model.Library;
import ir.webesho.org.FileUtil;
import com.tyron.compiler.CompilerAsyncTask;
import androidx.appcompat.app.AppCompatDelegate;
import ir.webesho.org.logger.Logger;

public class HomeActivity extends AppCompatActivity {
	
	public final int REQ_CD_ICONPICKER = 101;
	
	private Logger mLogger;
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private DrawerLayout _drawer;
	private String fontName = "";
	private String typeace = "";
	private String packageName = "";
	private boolean haveError = false;
	private String javaPath = "";
	private String assetsPath = "";
	private String resPath = "";
	private String iconPATH = "";
	private String libsPath = "";
	private String part1 = "";
	private String part2 = "";
	private boolean isVipUser = false;
	BillingProcessor bp;
	private String productID = "";
	private boolean ready2Purchase = false;
	private String LOG_TAG = "";
	
	private ScrollView vScroll;
	private TapsellBannerView myAd;
	private LinearLayout linear_main;
	private LinearLayout informationUI;
	private TextView TextView;
	private CircleImageView app_icon;
	private TextView HelpText;
	private LinearLayout linear_name;
	private LinearLayout PackageName;
	private TextView helperTEXT;
	private LinearLayout webLink;
	private Button makeApplication;
	private EditText editName;
	private EditText mainPKG;
	private EditText link;
	private RecyclerView _drawer_RecyclerView;
	
	private AlertDialog dialog;
	private Intent ICONpicker = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog dialog2;
	private RequestNetwork net;
	private RequestNetwork.RequestListener _net_request_listener;
	private Intent reload = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		_drawer = findViewById(R.id._drawer);
		ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(HomeActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = findViewById(R.id._nav_view);
		
		vScroll = findViewById(R.id.vScroll);
		myAd = findViewById(R.id.myAd);
		linear_main = findViewById(R.id.linear_main);
		informationUI = findViewById(R.id.informationUI);
		TextView = findViewById(R.id.TextView);
		app_icon = findViewById(R.id.app_icon);
		HelpText = findViewById(R.id.HelpText);
		linear_name = findViewById(R.id.linear_name);
		PackageName = findViewById(R.id.PackageName);
		helperTEXT = findViewById(R.id.helperTEXT);
		webLink = findViewById(R.id.webLink);
		makeApplication = findViewById(R.id.makeApplication);
		editName = findViewById(R.id.editName);
		mainPKG = findViewById(R.id.mainPKG);
		link = findViewById(R.id.link);
		_drawer_RecyclerView = _nav_view.findViewById(R.id.RecyclerView);
		ICONpicker.setType("image/*");
		ICONpicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		net = new RequestNetwork(this);
		
		app_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent ICONpicker = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
				startActivityForResult(ICONpicker, REQ_CD_ICONPICKER);
			}
		});
		
		makeApplication.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (haveError) {
					SketchwareUtil.showMessage(getApplicationContext(), "اطلاعات به درستی وارد نشده !");
				}
				else {
					_showLoading(true);
					//background executor create here
					java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
					
					final Handler handler = new Handler(Looper.getMainLooper());
					
					  executor.execute(new Runnable() {
							
							@Override
							
							public void run() {FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/AndroidManifest.xml"), "\n<manifest\n	xmlns:android=\"http://schemas.android.com/apk/res/android\"\n	package=\"".concat(mainPKG.getText().toString().concat("\">".concat("\n	<uses-permission android:name=\"android.permission.INTERNET\" />\n	<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\" />\n	<application\n		android:allowBackup=\"true\"\n		android:icon=\"@drawable/app_icon\"\n		android:label=\"@string/app_name\"\n		android:usesCleartextTraffic=\"true\"\n		android:theme=\"@style/AppTheme\">\n		<activity\n			android:name=\".MainActivity\"\n			android:configChanges=\"orientation|screenSize|keyboardHidden|smallestScreenSize|screenLayout\"\n			android:hardwareAccelerated=\"true\"\n			android:supportsPictureInPicture=\"true\"\n			android:theme=\"@style/FullScreen\"\n			android:screenOrientation=\"portrait\">\n			<intent-filter>\n				<action android:name=\"android.intent.action.MAIN\" />\n				<category android:name=\"android.intent.category.LAUNCHER\" />\n			</intent-filter>\n		</activity>\n		<uses-library\n			android:name=\"org.apache.http.legacy\"\n			android:required=\"false\" />\n	</application>\n</manifest>"))));
							FileUtil.writeFile(javaPath.concat("/MainActivity.java"), "package ".concat(mainPKG.getText().toString().concat(";\n\n".concat(part1.concat(link.getText().toString().concat("\";\n\n".concat(part2)))))));
							FileUtil.writeFile(resPath.concat("/values/strings.xml"), "<resources>\n	<string name=\"app_name\" translatable=\"false\">".concat(editName.getText().toString().concat("</string>\n</resources>\n")));
							handler.post(new Runnable() {
											
											@Override
											
											public void run() {_showLoading(false);
									SystemLogPrinter.start(mLogger);
									Project project = new Project();
									project.setLibraries(Library.fromFile(new File(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/lib/local_libs"))));
									
									project.setResourcesFile(new File(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/res")));
									
									project.setOutputFile(new File(FileUtil.getPackageDataDir(getApplicationContext()).concat("/build")));
									
									project.setJavaFile(new File(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/java")));
									
									project.setManifestFile(new File(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/AndroidManifest.xml")));
									project.setLogger(mLogger);
									
									project.setVersionName(new String("1.0"));
									
									project.setMinSdk(21);
									project.setVersionCode(1);
									project.setTargetSdk(28);
									CompilerAsyncTask task = new CompilerAsyncTask(HomeActivity.this);
									
									task.execute(project);
													
													//UI Thread work here
																	
											} });
									//background task start here
									
									
									
							} });
				}
			}
		});
		
		editName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (!_charSeq.equals("")) {
					haveError = false;
				}
				else {
					haveError = true;
					((EditText)editName).setError("اسم نمیتواند خالی باشد");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		link.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (!_charSeq.equals("")) {
					haveError = false;
				}
				else {
					haveError = true;
					((EditText)link).setError("لینک سایت نمیتواند خالی باشد");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		_showLoading(true);
		//background executor create here
		java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
		
		final Handler handler = new Handler(Looper.getMainLooper());
		
		  executor.execute(new Runnable() {
				
				@Override
				
				public void run() {if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source"))) {
					FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source"));
				}
				if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/build"))) {
					FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/build"));
				}
				FileUtil.makeDir(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source"));
				FileUtil.makeDir(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/java"));
				FileUtil.makeDir(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/lib"));
				FileUtil.makeDir(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/assets"));
				FileUtil.makeDir(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/res"));
				FileUtil.makeDir(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/res/drawable-xhdpi"));
				Decompress.unzipFromAssets(HomeActivity.this, "Network.zip", FileUtil.getPackageDataDir(getApplicationContext()) + "/Source/lib/");
				if (isVipUser) {
					Decompress.unzipFromAssets(HomeActivity.this, "vipRes.zip", FileUtil.getPackageDataDir(getApplicationContext()) + "/Source/res/");
				}
				else {
					Decompress.unzipFromAssets(HomeActivity.this, "freeRes.zip", FileUtil.getPackageDataDir(getApplicationContext()) + "/Source/res/");
				}
				{
					try{
						int count;
						java.io.InputStream input= HomeActivity.this.getAssets().open("default_icon.png");
						java.io.OutputStream output = new  java.io.FileOutputStream(FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/res/drawable-xhdpi/") + "app_icon.png");
						byte data[] = new byte[1024];
						while ((count = input.read(data))>0) {
							output.write(data, 0, count);
						}
						output.flush();
						output.close();
						input.close();
						
						}catch(Exception e){
								
						}
				}
				handler.post(new Runnable() {
								
								@Override
								
								public void run() {_showLoading(false);
										
										//UI Thread work here
														
								} });
						//background task start here
						
						
						
				} });
		if (isVipUser) {
			mainPKG.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
									final String _charSeq = _param1.toString();
					                _verifyPackageName(mainPKG);
					                
									
							}
							
							@Override
							public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
									
							}
							
							@Override
							public void afterTextChanged(Editable _param1) {
									
					
					                
					        }   
						   
			});
		}
		else {
			mainPKG.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
									final String _charSeq = _param1.toString();
					                _verifyPackageName(mainPKG);
					                
									
							}
							
							@Override
							public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
									
							}
							
							@Override
							public void afterTextChanged(Editable _param1) {
									
					                if (!mainPKG.getText().toString().startsWith("my.webesho.")) {
							                 mainPKG.setText("my.webesho.");
						                     _membershipDialog();
						                  }
					                
					        }   
						   
			});
		}
		javaPath = FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/java");
		assetsPath = FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/assets");
		resPath = FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/res");
		libsPath = FileUtil.getPackageDataDir(getApplicationContext()).concat("/Source/lib");
		part1 = "import android.animation.*;\nimport android.app.*;\nimport android.app.Activity;\nimport android.app.DialogFragment;\nimport android.app.Fragment;\nimport android.app.FragmentManager;\nimport android.content.*;\nimport android.content.Intent;\nimport android.content.res.*;\nimport android.graphics.*;\nimport android.graphics.drawable.*;\nimport android.media.*;\nimport android.net.*;\nimport android.net.Uri;\nimport android.os.*;\nimport android.text.*;\nimport android.text.style.*;\nimport android.util.*;\nimport android.view.*;\nimport android.view.View.*;\nimport android.view.animation.*;\nimport android.webkit.*;\nimport android.webkit.WebSettings;\nimport android.webkit.WebView;\nimport android.webkit.WebViewClient;\nimport android.widget.*;\nimport android.widget.LinearLayout;\nimport android.widget.ProgressBar;\nimport com.haozhang.lib.*;\nimport java.io.*;\nimport java.text.*;\nimport java.util.*;\nimport java.util.regex.*;\nimport org.json.*;\n\npublic class MainActivity extends Activity {\n	\n	private String URL = \"";
		part2 = "	private RelativeLayout linear1;\n	private LinearLayout linear3;\n	private WebView webview1;\n	private LinearLayout linear2;\n	private SlantedTextView SlantedTextView;\n	private ProgressBar progressbar1;\n	\n	private Intent i = new Intent();\n	\n	@Override\n	protected void onCreate(Bundle _savedInstanceState) {\n		super.onCreate(_savedInstanceState);\n		setContentView(R.layout.main);\n		initialize(_savedInstanceState);\n		initializeLogic();\n	}\n	\n	private void initialize(Bundle _savedInstanceState) {\n		linear1 = findViewById(R.id.linear1);\n		linear3 = findViewById(R.id.linear3);\n		webview1 = findViewById(R.id.webview1);\n		webview1.getSettings().setJavaScriptEnabled(true);\n		webview1.getSettings().setSupportZoom(true);\n		linear2 = findViewById(R.id.linear2);\n		SlantedTextView = findViewById(R.id.SlantedTextView);\n		progressbar1 = findViewById(R.id.progressbar1);\n		\n		webview1.setWebViewClient(new WebViewClient() {\n			@Override\n			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {\n				final String _url = _param2;\n				linear1.setVisibility(View.VISIBLE);\n				super.onPageStarted(_param1, _param2, _param3);\n			}\n			\n			@Override\n			public void onPageFinished(WebView _param1, String _param2) {\n				final String _url = _param2;\n				\n				super.onPageFinished(_param1, _param2);\n			}\n		});\n	}\n	\n	private void initializeLogic() {\n		webview1.loadUrl(URL);\n		linear1.setVisibility(View.GONE);\n		if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {\n					    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS\n					            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);\n		}\n		if (Build.VERSION.SDK_INT >= 19) {\n					    getWindow().getDecorView().setSystemUiVisibility(\n					            View.SYSTEM_UI_FLAG_LAYOUT_STABLE\n					                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN\n					                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION\n					    );\n		}\n		if (Build.VERSION.SDK_INT >= 21) {\n					    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS\n					            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);\n					    getWindow().setStatusBarColor(Color.TRANSPARENT);\n					    getWindow().setNavigationBarColor(Color.TRANSPARENT);\n		}\n	}\n\n	private void setWindowFlag(final int bits, boolean on) {\n		    Window win = getWindow();\n		    WindowManager.LayoutParams winParams = win.getAttributes();\n		    if (on) {\n					        winParams.flags |= bits;\n					    } else {\n					        winParams.flags &= ~bits;\n					    }\n		    win.setAttributes(winParams);\n	}\n	{\n	}\n	\n}";
		if (!isVipUser) {
				TapsellBannerView banner = findViewById(R.id.myAd);
				banner.loadAd(getApplicationContext()
				,"62ffed708f178c2d6268ded0",TapsellBannerType.BANNER_320x50);
						
				banner.setEventListener(new TapsellBannerViewEventListener() {
							@Override
							public void onError(String message) {
														
										  
										
							}
						
						@Override
							public void onHideBannerView() {
										
										  
										
							}
						
						@Override
							public void onNoAdAvailable() {
										
										  
										
							}
						
						@Override
							public void onNoNetwork() {
										
										  
										
							}
							@Override
							public void onRequestFilled() {
										
									  
										
							}
						
							
				});
		}
		mLogger = new Logger();
		mLogger.attach(_drawer_RecyclerView);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		if (!bp.handleActivityResult(_requestCode, _resultCode, _data));
		switch (_requestCode) {
			case REQ_CD_ICONPICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				iconPATH = _filePath.get((int)(0));
				_showLoading(true);
				//background executor create here
				java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
				
				final Handler handler = new Handler(Looper.getMainLooper());
				
				  executor.execute(new Runnable() {
						
						@Override
						
						public void run() {FileUtil.copyFile(_filePath.get((int)(0)), resPath.concat("/drawable-xhdpi/app_icon.png"));
						handler.post(new Runnable() {
										
										@Override
										
										public void run() {_showLoading(false);
								app_icon.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_filePath.get((int)(0)), 1024, 1024));
												
												//UI Thread work here
																
										} });
								//background task start here
								
								
								
						} });
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setTitle("WebeSho");
		_changeActivityFont("message");
		haveError = true;
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFFFFFFFF);
			SketchUi.setCornerRadius(d*21);
			informationUI.setElevation(d*17);
			informationUI.setBackground(SketchUi);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFFFFFFFF);
			SketchUi.setCornerRadius(d*11);
			SketchUi.setStroke(d*3,0xFF1763B3);
			linear_name.setElevation(d*3);
			linear_name.setBackground(SketchUi);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFFFFFFFF);
			SketchUi.setCornerRadius(d*11);
			SketchUi.setStroke(d*3,0xFF1763B3);
			PackageName.setElevation(d*3);
			PackageName.setBackground(SketchUi);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFFFFFFFF);
			SketchUi.setCornerRadius(d*11);
			SketchUi.setStroke(d*3,0xFF1763B3);
			webLink.setElevation(d*3);
			webLink.setBackground(SketchUi);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF1763B3);
			SketchUi.setCornerRadius(d*360);
			makeApplication.setElevation(d*9);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFFFFFFF}), SketchUi, null);
			makeApplication.setBackground(SketchUiRD);
			makeApplication.setClickable(true);
		}
		productID = "vipMembership";
		if(!BillingProcessor.isIabServiceAvailable(this)) {
			
			ready2Purchase = false;
			
		}
		bp = new BillingProcessor(this, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCT1XEAEHN9gvBN+I8nm87MnAOTkZA+88m7Mhxe6eLelUmBAzPmHrlSAdfvoIUOKnlKxnsOCQeAajpzV080qU4nEYF/QbIfPcHfIiX5qeOtZjqbgHGRC8PmvcTUiCGSv+we9cSgNEOZQ69w2T7gkC8hhV8Y2I4fs8K+6cR2e7tcQIDAQAB", "4a4f38bd-6215-4bc5-9635-5f9e0e6c6480", new BillingProcessor.IBillingHandler() {
			
			@Override
			public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
				
				SketchwareUtil.showMessage(getApplicationContext(), "با موفقیت پرداخت شد");
				
			}
			@Override
			public void onBillingError(int errorCode, @Nullable Throwable error) {
				
				final String MessageError = Integer.toString(errorCode);
				
				SketchwareUtil.showMessage(getApplicationContext(), "خطا در پرداخت");
				
			}
			@Override
			public void onBillingInitialized() {
				
				ready2Purchase = true;
				
			}
			@Override
			public void onPurchaseHistoryRestored() {
				for(String sku : bp.listOwnedProducts())
				Log.d(LOG_TAG, "Owned Managed Product: " + sku);
				for(String sku : bp.listOwnedSubscriptions())
				Log.d(LOG_TAG, "Owned Subscription: " + sku);
				 
			}
			
		});
		if (bp.isPurchased(productID)) {
			isVipUser = true;
		}
		else {
			isVipUser = false;
		}
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (bp != null)
		bp.release();
	}
	
	@Override
	public void onBackPressed() {
		if (_drawer.isDrawerOpen(GravityCompat.START)) {
			_drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	public void _changeActivityFont(final String _fontname) {
		fontName = "fonts/".concat(_fontname.concat(".ttf"));
		overrideFonts(this,getWindow().getDecorView()); 
	} 
	private void overrideFonts(final android.content.Context context, final View v) {
		
		try {
			Typeface 
			typeace = Typeface.createFromAsset(getAssets(), fontName);;
			if ((v instanceof ViewGroup)) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0;
				i < vg.getChildCount();
				i++) {
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			}
			else {
				if ((v instanceof TextView)) {
					((TextView) v).setTypeface(typeace);
				}
				else {
					if ((v instanceof EditText )) {
						((EditText) v).setTypeface(typeace);
					}
					else {
						if ((v instanceof Button)) {
							((Button) v).setTypeface(typeace);
						}
					}
				}
			}
		}
		catch(Exception e)
		
		{
			SketchwareUtil.showMessage(getApplicationContext(), "خطا در بارگذاری فونت");
		};
	}
	
	
	public void _membershipDialog() {
		dialog = new AlertDialog.Builder(HomeActivity.this).create();
		LayoutInflater dialogLI = getLayoutInflater();
		View dialogCV = (View) dialogLI.inflate(R.layout.membership_dialog, null);
		dialog.setView(dialogCV);
		final Button buy = (Button)
		dialogCV.findViewById(R.id.buy);
		final TextView cancel = (TextView)
		dialogCV.findViewById(R.id.cancel);
		final TextView information = (TextView)
		dialogCV.findViewById(R.id.textview1);
		final LinearLayout mAin = (LinearLayout)
		dialogCV.findViewById(R.id.mAin);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		buy.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/title.ttf"), 0);
		cancel.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/message.ttf"), 0);
		information.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/title.ttf"), 0);
		mAin.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)25, 0xFFFFFFFF));
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF1763B3);
			SketchUi.setCornerRadius(d*360);
			buy.setElevation(d*6);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFFFFFFF}), SketchUi, null);
			buy.setBackground(SketchUiRD);
			buy.setClickable(true);
		}
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dialog.dismiss();
			}
		});
		buy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dialog.dismiss();
				if (!ready2Purchase) {
					SketchwareUtil.showMessage(getApplicationContext(), "لطفاً بعدا دوباره تلاش کنید");
					return;
				}
				bp.purchase(HomeActivity.this, productID);
			}
		});
		dialog.show();
		SketchwareUtil.hideKeyboard(getApplicationContext());
	}
	
	
	public void _verifyPackageName(final View _Editable) {
		packageName = mainPKG.getText().toString();
		String[] packages = packageName.split("\\.");
		
		        if (packages == null) {
			            mainPKG.setError("نمیتوان خالی باشد");
			        } else if (packages.length == 1) {
			            mainPKG.setError("خیلی کوتاه است");
			        } else if (packageName.endsWith(".")) {
			            mainPKG.setError("نا معتبر است");
			        } else if (packageName.contains(" ")) {
			            mainPKG.setError("نا معتبر است");
			        } else if (!packageName.matches("^[a-zA-Z0-9.]+$")) {
			            mainPKG.setError("نا معتبر است");
			        } else if (packageName.equals("my.webesho.")) {
			            mainPKG.setError("نا معتبر است");
			        } else {
			            haveError = true;
			        }
	}
	
	
	public void _showLoading(final boolean _show) {
		if (_show) {
			dialog2 = new AlertDialog.Builder(HomeActivity.this).create();
			LayoutInflater dialog2LI = getLayoutInflater();
			View dialog2CV = (View) dialog2LI.inflate(R.layout.loading, null);
			dialog2.setView(dialog2CV);
			dialog2.setCancelable(false);
			dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog2.show();
		}
		else {
			dialog2.dismiss();
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}