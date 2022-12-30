package com.robinhsueh.nerdia.view.fragments.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.user.LoginInfo;
import com.robinhsueh.nerdia.model.user.UserData;
import com.robinhsueh.nerdia.utils.ConnectionHelper;
import com.robinhsueh.nerdia.utils.ConnectionReceiver;
import com.robinhsueh.nerdia.utils.SharedPreferenceStringLiveData;
import com.robinhsueh.nerdia.utils.SharedPreferenceUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment implements ConnectionReceiver.ReceiverListener {

    private final String LOG_TAG = "ProfileFragment";
    private Context context;

    private SharedPreferences sp;

    private ViewGroup anonymousGroup;
    private ViewGroup profileGroup;
    private TextView profileNameTextView;
    private ImageView profileImage;
    private MaterialButton ratedListBtn;

    private LoginInfo mLoginInfo;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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

        // Initialize loginInfo
        mLoginInfo = SharedPreferenceUtils.getLoginInfoFromSharedPreference(sp);

        // Observe userData SharedPreference livedata
        SharedPreferenceStringLiveData spUserLiveData = new SharedPreferenceStringLiveData(sp, StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_USERDATA_KEY, "");
        spUserLiveData.getStringLiveData(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_USERDATA_KEY, "").observe(this, s -> {
            if (!Strings.isNullOrEmpty(s)) {
                Gson gson = new Gson();
                UserData userData = gson.fromJson(s, UserData.class);
                if (userData != null) {
                    // show login UI
                    showUserUI(userData);
                    return;
                }
            }
            // show not login UI
            showAnonymousUI();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        anonymousGroup = view.findViewById(R.id.group_anonymous);
        profileGroup = view.findViewById(R.id.group_profile);
        profileNameTextView = view.findViewById(R.id.text_profile_name);
        profileImage = view.findViewById(R.id.img_profile);
        MaterialButton loginBtn = view.findViewById(R.id.btn_login);
        MaterialButton logoutBtn = view.findViewById(R.id.btn_logout);
        MaterialButton watchlistBtn = view.findViewById(R.id.btn_watchlist);
        ratedListBtn = view.findViewById(R.id.btn_rated_list);
        TextView versionTextView = view.findViewById(R.id.text_app_version);

        // Set login btn onClick listener
        loginBtn.setOnClickListener(v -> {
            boolean isNetworkConnected = ConnectionHelper.checkConnection(context, this);
            if (isNetworkConnected) { // INTERNET AVAILABLE
                navigateTo(R.id.action_profileFragment_to_loginTmdbFragment);
            } else { // NO INTERNET AVAILABLE
                showSnackBar(view, getString(R.string.msg_no_internet_available), context.getColor(R.color.white), context.getColor(R.color.red));
            }
        });

        // Set logout btn onClick listener
        logoutBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.label_logout);
            builder.setMessage(R.string.label_logout_alert_message);
            builder.setPositiveButton(R.string.label_ok, (dialog, id) -> {
                // User clicked OK button

                // Clear the session & user data stored in sharedPreference
                logoutSharedPreference();
                // Show the snackbar message
                showSnackBar(view, getString(R.string.msg_logout_successful), context.getColor(R.color.white), context.getColor(R.color.green));
            });
            builder.setNegativeButton(R.string.label_cancel, (dialog, id) -> {
                // User cancelled the dialog, do nothing
            });

            // Create the AlertDialog
            builder.show();
        });


        // Set FragmentResultListener to observe login result in other fragment is successful or not
        getParentFragmentManager().setFragmentResultListener(StaticParameter.ExtraDataKey.EXTRA_DATA_LOGIN_KEY, this, (requestKey, result) -> {
            // Get login result
            boolean isSuccess = result.getBoolean(StaticParameter.ExtraDataKey.EXTRA_DATA_LOGIN_IS_SUCCESS_KEY, false);
            // Show the Snackbar message
            String msg = isSuccess ? getString(R.string.msg_login_successful) : getString(R.string.msg_login_failed);
            int bgColor = isSuccess ? context.getColor(R.color.green) : context.getColor(R.color.red);
            showSnackBar(view, msg, context.getColor(R.color.white), bgColor);
        });

        // Set watchlist btn onClick listener
        watchlistBtn.setOnClickListener(v -> {
            navigateTo(R.id.action_profileFragment_to_watchlistTab);
        });

        // Set ratedList btn onClick listener
        ratedListBtn.setOnClickListener(v -> {
            navigateTo(R.id.action_profileFragment_to_ratedListFragment);
        });

        // Set app version text
        PackageManager pm = context.getPackageManager();
        String pkgName = context.getPackageName();
        try {
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, 0);
            String versionName = pkgInfo.versionName;
            versionTextView.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(LOG_TAG, "PackageNameNotFound: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show the UI when user is already login
     *
     * @param userData User data
     */
    private void showUserUI(UserData userData) {
        // hide the anonymous viewgroup
        anonymousGroup.setVisibility(View.GONE);

        // set the profile name
        profileNameTextView.setText(userData.getAccount());

        // set profile image
        String gravatarUrl = StaticParameter.getGravatarImageUrl(userData.getAvatar().gravatar.hash, 80);
        Glide.with(context)
                .load(gravatarUrl)
                .placeholder(R.drawable.ic_profile)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(profileImage);

        // show the profile viewgroup
        profileGroup.setVisibility(View.VISIBLE);

        // enable ratedListBtn
        ratedListBtn.setEnabled(true);
        ratedListBtn.setText(R.string.label_rated_list);
    }

    /**
     * Show the UI when user not login
     */
    private void showAnonymousUI() {
        // hide the profile viewgroup
        profileGroup.setVisibility(View.GONE);
        // show the anonymous viewgroup
        anonymousGroup.setVisibility(View.VISIBLE);
        // disable ratedListBtn
        ratedListBtn.setEnabled(false);
        ratedListBtn.setText(R.string.label_rated_list_not_login);
    }

    /**
     * Clear the session & user data stored in sharedPreference when logout
     */
    private void logoutSharedPreference() {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_SESSION_KEY, "");
        spEditor.putString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_USERDATA_KEY, "");
        spEditor.apply();
    }

    /**
     * Navigate to specific fragment
     *
     * @param actionId action resource id
     */
    private void navigateTo(int actionId) {
        NavHostFragment.findNavController(this)
                .navigate(actionId);
    }

    /**
     * Show SnackBar Message
     *
     * @param view     The view to find a parent from
     * @param msg      Message text
     * @param txtColor Text Color
     * @param bgColor  Background Color
     */
    private void showSnackBar(View view, String msg, int txtColor, int bgColor) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setTextColor(txtColor)
                .setBackgroundTint(bgColor)
                .setAnchorView(R.id.bottomNavView) // set snackbar above bottomNav
                .show();
    }

    /**
     * Triggered when network state change
     *
     * @param isConnected connection status
     */
    @Override
    public void onNetworkChange(boolean isConnected) {

    }


}