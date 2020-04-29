package com.example.myapplication.wishlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckOutActivity extends AppCompatActivity {

    private static final String BACKEND_URL = "https://kanz.app/demo/api/";
//    private static final String BACKEND_URL = "http://flatnvilla.com/testing/";

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret="sk_test_gozruiZ7Qi6ZSDAzR6QmESXF00bKdNSFth";
    private Stripe stripe;
    public CardInputWidget cardInputWidget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_checkout);
        cardInputWidget= (CardInputWidget)findViewById(R.id.cardInputWidget);
      startCheckout();
   /*     PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_jeWG4fssBoPH8Oec89kvhUIW00JoqCiBKm"
        );*/
       /* Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            doSome();
        });*/
    }

    public void doSome()
    {
        PaymentMethodCreateParams paymentMethodParams = cardInputWidget.getPaymentMethodCreateParams();
        if (paymentMethodParams != null) {
            stripe.createPaymentMethod(
                    paymentMethodParams,
                    new ApiResultCallback<PaymentMethod>() {
                        @Override
                        public void onSuccess(@NonNull PaymentMethod paymentMethod) {
                            // Send the ID of the PaymentMethod to your server.
                            final String paymentMethodId = paymentMethod.id;
                        }

                        @Override
                        public void onError(@NonNull Exception e) {
                            // Display the error to the customer.
                        }
                    });
        }
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the sample server's /create-payment-intent endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        String json = "{"
                + "\"currency\":\"usd\","
                + "\"items\":["
                + "{\"id\":\"photo_subscription\"}"
                + "]"
                + "}";
        RequestBody body = RequestBody.create(mediaType, json);

        Log.e("PAYMENTCHECKOUT","body to strong....."+body.toString());
        Request request = new Request.Builder()
                .url(BACKEND_URL + "payment/check")
//                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(CheckOutActivity.this, confirmParams);
            }
        });
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message,
                              boolean restartDemo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        if (restartDemo) {
            builder.setPositiveButton("Restart demo",
                    (DialogInterface dialog, int index) -> {
                        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                        cardInputWidget.clear();
                        startCheckout();
                    });
        } else {
            builder.setPositiveButton("Ok", null);
        }
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {

        Log.e("PAYMENTCHECKOUT","checkout the data...."+response.toString());
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Log.e("PAYMENTCHECKOUT","checkout the data...."+type.toString());

        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );

        Log.e("PAYMENTCHECKOUT","responseMap the data...."+responseMap.toString());


        // The response from the server includes the Stripe publishable key and
        // PaymentIntent details.
        // For added security, our sample app gets the publishable key from the server
        String stripePublishableKey = responseMap.get("pk_test_jeWG4fssBoPH8Oec89kvhUIW00JoqCiBKm");
        paymentIntentClientSecret = responseMap.get("sk_test_gozruiZ7Qi6ZSDAzR6QmESXF00bKdNSFth");

        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull(stripePublishableKey)
        );
    }

    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckOutActivity> activityRef;
        PayCallback(@NonNull CheckOutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckOutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("PAYMENTCHECKOUT","onFailure the data...."+e.toString());

            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckOutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {

                Log.e("PAYMENTCHECKOUT","response is not successfull the data...."+response.toString());

                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }
    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckOutActivity> activityRef;
        PaymentResultCallback(@NonNull CheckOutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckOutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent),
                        true
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(),
                        false
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final CheckOutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString(), false);
        }
    }
}
