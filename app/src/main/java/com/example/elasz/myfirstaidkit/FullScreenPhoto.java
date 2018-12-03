package com.example.elasz.myfirstaidkit;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullScreenPhoto extends AppCompatActivity {

    @BindView(R.id.imgDisplay)
    ImageView imgDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");

        //ImageView imgDisplay;
        Button btnClose;


        //imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        //btnClose = (Button) findViewById(R.id.btnClose);


       /* btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenPhoto.this.finish();
            }
        });*/


        imgDisplay.setImageBitmap(bmp );

    }

    @OnClick(R.id.btnClose)
    public void closeActivity(){
        FullScreenPhoto.this.finish();
    }
}

