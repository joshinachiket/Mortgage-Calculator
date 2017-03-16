package com.joshi.nachiket.mortgage_calculator;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by NACHIKET on 3/14/2017.
 */

public class Message {

    public static void message (Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
