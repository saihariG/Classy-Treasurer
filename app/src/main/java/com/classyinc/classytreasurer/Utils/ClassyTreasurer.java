package com.classyinc.classytreasurer.Utils;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class ClassyTreasurer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
