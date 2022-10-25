package in.jvapps.system_alert_window;

import android.content.Context;
import android.content.Intent;
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
//        Map<String, Object> headersMap = Commons.getMapFromObject(paramsMap, KEY_HEADER);
//        Map<String, Object> bodyMap = Commons.getMapFromObject(paramsMap, KEY_BODY);
//        Map<String, Object> footerMap = Commons.getMapFromObject(paramsMap, KEY_FOOTER);
//        LinearLayout headerView = new HeaderView(mContext, headersMap).getView();
//        LinearLayout bodyView = new BodyView(mContext, bodyMap).getView();
//        LinearLayout footerView = new FooterView(mContext, footerMap).getView();
//
//        bubbleLayout.setBackgroundColor(Color.WHITE);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        bubbleLayout.setLayoutParams(params);
//        bubbleLayout.addView(headerView);
//        bubbleLayout.addView(bodyView);
//        bubbleLayout.addView(footerView);
    }
}
