package com.classyinc.classytreasurer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.classyinc.classytreasurer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;



public class Developer extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Version 2.1");

        Element bio = new Element();
        bio.setTitle("I like Music, Cricket n Coding!");
        bio.setIcon(R.drawable.star);
        bio.setColor(R.color.colorAccent);

        Element bio1 = new Element();
        bio1.setTitle("I love reading Books (Fav:The Alchemist) ");
        bio1.setIcon(R.drawable.star);
        bio1.setColor(R.color.date_color);

        Element subbio = new Element();
        subbio.setTitle("A Breaking Bad fanatic !");
        subbio.setIcon(R.drawable.star);


        Element bio2 = new Element();
        bio2.setTitle("I can also build Websites and Projects ! ");
        bio2.setIcon(R.drawable.star);
        bio2.setColor(R.color.colorPrimary);

        Element subbio2 = new Element();
        subbio2.setTitle("Don't hesitate to Contact mE!");

        String des = "Hey ! I am Sai Hari\nI am currently pursuing my UG at SVCE specialised in CS";

        View aboutpage = new AboutPage(context)
                .isRTL(false)
                .setImage(R.drawable.profile)
                .setDescription(des)

                .addGroup("Chill Stuffs")
                .addItem(bio)
                .addItem(bio1)
                .addItem(subbio)

                .addItem(bio2)
                .addItem(subbio2)

                .addGroup("Connect with me")
                .addEmail("krishnansaihari@gmail.com")
                .addInstagram("classy_introvert_")
                .addFacebook("profile.php?id=100012392674579")
                .addTwitter("Sai_Hari_g")
                .addGitHub("saihariG")

                .addItem(new Element().setTitle("Classy Treasurer"))
                .addItem(adsElement)
                .addItem(createCopyright())
                .create();

        setContentView(aboutpage);
        MobileAds.initialize(context,"ca-app-pub-4710955483788759~6292166403");

        final InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-4710955483788759/7244664731");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                if(interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
            /*@Override
            public void onAdFailedToLoad(int errorCode){
                if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Toast.makeText(Developer.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                    Toast.makeText(Developer.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR){
                    Toast.makeText(Developer.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NO_FILL){
                    Toast.makeText(Developer.this,errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_APP_ID_MISSING){
                    Toast.makeText(Developer.this,errorCode, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAdOpened() {

            }
            @Override
            public void onAdClosed() {

            } */

        });
    }

    private Element createCopyright() {

        Element copyright = new Element();
        final String copyrightstring = String.format("Copyright %d by Classy Inc.", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightstring);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , copyrightstring, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, HomeActivity.class));
        overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
    }

}
