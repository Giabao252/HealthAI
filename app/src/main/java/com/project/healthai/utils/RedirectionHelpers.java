package com.project.healthai.utils;

import android.content.Context;
import android.content.Intent;
public class RedirectionHelpers {
    public void redirect(Context current, Class main) {
        Intent newActivity = new Intent(current, main);
        current.startActivity(newActivity);
    }
}
