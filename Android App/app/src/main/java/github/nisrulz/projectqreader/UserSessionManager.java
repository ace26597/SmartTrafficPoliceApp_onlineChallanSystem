package github.nisrulz.projectqreader;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class UserSessionManager {
     
    // Shared Preferences reference
    SharedPreferences pref;
     
    // Editor reference for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREFER_NAME = "ShopPref";
     
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
     
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

  //User project id (make variable public to access from outside)
    public static final String KEY_ID = "id";

    public static final String KEY_STATUS= "status";
    public static final String KEY_ADD= "add";


    public static final String KEY_GCMID = "gcmid";
    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    //Create login session
    public void createUserLoginSession(String name, String email,String id,String status,String add){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, name);
         
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing email in pref
        editor.putString(KEY_STATUS, status);

        // Storing id in pref
        editor.putString(KEY_ID, id);


        // Storing id in pref
        editor.putString(KEY_ADD, add);
        // commit changes
        editor.commit();
    }  
     
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){
             
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainLoginPage.class);
             
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
             
            return true;
        }
        return false;
    }

    //Create login session
    public void createGCMID(String id){
        // Storing id in pref
        editor.putString(KEY_GCMID, id);
        // commit changes
        editor.commit();
    }

    //Create login session
    public String geteGCMID(){
        // Storing id in pref
        return  pref.getString(KEY_GCMID, "");
    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
         
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
         
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
         
        // user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        
      //user id
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        //user status
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));

        //user status
        user.put(KEY_ADD, pref.getString(KEY_ADD, null));
        // return user
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
         
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, MainLoginPage.class);
         
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
     
     
    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }


    // Check for login
    public String chechstatus(){
        return pref.getString(KEY_STATUS, "0");
    }
}