package in.jvapps.system_alert_window;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;

import static in.jvapps.system_alert_window.utils.Constants.INTENT_EXTRA_PARAMS_MAP;

import com.squareup.picasso.Picasso;

public class AttentionSeekerActivity extends AppCompatActivity {

    private ConstraintLayout bubbleLayout;


    private Context mContext;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_seeker_activity);
        mContext = this;
        bubbleLayout = findViewById(R.id.parentLayout);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            HashMap<String, Object> paramsMap = (HashMap<String, Object>) intent.getSerializableExtra(INTENT_EXTRA_PARAMS_MAP);
            configureUI(paramsMap);
        }
    }

    void configureUI(HashMap<String, Object> paramsMap) {
        findViewById(R.id.dismissButton).setOnClickListener(view -> finish());
        findViewById(R.id.viewButton).setOnClickListener(view -> viewRequest());
        findViewById(R.id.settingsButton).setOnClickListener(view -> openSettings());
        TextView attentionMessage = findViewById(R.id.attentionMessage);
        ImageView profileImage = findViewById(R.id.profileImage);
        String nickName = (String) paramsMap.get("nickName");
        if (nickName != null && nickName.length() > 0) {
            String message = nickName + " has requested attention";
            attentionMessage.setText(message);
        }
        String imageUrl = (String) paramsMap.get("profileImageUrl");
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get()
                    .load(imageUrl)
                    .into(profileImage);
        }
    }

    void viewRequest(){
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData (Uri.parse("icayal://icayal.com/updates"));
        launchIntent(intent);
        finish();
    }

    void openSettings(){
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData (Uri.parse("icayal://icayal.com/settings"));
        launchIntent(intent);
        finish();
    }

    void launchIntent(Intent intent){
        mContext.startActivity(intent);
    }
}
