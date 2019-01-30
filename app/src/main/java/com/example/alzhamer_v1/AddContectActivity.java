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
import com.example.alzhamer_v1.libs.Map;
import com.example.alzhamer_v1.libs.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class AddContectActivity extends AppCompatActivity {



    EditText ac_title, ac_phone, ac_Loction,am_nots ,ac_More;
    ImageView btnimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contect);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_edit_white_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        ac_title = findViewById(R.id.ac_title);
        ac_phone = findViewById(R.id.ac_phone);
        ac_More = findViewById(R.id.ac_More);
        ac_Loction = findViewById(R.id.ac_Loction);
        am_nots = findViewById(R.id.am_notes);




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
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }




    void update_data() {

        if (options.thisAccount != null) {

            id = options.thisAccount.get("id");

            ac_title.setText(options.thisAccount.get("title"));
            ac_phone.setText(options.thisAccount.get("phone"));
            ac_More.setText(options.thisAccount.get("more"));
            ac_Loction.setText(options.thisAccount.get("location"));


            String path = CacheManager.getfile_path(this, options.thisAccount.get("image"));
            if (path != null) {
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    btnimage.setImageBitmap(myBitmap);
                }
            }
            options.thisAccount = null;
        }


    }




String id ="";






    public void save(View view) {


        String image = UUID.randomUUID().toString();

        HashMap<String, String> data = new HashMap<>();

        if (selectedImage != null) {
            CacheManager.cacheimages(this, image, selectedImage);
        }


        if (ac_title.getText().toString().trim() != "") {


            data.put("image", image);
            data.put("title", ac_title.getText().toString());
            data.put("more", ac_More.getText().toString());
            data.put("location", ac_Loction.getText().toString());
            data.put("phone", ac_phone.getText().toString());




            if (id.equals("")) {


                CacheManager._insert(this, "cons", data);

            } else {

                CacheManager._update(this, "cons", id, data);
                startActivity(new Intent(this, ContactActivity.class));

            }


            Toast.makeText(this, "Save Done successfully", Toast.LENGTH_LONG).show();

            ac_title.setText("");
            ac_More.setText("");
            ac_Loction.setText("");
            ac_phone.setText("");





        } else {


            Toast.makeText(this, "You Name IS Empty", Toast.LENGTH_LONG).show();

        }

    }








    public void gotoHome(View view) {

        startActivity(new Intent(this , HomeActivity.class));
    }

    public void gotoCons(View view) {

        startActivity(new Intent(this , ContactActivity.class));
    }

    public void getLoction(View view) {



        Map map = new Map(this,ac_Loction);


        map.getLoctions_Dialog();





    }

}
