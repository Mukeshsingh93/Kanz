package com.example.myapplication.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.stripe.android.PaymentConfiguration;

public class MultiLanguageApp extends Application {

  private String paymentIntentClientSecret;

  @Override
  public void onCreate() {
    super.onCreate();
    PaymentConfiguration.init(
            getApplicationContext(),
            "pk_test_jeWG4fssBoPH8Oec89kvhUIW00JoqCiBKm"
    );
    startCheckout();
  }

  private void startCheckout() {
    // Request a PaymentIntent from your server and store its client secret in paymentIntentClientSecret
    // Click Open on GitHub to see a full implementation
  }



  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(LocaleManager.setLocale(base));
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    LocaleManager.setLocale(this);
  }
}
