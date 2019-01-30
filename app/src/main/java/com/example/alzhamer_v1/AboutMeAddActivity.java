package com.example.alzhamer_v1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AboutMeAddActivity extends AppCompatActivity {

    EditText txt_name, txt_phone, txt_live_it, txt_born_date, txt_blood_group, txt_age,txt_location;
    ImageView btnimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me_add);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_edit_white_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        txt_name = findViewById(R.id.txt_name);
        txt_age = findViewById(R.id.txt_age);
        txt_blood_group = findViewById(R.id.txt_blood_group);
        txt_born_date = findViewById(R.id.txt_born_date);
        txt_live_it = findViewById(R.id.txt_live_it);
        txt_phone = findViewById(R.id.txt_phone);
        txt_location =findViewById(R.id.txt_loction);

        btnimage = findViewById(R.id.image);
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 5);
            }
        });
        update_data();




    }




    String id = "";

    void update_data() {


        ArrayList<HashMap<String, String>> alldata= CacheManager.getData(this, "aboutme");
        if (!alldata.isEmpty()) {


            HashMap<String, String> data = alldata.get(0);

            if (!data.get("id").equals("")) {
                id = data.get("id");
                txt_name.setText(data.get("name"));
                txt_phone.setText(data.get("phone"));
                txt_live_it.setText(data.get("live_it"));
                txt_born_date.setText(data.get("born_date"));
                txt_blood_group.setText(data.get("blood_group"));
                txt_age.setText(data.get("age"));

                if (options.location.containsKey("lat")&&options.location.containsKey("lng")) {

                    txt_location.setText(options.location.get("lat")+","+options.location.get("lng"));

                }else{

                    txt_location.setText(data.get("location"));


                }



                String path = CacheManager.getfile_path(this, data.get("image"));

                if (path != null) {
                    File imgFile = new File(path);
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        btnimage.setImageBitmap(myBitmap);
                    }
                }
            }
        }
    }


    Bitmap selectedImage;

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                btnimage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AboutMeAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(AboutMeAddActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }





    public void save(View view) {


        String image = UUID.randomUUID().toString();

        HashMap<String, String> data = new HashMap<>();

        if (selectedImage != null) {
            CacheManager.cacheimages(this, image, selectedImage);
        }


        if (txt_name.getText().toString().trim() != "") {


            data.put("image", image);

            data.put("name", txt_name.getText().toString());
            data.put("age", txt_age.getText().toString());
            data.put("blood_group", txt_blood_group.getText().toString());
            data.put("born_date", txt_born_date.getText().toString());
            data.put("live_it", txt_live_it.getText().toString());
            data.put("phone", txt_phone.getText().toString());

            data.put("location", txt_location.getText().toString());

            if (id.equals("")) {
                CacheManager._insert(this, "aboutme", data);

            } else {

                CacheManager._update(this, "aboutme", id, data);

            }


            Toast.makeText(AboutMeAddActivity.this, "Save Done successfully", Toast.LENGTH_LONG).show();

          /*  txt_name.setText("");
            txt_age.setText("");
            txt_blood_group.setText("");
            txt_born_date.setText("");
            txt_live_it.setText("");
            txt_phone.setText("");*/


        } else {


            Toast.makeText(AboutMeAddActivity.this, "You Name IS Empty", Toast.LENGTH_LONG).show();

        }
    }

    public void gotoaboutme(View view) {

        startActivity(new Intent(this, AboutmeActivity.class));

    }

    public void gotohome(View view) {

        startActivity(new Intent(this, HomeActivity.class));

    }


    public void getLoction(View view) {

        Intent i=     new Intent(this , GetLoctionActivity.class);
        i.putExtra("status","create");
        i.putExtra("from","aboutme");




        startActivity(i);

    }
}
