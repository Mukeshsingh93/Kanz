package com.example.myapplication.paymentgateway;

/**
 * Used as a callback to send the encoded data back from
 *
 * @author Matt Allen
 */
public interface OnTokenEncodedCallback
{
	void onTokenCreated(String token);
}
