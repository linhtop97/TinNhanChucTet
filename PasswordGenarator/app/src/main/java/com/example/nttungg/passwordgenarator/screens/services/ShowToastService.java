package com.example.nttungg.passwordgenarator.screens.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by nttungg on 6/25/18.
 */

public class ShowToastService extends IntentService {

    public ShowToastService() {
        super("ShowToastService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();
        Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show();
    }
}
