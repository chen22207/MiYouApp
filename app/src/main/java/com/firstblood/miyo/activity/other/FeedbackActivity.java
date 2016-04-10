package com.firstblood.miyo.activity.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.firstblood.miyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {

    @InjectView(R.id.feedback_content_et)
    EditText feedbackContentEt;
    @InjectView(R.id.feedback_contact_et)
    EditText feedbackContactEt;
    @InjectView(R.id.feedback_submit_bt)
    Button feedbackSubmitBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.feedback_submit_bt)
    public void onClick() {

    }
}
