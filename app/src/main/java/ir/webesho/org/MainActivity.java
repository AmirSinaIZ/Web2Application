package ir.webesho.org;

import ir.webesho.org.SplashActivity;
import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.android.billingclient.*;
import com.anjlab.android.iab.v3.*;
import com.googlecode.d2j.*;
import ir.tapsell.sdk.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.benf.cfr.reader.*;
import org.eclipse.jdt.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {
	
	private ArrayList<HashMap<String, Object>> Lmap = new ArrayList<>();
	
	private RelativeLayout bg;
	private ViewPager viewPager;
	private LinearLayout linear_dots;
	private WormDotsIndicator worm_dots_indicator;
	private Button start_app;
	
	private ObjectAnimator animation = new ObjectAnimator();
	private Intent i = new Intent();
	private SharedPreferences showINTRO;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		bg = findViewById(R.id.bg);
		viewPager = findViewById(R.id.viewPager);
		linear_dots = findViewById(R.id.linear_dots);
		worm_dots_indicator = findViewById(R.id.worm_dots_indicator);
		start_app = findViewById(R.id.start_app);
		showINTRO = getSharedPreferences("showINTRO", Activity.MODE_PRIVATE);
		
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int _position, float _positionOffset, int _positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageSelected(int _position) {
				if (_position == 2) {
					start_app.setVisibility(View.VISIBLE);
					animation.setTarget(start_app);
					animation.setPropertyName("alpha");
					animation.setFloatValues((float)(1.0d));
					animation.setDuration((int)(500));
					animation.start();
				}
				else {
					animation.setTarget(start_app);
					animation.setPropertyName("alpha");
					animation.setFloatValues((float)(0.0d));
					animation.setDuration((int)(500));
					animation.start();
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int _scrollState) {
				
			}
		});
		
		start_app.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				showINTRO.edit().putString("show", "false").commit();
				if (start_app.getVisibility() == View.VISIBLE) {
					i.setClass(getApplicationContext(), HomeActivity.class);
					startActivity(i);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			}
		});
	}
	
	private void initializeLogic() {
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("info", "notify");
			Lmap.add(_item);
		}
		
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("info", "done");
			Lmap.add(_item);
		}
		
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("info", "apps");
			Lmap.add(_item);
		}
		
		viewPager.setAdapter(new ViewPagerAdapter(Lmap));
		((PagerAdapter)viewPager.getAdapter()).notifyDataSetChanged();
		worm_dots_indicator.attachTo(viewPager);
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF1763B3);
			SketchUi.setCornerRadius(d*14);
			start_app.setElevation(d*15);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFFFFFFF}), SketchUi, null);
			start_app.setBackground(SketchUiRD);
			start_app.setClickable(true);
		}
		start_app.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/message.ttf"), 0);
		start_app.setVisibility(View.INVISIBLE);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(0xFFFFFFFF);
	}
	
	public class ViewPagerAdapter extends PagerAdapter {
		
		Context _context;
		ArrayList<HashMap<String, Object>> _data;
		
		public ViewPagerAdapter(Context _ctx, ArrayList<HashMap<String, Object>> _arr) {
			_context = _ctx;
			_data = _arr;
		}
		
		public ViewPagerAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_context = getApplicationContext();
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public boolean isViewFromObject(View _view, Object _object) {
			return _view == _object;
		}
		
		@Override
		public void destroyItem(ViewGroup _container, int _position, Object _object) {
			_container.removeView((View) _object);
		}
		
		@Override
		public int getItemPosition(Object _object) {
			return super.getItemPosition(_object);
		}
		
		@Override
		public CharSequence getPageTitle(int pos) {
			// Use the Activity Event (onTabLayoutNewTabAdded) in order to use this method
			return "page " + String.valueOf(pos);
		}
		
		@Override
		public Object instantiateItem(ViewGroup _container,  final int _position) {
			View _view = LayoutInflater.from(_context).inflate(R.layout.intro_items, _container, false);
			
			final LinearLayout bg2 = _view.findViewById(R.id.bg2);
			final ImageView thumb = _view.findViewById(R.id.thumb);
			final TextView description = _view.findViewById(R.id.description);
			
			thumb.setImageResource(getResources().getIdentifier(_data.get((int)_position).get("info").toString(), "drawable", getPackageName()));
			if (_position == 0) {
				description.setText("برنامه وبشو نیاز به دانش برنامه نویسی ندارد و فقط یک اپلیکیشن با استفاده از وب ویو تهیه میکند");
			}
			else {
				if (_position == 1) {
					description.setText("کار با اپلیکیشن بسیار ساده بوده و تمامی قابلیت ها در دسترس است");
				}
				else {
					if (_position == 2) {
						description.setText("لینک سایتت رو وارد کن و با یک کلیک اپلیکیشنشو نصب کن !");
					}
				}
			}
			description.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/message.ttf"), 0);
			
			_container.addView(_view);
			return _view;
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