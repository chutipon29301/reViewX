package com.chutipon.reviewx.manager;

import android.util.Log;

import com.chutipon.reviewx.activity.HomeActivity;
import com.chutipon.reviewx.activity.MainActivity;
import com.chutipon.reviewx.activity.PreferenceActivity;
import com.chutipon.reviewx.dao.CheckExistUserDao;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 14/12/2017 AD.
 */

public class CheckExistUserManager {
    private static CheckExistUserManager instance;
    private static String TAG = "CheckExistUserManager";

    private CheckExistUserManager() {
    }

    public static CheckExistUserManager getInstance() {
        if (instance == null) {
            instance = new CheckExistUserManager();
        }
        return instance;
    }

    public void startCheckExistUser(String facebookID) {
        HttpManager.getInstance().getApiService().checkExistUser(facebookID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckExistUserDao>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(CheckExistUserDao value) {
                        Log.d(TAG, "onNext: " + value.isExist());
                        if (value.isExist()) {
                            MainActivity.getInstance().redirectToPage(HomeActivity.class);
                        } else {
                            MainActivity.getInstance().redirectToPage(PreferenceActivity.class);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
