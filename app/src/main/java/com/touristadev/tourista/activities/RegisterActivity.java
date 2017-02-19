package com.touristadev.tourista.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.WelcomeActivity;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.FBfriends;
import com.touristadev.tourista.models.CurrentUser;
import com.touristadev.tourista.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegister;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private AccessToken currentAccessToken;
    private AccessToken accsTok;
    private ArrayList<FBfriends> friendsList = new ArrayList<>();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private FirebaseUser finUser;
    private HttpUtils httpUtils = new HttpUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        final Controllers mControllers = new Controllers();
        //FONTS
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");

        mCallbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                mControllers.addUser(user);
            }
        };
        mRegister = (Button) findViewById(R.id.btnRegister);
        mRegister.setTypeface(myCustomFont);
        mRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegister:
                login();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final String userID = token.getUserId();
        accsTok = token;

        Log.d("chanRegisterActivity",token.toString());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //do nothing
                        } else {
                            CurrentUser.email = user.getEmail();
                            CurrentUser.name = user.getDisplayName();
                            CurrentUser.photoUrl = user.getPhotoUrl().toString();
                            CurrentUser.userFacebookId = accsTok.getUserId();
                            CurrentUser.userFirebaseId = user.getUid();
                            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                            intent.putExtra("firstName", firstName);
                            intent.putExtra("lastName", lastName);
                            intent.putExtra("email", email);
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
                    public void onSuccess(LoginResult loginResult) {
                        currentAccessToken = loginResult.getAccessToken();
                        handleFacebookAccessToken(currentAccessToken);
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    email = object.getString("email");
                                    Controllers.setCurrentUserID(object.getString("id"));
                                    Toast.makeText(getApplicationContext(), "Welcome! " + firstName, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Log.d("Chan", "Exception "+e);
                                }
                                PostCurrentUser posCur = new PostCurrentUser();
                                posCur.execute();
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,birthday,email");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
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

                        JSONArray jarray = new JSONArray();

                        for(int x = 0 ; x < friendsList.size() ; x++){
                            JSONObject j = new JSONObject();
                            try {
                                j.put("userId", user.getUid());
                                j.put("facebookId",friendsList.get(x).getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("chanRegisterActivity",e+"");
                            }
                            jarray.put(j);
                        }


                        PostUserFriends po = new PostUserFriends();
                        po.execute(jarray);

                    }

                }

        ).executeAsync();



    }
    class PostCurrentUser extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            String currentUser = null;;

            JSONObject obj = new JSONObject();
            try {
                obj.put("userId",user.getUid());
                obj.put("facebookId",Controllers.getCurrentUserID());
                obj.put("firstName",firstName);
                obj.put("lastName",lastName);
                obj.put("birthday","2016-02-11");
                obj.put("email",email);
                obj.put("contactNumber","on hold");
                obj.put("tourGuide","false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Controllers.postToDb("api/create-user",obj);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getFriendsList();
            super.onPostExecute(aVoid);
        }
    }
    public class PostUserFriends extends AsyncTask<JSONArray, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(JSONArray... params) {
            Controllers.postToDb("api/post-friends",params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray rt) {

            super.onPostExecute(rt);
        }
    }
}
