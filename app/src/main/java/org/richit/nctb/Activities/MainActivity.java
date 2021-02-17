package org.richit.nctb.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.richit.materialofficeaboutlib.Others.LoadListener;
import org.richit.materialofficeaboutlib.Others.OfficeAboutHelper;
import org.richit.nctb.AdUtils.AdHelper;
import org.richit.nctb.AdUtils.MyAdsListener;
import org.richit.nctb.AdUtils.MyAllAdsUtil;
import org.richit.nctb.Adapters.ClassTypeAdapter;
import org.richit.nctb.Config;
import org.richit.nctb.Models.ClassType;
import org.richit.nctb.Others.GlobalMethods;
import org.richit.nctb.Models.OnlineData;
import org.richit.nctb.R;

import java.util.ArrayList;

import p32929.myhouseads2lib.FayazSP;
import p32929.myhouseads2lib.HouseAds;
import p32929.myhouseads2lib.InterListener;
import p32929.updaterlib.AppUpdater;
import p32929.updaterlib.UpdateListener;
import p32929.updaterlib.UpdateModel;

public class MainActivity extends AppCompatActivity {

    //Views
    RecyclerView recyclerViewClass;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    LinearLayout linearLayoutAds;

    //Library Classes
    Drawer drawerUpper;
    HouseAds houseAds;
    OfficeAboutHelper officeAboutHelper;

    //Adapter
    ClassTypeAdapter classTypeAdapter;

    //Data variables
    OnlineData onlineData;
    ArrayList<ClassType> classTypes = new ArrayList<>();
    String jsonData = "";
    private boolean triedRemoveAds = false;
    private boolean triedSupportUs = false;

    //Dummy
    private String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.savedInstanceState=savedInstanceState;
        //Initializing AndroidNetworking for get,post,retrieve and create data from specific url
        AndroidNetworking.initialize(getApplicationContext());

        //Initializing shared preferences to show data when internet connection is unavailable
        FayazSP.init(this);

        //Initializing Views
        toolbar = findViewById(R.id.toolbar);
        recyclerViewClass = findViewById(R.id.recyclerView_class);
        linearLayoutAds = findViewById(R.id.adsLL);


        setSupportActionBar(toolbar);

        //Setting progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        //Setting the adapter for showing loaded data from adapter class via ClassModel
        //Here ClassType model use for type of student's classes such as class 1, class 2....
        classTypeAdapter = new ClassTypeAdapter(this, classTypes);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClass.setAdapter(classTypeAdapter);

        // file uri exception fixer
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Populating data from both offline and online
        populateList();

        //Initializing ads
        houseAds = new HouseAds(this,
                Config.houseAdsUrl);
        houseAds.setMenInBlack(true);

        //Sending user feedback to the developer
        houseAds.setFeedbackEmail(Config.feedBackEmail);

        //See Config.java to know more about OfficeAboutHelper
        officeAboutHelper = new OfficeAboutHelper(this, Config.officeAboutUrl);
        //Setting the CEO of the company always top of the list
        officeAboutHelper.shuffleAndBringSomeoneTopByDesignation("ceo", false);

        //Drawer method for open and closing drawer
        drawer();

        //Adding ads to the app
        adsAdder(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //This houseAds are showing if user finally intended to exit the app
        houseAds.setListener(new InterListener() {
            @Override
            public void onShow(LinearLayout linearLayoutAboveList) {
                MyAllAdsUtil.addAnyBannerAd(true, MainActivity.this, true, linearLayoutAboveList, new MyAdsListener());
            }
        });
        houseAds.showInterAds();
    }

