package com.robinhsueh.nerdia.view.fragments.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.auth.RequestTokenResponse;
import com.robinhsueh.nerdia.model.user.UserData;
import com.robinhsueh.nerdia.utils.SharedPreferenceUtils;
import com.robinhsueh.nerdia.viewmodel.LoginTmdbViewModel;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.util.Date;

public class LoginTmdbFragment extends Fragment{
    private final String LOG_TAG = "LoginTmdbFragment";
    private Context context;

    private WebView webView;
    private ProgressBar progressBar;
    private LoginTmdbViewModel viewModel;

    private SharedPreferences sp;
    private Date tokenExpiredDate = new Date(Long.MIN_VALUE);
    private String existedToken;

    // Define UriMatcher
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StaticParameter.AuthLocalValidUrlAuthority, StaticParameter.AuthLocalValidUrlPath, 100);
    }


    public LoginTmdbFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginTmdbFragment newInstance() {
        LoginTmdbFragment fragment = new LoginTmdbFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        // Get SharedPreference file
        sp = SharedPreferenceUtils.getOrCreateSharedPreference(StaticParameter.SharedPreferenceFileKey.SP_FILE_TMDB_KEY, context);

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(LoginTmdbViewModel.class);

        // Observe the token request
        viewModel.getRequestTokenLiveData().observe(this, requestTokenResponse -> {
            if (requestTokenResponse.isSuccess()) {
                existedToken = requestTokenResponse.getToken();
                webView.loadUrl(String.format(StaticParameter.TmdbAuthFormatUrl, existedToken));
                // Save the token into sharedPreference
                saveToken(sp, requestTokenResponse);
            }
        });

        // Observe the user session
        viewModel.getSessionLiveData().observe(this, sessionResponse -> {
            if (sessionResponse.isSuccess() && !Strings.isNullOrEmpty(sessionResponse.getSession())) {
                // Save the session into sharedPreference
                saveSession(sp, sessionResponse.getSession());
                // Once get the session, then get userData
                viewModel.getUserData(sessionResponse.getSession());
            }
        });

        // Observe the userData
        viewModel.getUserLiveData().observe(this, userData -> {
            // Save the userData into sharedPreference
            saveUserData(sp, userData);
            // Hide progressBar
            progressBar.setVisibility(View.GONE);
            // Navigate back to profile page, login successful
            navigateBackToProfile(true);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_tmdb, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        webView = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar);


        // Enable the javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // Prevent WebViews with dangerous settings from loading local files
        webView.getSettings().setAllowFileAccess(false);
        // WebViewClient allows you to handle onPageFinished and override Url loading
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                if (URI_MATCHER.match(url) == 100) { // nerdia://auth?request_token=[xxx]&approved=true
                    // Get query parameters
                    String approved = url.getQueryParameter("approved") != null ? url.getQueryParameter("approved") : "false";
                    String approvedToken = url.getQueryParameter("request_token");
                    // Check approved parameter
                    if (approved.equalsIgnoreCase("true") && !Strings.isNullOrEmpty(approvedToken)) {
                        // Hide webview & show progressBar
                        webView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        // Create session using approved token
                        viewModel.createSession(approvedToken);
                        return true;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.label_authorization_denied);
                        builder.setPositiveButton(R.string.label_retry, (dialog, id) -> {
                            // User clicked RETRY button
                            viewModel.requestNewToken();
                        });
                        builder.setNegativeButton(R.string.label_cancel, (dialog, id) -> {
                            // User cancelled the dialog, login failed
                            navigateBackToProfile(false);
                        });
                        // Prevent dialog from getting dismissed on back key pressed & touch outside dialog
                        builder.setCancelable(false);

                        // Create the AlertDialog
                        builder.show();
                        return true;
                    }
                }
                Log.d(LOG_TAG, url.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

        });

        // Request new token
        viewModel.requestNewToken();
    }

    /**
     * Navigate back and pass pass the login result
     */
    private void navigateBackToProfile(boolean isSuccessful) {
        Bundle result = new Bundle();
        result.putBoolean(StaticParameter.ExtraDataKey.EXTRA_DATA_LOGIN_IS_SUCCESS_KEY, isSuccessful);
        getParentFragmentManager().setFragmentResult(StaticParameter.ExtraDataKey.EXTRA_DATA_LOGIN_KEY, result);
        // Navigate back to profileFragment
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginTmdbFragment_to_profileFragment);
    }

    /**
     * Save Token into Shared Preference
     *
     * @param sharedPreferences target Shared Preference
     * @param tokenResponse     RequestTokenResponse object
     */
    private void saveToken(SharedPreferences sharedPreferences, RequestTokenResponse tokenResponse) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(tokenResponse);
            spEditor.putString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_TOKEN_KEY, json);
            spEditor.apply();
        }
    }

    /**
     * Save Session into Shared Preference
     *
     * @param sharedPreferences target Shared Preference
     * @param session           User Session
     */
    private void saveSession(SharedPreferences sharedPreferences, String session) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_SESSION_KEY, session);
            spEditor.apply();
        }
    }


    /**
     * Save UserData into Shared Preference
     *
     * @param sharedPreferences target Shared Preference
     * @param userData          User Data
     */
    private void saveUserData(SharedPreferences sharedPreferences, UserData userData) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(userData);
            spEditor.putString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_USERDATA_KEY, json);
            spEditor.apply();
        }
    }

}