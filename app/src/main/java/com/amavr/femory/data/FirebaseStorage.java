package com.amavr.femory.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.amavr.femory.BuildConfig;
import com.amavr.femory.models.GroupInfo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseStorage implements IRemoteStorage, ValueEventListener {

    private static final String TAG = "XDBG.FirebaseStorage";

    IRemoteAnswerListener consumer;

    public FirebaseStorage(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(BuildConfig.goo_app_id) // Required for Analytics.
                .setApiKey(BuildConfig.api_key) // Required for Auth.
                .setDatabaseUrl(BuildConfig.db_url) // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(context, options, "LISTS");
        Log.d(TAG, "Firebase initialized");
    }

    private DatabaseReference createRef(String path, ValueEventListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(path);
        ref.addValueEventListener(listener);
        return ref;
    }

    public GroupInfo queryListByKey(String key, ValueEventListener listener) {
        GroupInfo gi = new GroupInfo();
        gi.key = key;
        gi.ref = createRef("lists/" + key, listener);
        return gi;
    }

    @Override
    public void queryGroups(List<GroupInfo> sour_groups, IRemoteAnswerListener listener) {
        this.consumer = listener;
        Log.d(TAG, "getGroups");
        for (GroupInfo grp : sour_groups) {
            queryListByKey(grp.key, this);
        }
    }

    @Override
    public GroupInfo addGroup(String key, String name){
        GroupInfo gi = new GroupInfo();
        gi.key = key;
        gi.name = name;
        DatabaseReference ref = createRef("lists/" + key, this);
        ref.setValue(gi);
        gi.ref = ref;

        return gi;
    }

    @Override
    public void delGroup(GroupInfo group) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String key = snapshot.getKey();
        GroupInfo gi = snapshot.getValue(GroupInfo.class);
        this.consumer.onAnswer(key, gi);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        this.consumer.onError(error.toException());
    }
}

