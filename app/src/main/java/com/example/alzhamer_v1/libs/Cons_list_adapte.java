
package com.example.alzhamer_v1.libs;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alzhamer_v1.AddContectActivity;
import com.example.alzhamer_v1.ContactActivity;
import com.example.alzhamer_v1.MemsActivity;
import com.example.alzhamer_v1.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class Cons_list_adapte extends ArrayAdapter<HashMap<String, String>> {

    Context context;
    ArrayList<HashMap<String, String>> objects;
    ArrayList<HashMap<String, String>> fobjects;

    public Cons_list_adapte(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);

        this.objects = new ArrayList<HashMap<String, String>>();
        this.fobjects = new ArrayList<HashMap<String, String>>();
        this.objects.addAll(objects);
        this.fobjects.addAll(objects);
        this.context = context;

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.contact_item_layout, null);
        final HashMap<String, String> cat = objects.get(position);

        TextView t =  view.findViewById(R.id.title);
        t.setText(cat.get("title"));


        TextView n =  view.findViewById(R.id.phone);
        n.setText(cat.get("phone"));

        TextView l =  view.findViewById(R.id.Loction);
        l.setText(cat.get("location"));



        TextView time =  view.findViewById(R.id.more);
        time.setText(cat.get("more"));



        String path = CacheManager.getfile_path(context, cat.get("image"));


        ImageView delete =  view.findViewById(R.id.delete_bt);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacheManager._delete(context, "cons", cat.get("id"));
                Toast.makeText(context, context.getString(R.string.deleteusersmsg), Toast.LENGTH_LONG);
                context.startActivity(new Intent(context, MemsActivity.class));
            }
        });




        ImageView edit_bt =  view.findViewById(R.id.edit_bt);
        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                options.thisAccount = cat;

                context.startActivity(new Intent(context, AddContectActivity.class));
            }
        });



        ImageView call_bt =  view.findViewById(R.id.call_bt);
        call_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                options.thisAccount = cat;



                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", cat.get("phone"), null)));


            }
        });




        ImageView delete_bt =  view.findViewById(R.id.delete_bt);
        delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacheManager._delete(context, "cons", cat.get("id"));
                Toast.makeText(context, context.getString(R.string.deleteusersmsg), Toast.LENGTH_LONG);
                context.startActivity(new Intent(context, ContactActivity.class));
            }
        });




        ImageView i =  view.findViewById(R.id.image);
        if (path != null) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                i.setImageBitmap(myBitmap);
            }
        }


        return view;
    }


}
