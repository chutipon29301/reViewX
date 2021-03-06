package com.chutipon.reviewx.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.chutipon.reviewx.R;
import com.chutipon.reviewx.manager.CheckExistUserManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements CheckExistUserManager.onLoad {
    private CallbackManager callbackManager;
    private static final String TAG = "MainActivity";
    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isLoggedIn()) {
            redirect();
        }

//          For getting app keyhash
//        getKeyhash();

        instance = this;
        redirect();
        initInstance(savedInstanceState);
    }

    private void initInstance(Bundle savedInstanceState) {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                redirect();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: ");
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckExistResult(boolean exist) {
        if (exist) {
            MainActivity.getInstance().redirectToPage(HomeActivity.class);
        } else {
            MainActivity.getInstance().redirectToPage(PreferenceActivity.class);
        }
    }

    private void redirect() {
        if (isLoggedIn()) {
            Log.d(TAG, "redirect: Access token " + AccessToken.getCurrentAccessToken().getUserId());
            CheckExistUserManager.getInstance().startCheckExistUser(this);
        }
    }

    public void redirectToPage(Class cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void getKeyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.chutipon.reviewx",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

}
