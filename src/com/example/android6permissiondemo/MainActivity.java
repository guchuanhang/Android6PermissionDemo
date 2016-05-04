package com.example.android6permissiondemo;

import java.io.File;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	static final String tag = MainActivity.class.getSimpleName();
	public static final int REQUEST_TAKE_PHOTO = 0x12;
	public static final int REQUEST_PERMISSION_READ_EXTERNAL = 0x18;
	private ImageView imageView;
	private File mPhotoFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.iv);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// take photo create a external storage file
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					mPhotoFile = new File(Environment
							.getExternalStorageDirectory(), getResources()
							.getString(R.string.app_name));
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(mPhotoFile));
					startActivityForResult(takePictureIntent,
							REQUEST_TAKE_PHOTO);
				}
			}
		});

	}

	/**
	 * access external storage get and show photo
	 */
	public void getAndShowPhoto() {
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageBitmap(BitmapFactory.decodeFile(mPhotoFile
				.getAbsolutePath()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_TAKE_PHOTO: {
			// photo is saved to external storage
			if (resultCode == RESULT_OK) {
				applyAccessPermission();
			}

		}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * info user why you want to access that permission （if you feel
				it's necessary）
	 */
	protected void explainApplyPermissionReason() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("Info");
		builder.setMessage("for show external stoage image,please allow me access external storage later !");
		builder.setPositiveButton("I Know", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				applyPermission();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				applyPermission();
			}
		});
		builder.setCancelable(true);
		builder.create().show();
	}

	@TargetApi(23)
	public void applyPermission() {
		ActivityCompat.requestPermissions(MainActivity.this,
				new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
				REQUEST_PERMISSION_READ_EXTERNAL);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void applyAccessPermission() {
		// check if already got permission
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// if you think it's necessary explain something to user about
			// permission,you can override
			// shouldShowRequestPermissionRationale
			if (ActivityCompat.shouldShowRequestPermissionRationale(
					MainActivity.this,
					Manifest.permission.READ_EXTERNAL_STORAGE)) {
			} else {
				applyPermission();
			}
		} else {
			getAndShowPhoto();
		}

	}

	@TargetApi(23)
	@Override
	public boolean shouldShowRequestPermissionRationale(String permission) {

		switch (permission) {
		case Manifest.permission.READ_EXTERNAL_STORAGE: {
			explainApplyPermissionReason();
		}
			return true;

		default:
			break;
		}

		return super.shouldShowRequestPermissionRationale(permission);
	}

	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		switch (requestCode) {
		case REQUEST_PERMISSION_READ_EXTERNAL: {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				getAndShowPhoto();
			} else {
				Toast.makeText(MainActivity.this,
						"I sorry,lack permission i cann't show image there~",
						Toast.LENGTH_LONG).show();
			}

		}

			break;

		default:
			break;
		}

		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

}