    private void populateList() {
        if (isNetworkAvailable()) {

            //Showing data from online when device is connected with online
            progressDialog.show();

            //Update data when device is connected with the online
            new AppUpdater(this, Config.booksDataUrl, new UpdateListener() {
                @Override
                public void onJsonDataReceived(final UpdateModel updateModel, JSONObject jsonObject) {

                    //Save data to device using FayazSP for further retrieving data
                    FayazSP.put("json", jsonObject.toString());

                    //onlineData are those retrieved JSON data that uploaded to github
                    //Here we use Gson for parsing the data from internet
                    //Because it's easy for parsing JSON data by using GSON library
                    //You can use your own JSON data parsing library
                    onlineData = new Gson().fromJson(jsonObject.toString(), OnlineData.class);

                    //setting data to the adapter
                    classTypeAdapter.replaceArrayList(onlineData.getClasses());

                    //Hiding progress dialog when data retrieved
                    progressDialog.dismiss();

                    try {

                        //Getting Encrypted ad IDs
                        String encryptedString = jsonObject.getString("gs");
                        Log.d(TAG, "onJsonDataReceived: " + encryptedString);

                        //Initializing the ads
                        GlobalMethods.initAdIds(encryptedString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //If there is any update for the app version
                    if (AppUpdater.getCurrentVersionCode(MainActivity.this) < updateModel.getVersionCode()) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Update available")
                                .setCancelable(updateModel.isCancellable())
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateModel.getUrl()));
                                        startActivity(browserIntent);
                                        finish();
                                    }
                                })
                                .show();
                    }
                }



