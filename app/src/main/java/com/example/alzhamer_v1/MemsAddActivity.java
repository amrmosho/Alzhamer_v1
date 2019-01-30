package com.example.alzhamer_v1;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class MemsAddActivity extends AppCompatActivity {


    EditText am_title, am_nots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mems_add);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_edit_white_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        am_times_btn = findViewById(R.id.am_times_btn);
        am_nots = findViewById(R.id.am_notes);
        am_title = findViewById(R.id.am_title);

        cboxes[0] = findViewById(R.id.all);
        cboxes[4] = findViewById(R.id.str);
        cboxes[1] = findViewById(R.id.sun);
        cboxes[2] = findViewById(R.id.mon);
        cboxes[3] = findViewById(R.id.tus);
        cboxes[5] = findViewById(R.id.wen);
        cboxes[6] = findViewById(R.id.thr);
        cboxes[7] = findViewById(R.id.fri);


        radioButtons[0] = findViewById(R.id.am_color_r);
        radioButtons[1] = findViewById(R.id.am_color_b);
        radioButtons[2] = findViewById(R.id.am_color_g);
        radioButtons[3] = findViewById(R.id.am_color_bl);
        radioButtons[4] = findViewById(R.id.am_color_w);
        radioButtons[5] = findViewById(R.id.am_color_o);


        update_times();


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

    String selectedTime="";
    void update_data() {

        if (options.thisAccount != null) {

            id = options.thisAccount.get("id");

            am_title.setText(options.thisAccount.get("title"));
            am_nots.setText(options.thisAccount.get("notes"));
            am_times_btn.setText(getString(R.string.times) + " : " +  options.thisAccount.get("time"));
            String color =   options.thisAccount.get("color");
            for (RadioButton ra : radioButtons) {
                if (color.equalsIgnoreCase(ra.getTag().toString()) ) {
                    ra.setChecked(true);
                    break;
                }

            }

            String days[] = options.thisAccount.get("days").split(";");

            for (CheckBox c : cboxes) {

                if (Arrays.asList(days).contains(c.getTag())) {

                    c.setChecked(true);
                }




            }

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


    void update_times() {


        am_times_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;


                mTimePicker = new TimePickerDialog(MemsAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        am_times_btn.setText(selectedHour + ":" + selectedMinute);
                        selectedTime=selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



            }
        });


    }





    Button am_times_btn;



    ImageView btnimage;

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


    RadioButton[] radioButtons = new RadioButton[6];


    String getColor() {
        String r = "";
        for (RadioButton ra : radioButtons) {
            if (ra.isChecked()) {
                r = (String) ra.getTag();
                break;
            }

        }

        return r;

    }

    CheckBox[] cboxes = new CheckBox[8];

    String getDays() {
        String days = "";
        for (CheckBox c : cboxes) {
            if (c.isChecked()) {
                days += ";" + c.getTag();
            }

        }

        return days;
    }


    public void save(View view) {


        String image = UUID.randomUUID().toString();

        HashMap<String, String> data = new HashMap<>();

        if (selectedImage != null) {
            CacheManager.cacheimages(this, image, selectedImage);
        }


        if (am_title.getText().toString().trim() != "") {


            data.put("image", image);
            data.put("title", am_title.getText().toString());
            data.put("notes", am_nots.getText().toString());


            data.put("color", getColor());


            data.put("time", selectedTime);
            data.put("days", getDays());


              if (id.equals("")) {


            CacheManager._insert(this, "memos", data);

        } else {

              CacheManager._update(this, "memos", id, data);
                  startActivity(new Intent(this, MemsActivity.class));

             }


            Toast.makeText(this, "Save Done successfully", Toast.LENGTH_LONG).show();

            am_title.setText("");
            am_nots.setText("");
        } else {


            Toast.makeText(this, "You Name IS Empty", Toast.LENGTH_LONG).show();

        }
    }


    public void gotomeoms(View view) {

        startActivity(new Intent(this, MemsActivity.class));

    }
}
