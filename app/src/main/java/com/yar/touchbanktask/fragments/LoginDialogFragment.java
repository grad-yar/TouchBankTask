package com.yar.touchbanktask.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yar.touchbanktask.R;
import com.yar.touchbanktask.rest.InstagramEndpoints;


public class LoginDialogFragment extends DialogFragment {


    private WebView mMWebView;

    private LoginListener mLoginListener;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mLoginListener = (LoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + LoginListener.class.getName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);

        mMWebView = new WebView(activity);
        mMWebView.setVerticalScrollBarEnabled(false);
        mMWebView.setHorizontalScrollBarEnabled(false);
        mMWebView.setWebViewClient(new TokenGrabberClient());
        mMWebView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );

        mMWebView.loadUrl(InstagramEndpoints.AUTH_URL);

        setCancelable(false);

        return adb.setTitle(R.string.dialog_title_authorize)
                .setView(mMWebView)
                .setNegativeButton(R.string.dialog_cancel_text, (dialog, which) -> {
                    mMWebView.stopLoading();
                    dialog.cancel();
                })
                .create();
    }

    public interface LoginListener {
        void onLoginComplete(String token);

        void onLoginError();
    }

    private class TokenGrabberClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return getTokenFromUri(uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            return getTokenFromUri(uri);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            onRequestError(view, errorCode, description, failingUrl);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            onRequestError(view, error.getErrorCode(),
                    error.getDescription().toString(), request.getUrl().toString());
        }

        private void onRequestError(WebView view, int errorCode, String description, String failingUrl) {
            mLoginListener.onLoginError();
            dismiss();
        }

        private boolean getTokenFromUri(final Uri uri) {
            String uriStr = uri.toString();

            // get access token
            if (uriStr.startsWith(InstagramEndpoints.REDIRECT_URI + "/#access_token=")) {
                String token = uriStr.substring(uriStr.indexOf("=") + 1, uriStr.length());

                if (!token.isEmpty()) {
                    mLoginListener.onLoginComplete(token);
                } else {
                    mLoginListener.onLoginError();
                }

                dismiss();

                return true;
            }

            return false;
        }
    }
}
