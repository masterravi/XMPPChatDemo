package com.demo.xmppchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.demo.xmppchat.R;

//Activity for Splash screen
public class SplashActivity extends Activity {

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 6000; // 6 seconds
	private Handler myhandler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		myhandler = new Handler();

		// run a thread to start the home screen
		myhandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				finish();

				if (!mIsBackButtonPressed) {
					// start the home activity
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
				}

			}

		}, SPLASH_DURATION);
	}

	// handle back button press
	@Override
	public void onBackPressed() {
		mIsBackButtonPressed = true;
		super.onBackPressed();
	}

}