                @Override
                public void onError(String error) {

                    Toast.makeText(MainActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }
            }).execute();
        } else {

            //getting data from device shared preference
            jsonData = FayazSP.getString("json", "");

            //Check if there is any data in shared preferences
            if (jsonData.isEmpty()) {
                //If there is no data in shared preference then show alert for connecting the device with internet
                showNoInternetDialog();
            } else {

                //if there is data in shared preferences then set that data to the Online data model
                onlineData = new Gson().fromJson(jsonData, OnlineData.class);

                //Set the retrieved data to class type adapter for showing the data
                classTypeAdapter.replaceArrayList(onlineData.getClasses());
            }
        }

    }

    //Adding component to Navigation Drawer
    public void drawer() {

        drawerUpper = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)

                //at the top of the drawer showing the app name
                .withHeader(R.layout.header)
                .addDrawerItems(

                        //Setting drawer items
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(R.drawable.ic_book_open_page_variant_grey600_36dp).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Support us").withIcon(R.mipmap.ic_gift_grey600_36dp).withIdentifier(2),
                        new PrimaryDrawerItem().withName("About").withIcon(R.mipmap.ic_information_grey600_36dp).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Rate").withIcon(R.mipmap.ic_star_grey600_36dp).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Share").withIcon(R.drawable.ic_share_variant_grey600_36dp).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position) {
                            case 2:

                                //If user click to the second position of the drawer item
                                //it shows the support dialog
                                drawerUpper.closeDrawer();
                                showSupportUsDialog();
                                drawerUpper.setSelection(1);
                                break;

                            case 3:

                                //If user click to the third position of the drawer item
                                //it shows the about of the developer team
                                drawerUpper.closeDrawer();
                                showAbout();
                                drawerUpper.setSelection(1);
                                break;

                            case 4:

                                //If user click to the fourth position of the drawer item
                                //it shows the App rating dialog
                                drawerUpper.closeDrawer();
                                showRateDialog();
                                drawerUpper.setSelection(1);
                                break;

                            case 5:

                                //If user click to the fifth position of the drawer item
                                //it shows the app sharing medias
                                drawerUpper.closeDrawer();
                                houseAds.shareApp();
                                MyAllAdsUtil.showAdsLogDialog(MainActivity.this);
                                drawerUpper.setSelection(1);
                        }
                        return false;
                    }
                })
                .build();
    }

    private void showRateDialog() {

        //dialog for user rating
        houseAds.showRateDialog();
    }

    private void showAbout() {

        //this is for the details of the developer team you can check here https://github.com/p32929/OfficeAbout
        officeAboutHelper.showAboutActivity(true, new LoadListener() {
            @Override
            public void onLoad(LinearLayout linearLayoutDummy) {

                //Show BannerAds at the beginning when the data is loaded
                MyAllAdsUtil.addAllBannerAds(MainActivity.this, linearLayoutDummy, new MyAdsListener());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Interface for reward ad
    MyAdsListener rewardListener = new MyAdsListener() {
        @Override
        public void onAnySuccess(String which) {
            super.onAnySuccess(which);
            progressDialog.dismiss();
        }

        @Override
        public void onAnyRewarded(String which) {
            super.onAnyRewarded(which);
            progressDialog.dismiss();
            checkRewarded(true);
        }

        @Override
        public void onTotalFailure() {
            super.onTotalFailure();
            progressDialog.dismiss();
            checkRewarded(false);
        }

        @Override
        public void onClosed(String which) {
            super.onClosed(which);
            progressDialog.dismiss();
        }
    };

    private void showSupportUsDialog() {

        //This alert dialog contain ads for supporting the developer
        //if user watch a video from this app, developer can get little cent from google or any other app store
        // where the app is uploaded
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Support us")
                .setCancelable(false)
                .setMessage("You can support us by watching a video ad. Would you like to continue?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        triedSupportUs = true;

                        //Showing the Reward app into a given time limit
                        MyAllAdsUtil.showAnyReward(true, MainActivity.this, rewardListener);

                        //Showing the Interstitial Ads
                        MyAllAdsUtil.showAnyInter(true, MainActivity.this, new MyAdsListener());
                        progressDialog.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyAllAdsUtil.showAnyInter(true, MainActivity.this, new MyAdsListener());
                    }
                });

        MyAllAdsUtil.addBannerAdsInDialog(MainActivity.this, builder);
        builder.show();
    }

//    private void showRemoveAdsDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("REMOVE ADS")
//                .setCancelable(false)
//                .setMessage("By clicking OK, it will play a video ad. After watching the full video ad, it will remove all the ads for this session from the app.")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        triedRemoveAds = true;
//                        MyAllAdsUtil.showAnyReward(true, MainActivity.this, rewardListener);
//                        MyAllAdsUtil.showAnyInter(true, MainActivity.this, new MyAdsListener());
//                        progressDialog.show();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        MyAllAdsUtil.showAnyInter(true, MainActivity.this, new MyAdsListener());
//                    }
//                })
//                .show();
//    }

    //Alert for internet connection
    private void showNoInternetDialog() {

        //This dialog will be shown if the device is not connected with the online
        new AlertDialog.Builder(this)
                .setTitle("No internet")
                .setMessage("Please turn on your internet connection")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        populateList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    private boolean isNetworkAvailable() {

        //Get the connectivity service from the device
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //Return true if there is no connectivity issues with the internet
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    private void adsAdder(Bundle savedInstanceState) {

        // At last, we need to call this to initialize the Admob & StartApp SDKs
        MyAllAdsUtil.init(this, savedInstanceState, R.layout.splash_layout, true);

        // We're also adding a banner, below all the contents
        MyAllAdsUtil.addAnyBannerAd(true, this, true, linearLayoutAds, new MyAdsListener());


    }

    //Method for reward ads
    private void checkRewarded(boolean isRewardShowing) {

        //dismiss when reward ad loaded
        progressDialog.dismiss();

        //Check if user want to watch ads or not
        //If reward is showing then remove the ads by user selection
        //otherwise if user stop watching ad developer don't get any rewards from that ad
        if (triedRemoveAds) {
            if (isRewardShowing) {
                AdHelper.setAdsRemoved(true, linearLayoutAds);
                classTypeAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Ads removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "You need to watch the full video ads to remove ads", Toast.LENGTH_LONG).show();
            }
        } else if (triedSupportUs) {

            //if user want to support the developer
            if (isRewardShowing)
                AdHelper.setAdsRemoved(true, linearLayoutAds);
            Toast.makeText(MainActivity.this, "Thank you so much for your support", Toast.LENGTH_SHORT).show();
        }

        triedRemoveAds = false;
        triedSupportUs = false;
    }


}
