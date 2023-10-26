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

public class EventRallyUpActivity extends AppCompatActivity {

    private ConstraintLayout bubbleLayout;


    private Context mContext;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_rally_up_activity);
        mContext = this;
        bubbleLayout = findViewById(R.id.parentLayout);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            HashMap<String, Object> paramsMap = (HashMap<String, Object>) intent.getSerializableExtra(INTENT_EXTRA_PARAMS_MAP);
            configureUI(paramsMap);
        }
    }

    void configureUI(HashMap<String, Object> paramsMap) {
        String eventID = (String) paramsMap.get("id");
        findViewById(R.id.dismissButton).setOnClickListener(view -> finish());
        findViewById(R.id.viewButton).setOnClickListener(view -> viewRequest(eventID));
        findViewById(R.id.settingsButton).setOnClickListener(view -> openSettings());
        TextView eventTitle = findViewById(R.id.eventTitle);
        TextView eventDescription = findViewById(R.id.eventDescription);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView startTime = findViewById(R.id.startTime);
        TextView startDate = findViewById(R.id.startDate);
        TextView endTime = findViewById(R.id.endTime);
        TextView endDate = findViewById(R.id.endDate);
        String title = (String) paramsMap.get("title");
        String startTimeString = (String) paramsMap.get("start_time");
        String startDateString = (String) paramsMap.get("start_date");
        String endTimeString = (String) paramsMap.get("end_time");
        String endDateString = (String) paramsMap.get("end_date");
        String description = (String) paramsMap.get("description");
        if (title != null && title.length() > 0) {
            eventTitle.setText(title);
        }
        if (description != null && description.length() > 0) {
            eventDescription.setText(description);
        }
        if (startTimeString != null && startTimeString.length() > 0) {
            startTime.setText(startTimeString);
        }
        if (startDateString != null && startDateString.length() > 0) {
            startDate.setText(startDateString);
        }
        if (endTimeString != null && endTimeString.length() > 0) {
            endTime.setText(endTimeString);
        }
        if (endDateString != null && endDateString.length() > 0) {
            endDate.setText(endDateString);
        }
        String imageUrl = (String) paramsMap.get("profileImageUrl");
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get()
                    .load(imageUrl)
                    .into(profileImage);
        }
    }

    void viewRequest(String eventID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("teamz://teamz.com/events?eventID=" + eventID));
        launchIntent(intent);
        finish();
    }

    void openSettings() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("teamz://teamz.com/settings"));
        launchIntent(intent);
        finish();
    }

    void launchIntent(Intent intent) {
        mContext.startActivity(intent);
    }
}
