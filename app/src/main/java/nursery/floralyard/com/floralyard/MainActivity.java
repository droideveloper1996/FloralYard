package nursery.floralyard.com.floralyard;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    Toolbar toolbar;
    private WebView webView;
    TextView noConnection;
    private Context context;
    private ProgressBar progressBar;
    NavigationView navigationView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mtoggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        toolbar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        context = MainActivity.this;
        noConnection = (TextView) findViewById(R.id.noConnection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadWebPageFromURL();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_home:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/index.html");
                        return true;
                    case R.id.action_groceries:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/groceries.html");
                        return true;
                    case R.id.action_household:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/household.html");
                        return true;
                    case R.id.action_peronalCare:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/personalcare.html");
                        return true;
                    case R.id.action_packagedFood:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/packagedfoods.html");
                        return true;
                    case R.id.action_beverages:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/beverages.html");
                        return true;
                    case R.id.action_gourmet:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/gourmet.html");
                        return true;
                    case R.id.action_offers:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/offers.html");
                        return true;
                    case R.id.action_contacts:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/contact.html");
                        return true;
                    case R.id.action_login:
                        mDrawerLayout.closeDrawers();
                        webView.loadUrl("http://www.codeham.com/floralyard/web/registered.html");
                        return true;
                }
                return false;
            }
        });


        // Update the action bar title with the TypefaceSpan instance

        getSupportActionBar().setTitle("Floral Yard");
        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadWebPageFromURL() {
        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            webView = (WebView) findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Toast.makeText(context, "called shouldOverrideUrlLoading()", Toast.LENGTH_SHORT).show();

                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //  Toast.makeText(context, "Finished LOADING", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    //  Toast.makeText(context, "LOADING", Toast.LENGTH_SHORT).show();
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("http://www.codeham.com/floralyard/web");


        } else {
            noConnection.setVisibility(View.VISIBLE);
        }
        getNotification();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.mytoolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setColor(Color.GREEN);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floralyard1);
        builder.setContentTitle("Welome To Floral Yard");
        builder.setSmallIcon(R.drawable.logo);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);
        builder.setStyle(style);
        Notification notification = builder.build();
        NotificationManagerCompat.from(MainActivity.this).notify(0, notification);
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
