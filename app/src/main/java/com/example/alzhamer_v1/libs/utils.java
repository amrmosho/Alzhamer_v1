package com.example.alzhamer_v1.libs;

import android.app.AlertDialog;
import android.content.Context;

public class utils {


    public  static void sendalert (Context c, String title, String message , String button){

        new AlertDialog.Builder(c).setTitle(title).setMessage(message).setNeutralButton(button, null).show();

    }
    public  static void sendalert (Context c, String title, String message){

        new AlertDialog.Builder(c).setTitle(title).setMessage(message).setNeutralButton("Close", null).show();

    }




}
