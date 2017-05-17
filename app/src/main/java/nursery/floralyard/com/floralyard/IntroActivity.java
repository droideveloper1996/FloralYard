package nursery.floralyard.com.floralyard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TextView textView=(TextView)findViewById(R.id.floralYard);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/harrington_regular.ttf");
        textView.setTypeface(type);
        final ImageView imageView=(ImageView)findViewById(R.id.logo_logo);
        imageView.animate().alpha(1f).setDuration(2000).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                  Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);


                 startActivity(mainIntent);
            }
        }, 2500);
    }
}
