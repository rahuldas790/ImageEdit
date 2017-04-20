package com.xinlan.imageeditlibrary;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class FinalActivity extends AppCompatActivity {


    ImageView image;
    EditText description;
    Button done;
    LinearLayout location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Status");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Display d = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        int width = size.x;

        image = (ImageView) findViewById(R.id.image);
        image.setImageURI(Uri.fromFile(new File(getIntent().getStringExtra("path"))));
        image.setLayoutParams(new LinearLayout.LayoutParams(width, width));


        description = (EditText) findViewById(R.id.text);
        done = (Button) findViewById(R.id.done);
        location = (LinearLayout) findViewById(R.id.parent);
        TextView add = new TextView(this);
        add.setGravity(Gravity.CENTER);
//        add.setPadding(10, 10, 10, 20);
        add.setText("       +       ");
        add.setTextSize(20);
        add.setTextColor(Color.WHITE);
        add.setBackgroundColor(Color.GRAY);

//        location.addView(add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocation();
            }
        });
    }

    private void addLocation() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Add Location");
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        params.setMargins(10, 50, 10, 50);
        params.gravity = Gravity.CENTER;
        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);
        et.setLayoutParams(params);
        et.setBackgroundColor(Color.TRANSPARENT);
        et.setHint("Location...");
        ad.setView(et);
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String location = et.getText().toString();
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                params.setMargins(10, 0, 0, 0);
                TextView add = new TextView(FinalActivity.this);
                add.setGravity(Gravity.CENTER);
                add.setText(location);
                add.setPadding(30, 0, 30, 0);
                add.setTextSize(14);
                add.setTextColor(Color.WHITE);
                add.setBackgroundColor(Color.GRAY);
                add.setLayoutParams(params);
                FinalActivity.this.location.addView(add);

                dialogInterface.dismiss();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.show();

    }


}
