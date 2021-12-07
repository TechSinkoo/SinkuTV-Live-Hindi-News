package com.abhinav.sinkutv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView img;
    String mytxt;
    ImageView imagefire;
    CategoryAdapter adapter;
    SearchView searchView;
    InterstitialAd mInterstitialAd;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private Dialog loadingDialog;
    private static final String ONESIGNAL_APP_ID = "129c4841-b538-4624-a8a0-c927d46738d5";


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager1;
    public static List<CategoryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Window w = getWindow();
//                w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
//        }catch (Exception e ){}


        Toolbar toolbar = findViewById(R.id.toolbar);
        loadAds();
        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle("SinkuTV");
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Live - Hindi News");
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
        actionbar.setDisplayHomeAsUpEnabled(true);
        img = findViewById(R.id.icon);
        imagefire = findViewById(R.id.imgfire);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        recyclerView = findViewById(R.id.rv);

        // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager1 = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager1);

        list = new ArrayList<>();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.navitaion);


        //  navigationView.setNavigationItemSelectedListener(MainActivity.this);
        navigationView.bringToFront();

        View headView = navigationView.getHeaderView(0);
        TextView disclm = headView.findViewById(R.id.disclaimer);
        TextView titleapp = headView.findViewById(R.id.titlebottomtext);
        ImageView icon_nav_haeder = headView.findViewById(R.id.nav_header_imageView);


        adapter = new CategoryAdapter(list);
        recyclerView.setAdapter(adapter);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        loadingDialog.show();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                interstitialAdLoad();

            }
        });
        myRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    List<String> sets = new ArrayList<>();
                    for (DataSnapshot dataSnapshot2 : snapshot1.child("sets").getChildren()) {
                        sets.add(dataSnapshot2.getKey());
                    }

                    list.add(new CategoryModel(snapshot1.child("name").getValue().toString(),
                            snapshot1.child("imageurl").getValue().toString(),
                            snapshot1.child("url").getValue().toString(),
                            snapshot1.child("websiteurl").getValue().toString(),
                            snapshot1.getKey()

                    ));

                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_network_stream:

                        dialogShow();

                        break;
                    case R.id.nav_share:
                        //   Toast.makeText(MainActivity.this, "share", Toast.LENGTH_SHORT).show();
                        Intent intentInvite = new Intent(Intent.ACTION_SEND);
                        intentInvite.setType("text/plain");
                        String body = "Download Sinku TV and watch Live live news \nhttps://play.google.com/store/apps/details?id=com.abhinav.sinkutv";
                        String subject = "Download SinkuTV and watch free LiveTV - Live News.";
                        intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intentInvite.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(intentInvite, "Share using"));
                        break;

                    //method 2 with image..

                    //BitmapDrawable drawable = (BitmapDrawable)

//                        Uri uri2 = Uri.fromFile(image)
//                        Intent intent1 = new Intent();
//                        intent1.setAction(Intent.ACTION_SEND);
//                        intent1.setType("image/*");
//                        intent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "App Name");
//                        intent1.putExtra(Intent.EXTRA_TEXT, "Download the app from google play store now - "+ APP_STORE_URL);
//                        intent1.putExtra(Intent.EXTRA_STREAM, uri2);
//                        try {
//                            startActivity(Intent.createChooser(intent1, "Share"));
//                        } catch (ActivityNotFoundException e) {
//                            Toast.makeText(MainActivity.this, "please try again", Toast.LENGTH_SHORT).show();
//                        }


                    case R.id.nav_rate:
                        //Toast.makeText(MainActivity.this, "Rate us", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                        Intent playstoreIntent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(playstoreIntent);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Unable to open" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.subscribe:

                        Uri uri2 = Uri.parse("https://www.youtube.com/c/Techieabhi");
                        Intent intenti = new Intent(Intent.ACTION_VIEW, uri2);
                        startActivity(intenti);
                        break;

                    case R.id.valuecredit:

                        Intent intentValueCredit = new Intent(MainActivity.this, WebViewActivity.class);
                        intentValueCredit.putExtra("url", "https://sinkutv-hindinews.blogspot.com/p/valuecredit.html");
                        startActivity(intentValueCredit);
                        break;

                    case R.id.privacypolicy:

                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("url", "https://sinkutv-hindinews.blogspot.com/p/sinkutv-hindi-news-privacy-policy.html");
                        startActivity(intent);
                        break;

                    case R.id.moreapps:
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=TecStudyAP"));
                        startActivity(i);
                        break;

                    case R.id.appdev:
                        AppDevDialog();

                        break;

                    case R.id.newsPub:

                        Intent newspub = new Intent(MainActivity.this, WebViewActivity.class);
                        newspub.putExtra("url", "https://sinkutv-hindinews.blogspot.com/p/news-publisher-information.html");
                        startActivity(newspub);
                        break;

                    case R.id.exitbtn:
                        finish();
                        System.exit(0);
                        break;

                }

                return true;
            }
        });


        disclm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disclaimerr();
            }
        });

        titleapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disclaimerr();
            }
        });
        icon_nav_haeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri2 = Uri.parse("https://www.youtube.com/c/Techieabhi");
                Intent intenti = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intenti);

            }
        });

        setupHyperlink();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void loadAds() {

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Network Stream");
        builder.setMessage("Please Enter Channel URL: ");
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);
        builder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mytxt = input.getText().toString();

                String channelLink = mytxt;
                Intent intentChannelLink = new Intent(MainActivity.this, VideoplayActivity.class);
                intentChannelLink.putExtra("vdokey", channelLink);
                MainActivity.this.startActivity(intentChannelLink);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void AppDevDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Contact us:");
        builder.setMessage("\nEmail:- abhinav290592@gmail.com\n\nYoutube:- @TecStudyAP \n\nInstagram:- @TecStudyAP");

        builder.setPositiveButton("OK", null);

        builder.setNegativeButton("", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void disclaimerr() {
        final SpannableString s = new SpannableString(" This news app does not stream any of the channels included in its website / app. All the streaming links are from third  parties available free on the internet. If any one founds that  we are violating by broadcasting any copyright protected channel email us the channel name, based on your email request.\n\n Email ID : abhinav290592@gmail.com \n\n As of now this all news channels are streamed by Live - Hindi News. We are not responsible for its contents. ");

//added a TextView
        final TextView tx1=new TextView(this);
        tx1.setText(s);
        tx1.setAutoLinkMask(RESULT_OK);
        tx1.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Disclaimer");

       // builder.setMessage(R.string.disclaimerDialog);

        builder.setPositiveButton("OK", null);

        builder.setNegativeButton("", null);
        builder.setView(tx1);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    public void interstitialAdLoad() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.interstitialAd_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //  Log.d("Admob interstitial", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
                                // Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.d("Admob interstitial", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchview, menu);
        MenuItem menuItem = menu.findItem(R.id.searchIcon);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter Channel Name");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //adapter.getFilter().filter(newText);


                search(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void showInterstitialAd() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {

            //Toast.makeText(MainActivity.this, "yourmessage", Toast.LENGTH_SHORT).show();
        }
    }

    private void search(String str) {
        ArrayList<CategoryModel> myList = new ArrayList<>();
        for (CategoryModel object : list) {
            if (object.getName().toLowerCase().contains(str.toLowerCase())) {

                myList.add(object);
            }
        }

        CategoryAdapter adapterM = new CategoryAdapter(myList);

        recyclerView.setAdapter(adapterM);

    }

    private void setupHyperlink() {



    }
}