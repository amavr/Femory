package com.amavr.femory;

import android.app.Application;
import android.util.Log;

import com.amavr.femory.data.FirebaseStorage;
import com.amavr.femory.data.LocalStorage;
import com.amavr.femory.data.Repository;
import com.amavr.femory.ext.Tools;
import com.amavr.femory.models.GroupInfo;

public class App extends Application {

    static final String TAG = "XDBG.App";

    public Repository repository;
    public LocalStorage localStorage;
    public FirebaseStorage firebaseStorage;

    private static App instance;
    public static App get() { return instance; }

    public String app_id;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        instance = this;

        initLocalStorage();
        initFirebaseStorage();
        initRepository();
    }

    private void initFirebaseStorage() {
        this.firebaseStorage = new FirebaseStorage(this.getApplicationContext());
    }

    private void initLocalStorage() {
        localStorage = new LocalStorage(this.getApplicationContext());
        app_id = localStorage.getAppId();
//        String key = makeGroupKey();
//        GroupInfo gi = new GroupInfo();
//        gi.key = key;
//        gi.name = String.format("group #%s", localStorage.getGroups().size());
//        localStorage.updateGroup(key, gi);
    }

    private void initRepository() {
        this.repository = new Repository(this.localStorage, this.firebaseStorage);
    }

    String makeGroupKey(){
        return app_id + ":" + Tools.generateKey();
    }
}
