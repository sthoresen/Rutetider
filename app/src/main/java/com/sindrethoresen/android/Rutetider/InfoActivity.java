package com.sindrethoresen.android.Rutetider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by st97m_000 on 14.08.2017.
 */

public class InfoActivity extends AppCompatActivity {
    TextView txtLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtLink = (TextView) findViewById(R.id.txtLink);
        txtLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

