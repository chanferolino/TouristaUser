package com.touristadev.tourista.activities;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.facebook.rebound.ui.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.FBfriends;
import com.touristadev.tourista.models.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegister, mSignIn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private Controllers mControllers = new Controllers();
    private AccessToken accsTok;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    GoogleAccountCredential mFinalCredential;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private ArrayList<FBfriends> friendsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        mFinalCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList("https://www.googleapis.com/auth/calendar"))
                .setBackOff(new ExponentialBackOff());
        startActivityForResult(
                mFinalCredential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");

        mRegister = (Button) findViewById(R.id.btnRegister);
        mSignIn = (Button) findViewById(R.id.btnLogin);

        mRegister.setTypeface(myCustomFont);
        mSignIn.setTypeface(myCustomFont);
        TextView t;
        t = (TextView) findViewById(R.id.txtPrice);
        System.out.println(t);





        mRegister.setOnClickListener(this);
        mSignIn.setOnClickListener(this);


        mCallbackManager = CallbackManager.Factory.create();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                mControllers.addUser(user);


            }
        };
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                login();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null &&
                data.getExtras() != null) {
            String accountName =
                    data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                SharedPreferences settings =
                        getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_NAME, accountName);
                editor.apply();
                mFinalCredential.setSelectedAccountName(accountName);
                Controllers con = new Controllers();
                con.setCredentials(mFinalCredential);
            }


        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        accsTok = token;
        getFriendsList();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //do nothing
                        } else {
                            Controllers con = new Controllers();
                            Intent intent = new Intent(LoginActivity.this, ExploreActivity.class);
                            CurrentUser.email = user.getEmail();
                            CurrentUser.name = user.getDisplayName();
                            CurrentUser.photoUrl = user.getPhotoUrl().toString();
                            CurrentUser.userFacebookId = accsTok.getUserId();
                            CurrentUser.userFirebaseId = user.getUid();

                            con.setCurrentUserID(user.getUid());
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void login() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }
                    @Override
                        public void onError(FacebookException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            if (e instanceof FacebookAuthorizationException) {
                                if (AccessToken.getCurrentAccessToken() != null) {
                                    LoginManager.getInstance().logOut();

                                }
                            }
                        }

                });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }
    public void getFriendsList(){
       /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+accsTok.getUserId()+"/friends/",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String id,name,email;
                        boolean ins;

                        try {
                            JSONObject json = new JSONObject(response.getRawResponse());
                            JSONArray jarray = json.getJSONArray("data");
                            for(int i = 0; i < jarray.length(); i++){
                                JSONObject jsonFriend = jarray.getJSONObject(i);
                                FBfriends newFriend = new FBfriends(jsonFriend.get("id").toString(),jsonFriend.get("name").toString());

                                friendsList.add(newFriend);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        ).executeAsync();
    }

}
