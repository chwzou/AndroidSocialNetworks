package com.androidsocialnetworks.app.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.androidsocialnetworks.app.R;
import com.androidsocialnetworks.app.fragment.SocialNetworksListFragment;
import com.androidsocialnetworks.lib.SocialNetworkManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends ActionBarActivity {
    public static final File ANDROID_IMAGE = new File(Environment.getExternalStorageDirectory(), "android.jpg");
    private static final String TAG = MainActivity.class.getSimpleName();
    private SocialNetworkManager mSocialNetworkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mSocialNetworkManager = SocialNetworkManager.Builder.create()
                    .twitter("3IYEDC9Pq5SIjzENhgorlpera", "fawjHMhyzhrfcFKZVB6d5YfiWbWGmgX7vPfazi61xZY9pdD1aE")
                    .linkedIn("77ieoe71pon7wq", "pp5E8hkdY9voGC9y", "r_basicprofile+rw_nus+r_network+w_messages")
                    .build();

            getSupportFragmentManager().beginTransaction().add(mSocialNetworkManager, SocialNetworkManager.SAVE_KEY).commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, SocialNetworksListFragment.newInstance())
                    .commit();
        } else {
            mSocialNetworkManager = (SocialNetworkManager) getSupportFragmentManager().getFragment(savedInstanceState, SocialNetworkManager.SAVE_KEY);
        }

        if (!ANDROID_IMAGE.exists()) {
            copyAsset();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, SocialNetworkManager.SAVE_KEY, mSocialNetworkManager);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
    }

    public SocialNetworkManager getSocialNetworkManager() {
        return mSocialNetworkManager;
    }

    private void copyAsset() {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = getAssets().open("android.jpg");
            output = new FileOutputStream(ANDROID_IMAGE);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
