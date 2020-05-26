package com.classyinc.classytreasurer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId,txtAMount,txtStatus;
    Button GOhome;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txt_payment_id);
        txtAMount = findViewById(R.id.txt_payment_amount);
        txtStatus = findViewById(R.id.txt_payment_status);
        GOhome =findViewById(R.id.btn_GOHOME_id);

        GOhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentDetails.this,HomeActivity.class));
            }
        });
        //get intent
        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(intent.getStringExtra("PaymentDetails")));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response, String paymentAmount) {

        try {
            txtId.setText(response.getString("id"));
            txtAMount.setText("$"+paymentAmount);
            txtStatus.setText(response.getString("state"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}
