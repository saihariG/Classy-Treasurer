package com.classyinc.classytreasurer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.classyinc.classytreasurer.confg.Confg;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class PayActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config= new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION) //use Sandbox to test
            .clientId(Confg.PAYPAL_CLIENT_ID) ;

    Button btnPay;
    EditText edtpayAmount;
    String amount = " ";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        //start paypal service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        btnPay = findViewById(R.id.btn_pay_id);
        edtpayAmount = findViewById(R.id.edt_pay_id);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                amount = edtpayAmount.getText().toString();
                if(TextUtils.isEmpty(amount)){
                    edtpayAmount.setError("Empty Field...!");
                }
                if(!TextUtils.isEmpty(amount))
                processPayment();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processPayment() {

        Locale current = getResources().getConfiguration().locale;

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                "Invest in Classy Inc",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent =new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation =
                        Objects.requireNonNull(data).getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {

                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } else if (requestCode == Activity.RESULT_CANCELED) {

                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}
