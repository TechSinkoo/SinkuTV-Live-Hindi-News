package com.abhinav.sinkutv;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jarvanmo.exoplayerview.media.ExoMediaSource;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.media.SimpleQuality;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;

public class VideoplayActivity extends AppCompatActivity {

    ImageView imagefire;
    private ExoVideoView videoView;
//    private ImageButton modeFit;
//    private ImageButton modeNone;
//    private ImageButton modeHeight;
//    private ImageButton modeWidth;
//    private ImageButton modeZoom;
    private View wrapper;
    private Button play;
    String s;
    String websiteurlfor;
    //TextView subytc;

    WebView webView;
  InterstitialAd mInterstitialAd;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String mytxt;
  //  private MediaController mediaController;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_videoplay);

//        try{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Window w = getWindow();
//                w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
//        }catch (Exception e ){}

        loadAds();
        webView = findViewById(R.id.webViewForWebsite);
        videoView = findViewById(R.id.videoView);
        imagefire = findViewById(R.id.imgfire);

        videoView = findViewById(R.id.videoView);

        // subytc = findViewById(R.id.subyoutube);
//        modeFit = findViewById(R.id.mode_fit);
//        modeNone = findViewById(R.id.mode_none);
//        modeHeight = findViewById(R.id.mode_height);
//        modeWidth = findViewById(R.id.mode_width);
//        modeZoom = findViewById(R.id.mode_zoom);

        wrapper = findViewById(R.id.wrapper);
        play = findViewById(R.id.changeMode);
        videoView.setBackListener((view, isPortrait) -> {
            if (isPortrait) {
                finish();
            }
            return false;
        });


        videoView.setOrientationListener(orientation -> {
            if (orientation == SENSOR_PORTRAIT) {
                changeToPortrait();
            } else if (orientation == SENSOR_LANDSCAPE) {
                changeToLandscape();
            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                interstitialAdLoad();

            }
        });




        myRef.child("BgImg").child("BgImg").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(imagefire);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        Intent intentL = getIntent();
        s = intentL.getStringExtra("vdokey");

       SimpleMediaSource mediaSource = new SimpleMediaSource(s);


        mediaSource.setDisplayName(" Video Playing");
        List<ExoMediaSource.Quality> qualities = new ArrayList<>();
        ExoMediaSource.Quality quality;

        for (int i = 0; i < 6; i++) {
            SpannableString spannableString = new SpannableString("Quality" + i);
            if (i % 2 == 0) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.YELLOW);
                spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            } else {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
                spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }


            quality = new SimpleQuality(spannableString, mediaSource.uri());
            qualities.add(quality);
        }
        mediaSource.setQualities(qualities);

        videoView.play(mediaSource, false);
        play.setOnClickListener(view -> {
            //videoView.play(mediaSource);

            videoView.getPlayer().seekTo((int) videoView.getPlayer().getDuration()-30000);
            play.setVisibility(View.GONE);
//            AlertDialog.Builder builder = new AlertDialog.Builder(VideoplayActivity.this);
//            builder.setTitle("Network Stream");
//            builder.setMessage("Please Enter Channel URL: ");
//            final EditText input = new EditText(VideoplayActivity.this);
//            input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//            builder.setView(input);
//            builder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    mytxt = input.getText().toString();
//
//                    String channelLink = mytxt;
//                    SimpleMediaSource ms = new SimpleMediaSource(channelLink);
//                    videoView.play(ms);
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();


        });
//        modeFit.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT));
//      //  modeWidth.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH));
//        modeHeight.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT));
//        modeNone.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL));
//        modeZoom.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM));

//        subytc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("https://www.youtube.com/c/Techieabhi");
//                Intent intenti = new Intent(Intent.ACTION_VIEW,uri);
//                startActivity(intenti);
//            }
//        });

//modeWidth.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        changeToLandscape();
//
//    }
//});


        Intent webisi = getIntent();
        websiteurlfor = webisi.getStringExtra("web");


        webView = (WebView) findViewById(R.id.webViewForWebsite);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setBackgroundColor(0x00000000);
        webView.loadUrl(websiteurlfor);
        webView.setWebViewClient(new WebViewClient(){

            //press ctrl+O
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                webView.loadUrl("file:///android_asset/errorr.JPG");
            }
        });

   }
//        Uri uri = Uri.parse(s);
//        videoView.setVideoURI(uri);
//
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        mediaController.setMediaPlayer(videoView);
//        videoView.setMediaController(mediaController);
//        videoView.start();

    private void changeToPortrait() {

        WindowManager.LayoutParams attr = getWindow().getAttributes();
//        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        window.setAttributes(attr);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        wrapper.setVisibility(View.VISIBLE);
    }


    private void changeToLandscape() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //   lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        wrapper.setVisibility(View.GONE);
      showInterstitialAd();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.resume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= 23)) {
            videoView.resume();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            videoView.pause();

        }
    }



    @Override
    public void onStop() {
        super.onStop();
       videoView.pause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.releasePlayer();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return videoView.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    //google banner ads load
    private void loadAds() {

        AdView mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



    //google interstitial ads
    public void interstitialAdLoad(){

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.interstitialAd_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                       // Log.d("Admob interstitial", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                //interstitialAdLoad();



                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                               // Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                //Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                      //  Log.d("Admob interstitial", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }



    private void showInterstitialAd(){

        if (mInterstitialAd != null) {
            mInterstitialAd.show(VideoplayActivity.this);
        } else {

            //Toast.makeText(MainActivity.this, "yourmessage", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onUserLeaveHint () {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            enterPictureInPictureMode();
        }


    }


//@RequiresApi(api = Build.VERSION_CODES.O)
//public void pipViewG(){
//
//try{
//
//
//
////        Display display  = getWindowManager().getDefaultDisplay();
////        Point size = new Point();
////        display .getSize(size);
////        int width = size.y;
////       int height = size.x;
//
//        Rational ratio = new Rational(videoView.getWidth(), videoView.getHeight());
//        PictureInPictureParams.Builder pip_Builder = new PictureInPictureParams.Builder();
//        pip_Builder.setAspectRatio(ratio).build();
//        enterPictureInPictureMode(pip_Builder.build());
//
//
//    }catch (Exception e ){}
//
//}






}

