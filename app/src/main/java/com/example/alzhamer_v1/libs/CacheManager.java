package com.example.alzhamer_v1.libs;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by empcl on 01 - 08 - 2017.
 */

public class CacheManager {

    public static ProgressBar loader;

    private static final long MAX_SIZE = 5242880L; // 5MB


    private static void show_loader() {
        if (loader != null) {
            loader.setVisibility(View.VISIBLE);
        }
    }

    private static void remove_loader() {
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }
    }


    /**
     * Write bitmap associated with a url to disk cache
     */


    public static void cacheimages(Context context, String name, Bitmap avatar) {
        String[] namedata = name.split("/");
        name = namedata[namedata.length - 1];

        File cacheDir = context.getCacheDir();
        File cacheFile = new File(cacheDir, "" + name);
        try {
            cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            avatar.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("xxxx", e.getMessage());
        }
    }


    public static File get_file(Context context, String name) {

        String[] namedata = name.split("/");
        name = namedata[namedata.length - 1];

        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, name);

        if (!file.exists()) {
            return null;
        }
        return file;
    }


    public static String getfile_path(Context context, String name) {

        try {


            File cacheDir = context.getCacheDir();
            File file = new File(cacheDir, name);

            if (!file.exists()) {
                return null;
            }
            return file.getAbsolutePath();
        } catch (NullPointerException e) {
            Log.e("xxx", e.getMessage());
            return null;


        }

    }


//Core Start


    private static void cleanDir(File dir, long bytes) {
        long bytesDeleted = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            bytesDeleted += file.length();
            file.delete();

            if (bytesDeleted >= bytes) {
                break;
            }
        }
    }


    private static long getDirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }
        }

        return size;
    }


    public static void clearData(Context context) {
        show_loader();
        File cacheDir = context.getCacheDir();

        File[] files = cacheDir.listFiles();

        for (File file : files) {
            file.delete();

        }
        remove_loader();


    }


    public static void cacheData(Context context, String sdata, String name) {

        File cacheDir = context.getCacheDir();
        try {
            long size = getDirSize(cacheDir);


            byte[] data = sdata.getBytes("UTF-8");


            long newSize = data.length + size;

            if (newSize > MAX_SIZE) {
                cleanDir(cacheDir, newSize - MAX_SIZE);
            }



            File file = new File(cacheDir, name + ".txt");
            FileOutputStream os = new FileOutputStream(file);

            try {
                os.write(data);
            } finally {
                os.flush();
                os.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    public static String getDataString(Context context, String name) {

        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, name + ".txt");

        if (!file.exists()) {
            // Data doesn't exist
            return null;
        }



        byte[] data = new byte[(int) file.length()];
        try {
            FileInputStream is = new FileInputStream(file);
            try {
                is.read(data);
            } finally {
                is.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return new String(data);


    }


    public static void cacheData(Context context, ArrayList<HashMap<String, String>> sdata, String name) {

        cacheData(context, Json.MapTojson(sdata), name);
    }

    public static ArrayList<HashMap<String, String>> getData(Context context, String name) {
        return Json.jsonToMap(getDataString(context, name));
    }

    public static String _insert(Context context, String name, HashMap<String, String> data) {




        String ID = UUID.randomUUID().toString();
        data.put("id", ID);
        ArrayList<HashMap<String, String>> getmyData = getData(context, name);
        getmyData.add(data);

        cacheData(context, getmyData, name);




        return ID;
    }

    public static boolean _delete(Context context, String name, String id) {
        boolean r = false;
        ArrayList<HashMap<String, String>> getmyData = getData(context, name);


        ArrayList<HashMap<String, String>> newmyData = new ArrayList<>();


        for (HashMap<String, String> h : getmyData) {
            if (!h.get("id").equals(id)) {
                newmyData.add(h);
                r = true;
            }
        }



        cacheData(context, newmyData, name);

        return r;
    }

    public static boolean _update(Context context, String name, String id, HashMap<String, String> newh) {
        boolean r = false;
        ArrayList<HashMap<String, String>> getmyData = getData(context, name);
        ArrayList<HashMap<String, String>> newmyData = new ArrayList<>();
        for (HashMap<String, String> h : getmyData) {
            if (h.get("id").equals(id)) {
                newh.put("id", id);
                newmyData.add(newh);
                r = true;
            } else {
                newmyData.add(h);
            }
        }
        cacheData(context, newmyData, name);

        return r;
    }

//Core End


}

