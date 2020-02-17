package com.hitmacreed.plugin.instagramcamera;

   
//Native apis
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import java.io.File;


import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//Lib Apis
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.model.Media;


public class Main extends CordovaPlugin {

    private static final int CAPTURE_MEDIA = 368;
    Boolean showPicker = true;
    Boolean enableImageCropping = true;
    int setVideoFileSize = 100;

    private CallbackContext mCallbackContext;
    
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }
   
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        Context context = cordova.getActivity().getApplicationContext();

        try {
            JSONObject options = args.getJSONObject(0);
            
            //GET VALUES
            showPicker = options.getBoolean("showPicker");
            enableImageCropping = options.getBoolean("enableImageCropping");
            setVideoFileSize = options.getInt("setVideoFileSize");
         

        } catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
            return false;
        }
        
        if(action.equals("getPicture")) {
           
           this.launchCamera(context);
           
            return true;
        }
        return false;

    }

    private void launchCamera(Context context) {

        SandriosCamera
        .with()
        .setShowPicker(showPicker)
        .setVideoFileSize(setVideoFileSize)
        .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
        .enableImageCropping(enableImageCropping)
        .launchCamera(cordova.getActivity());
        cordova.setActivityResultCallback(this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       
            if (resultCode == Activity.RESULT_OK  && requestCode == SandriosCamera.RESULT_CODE  && data != null) {

                    if (data.getSerializableExtra(SandriosCamera.MEDIA) instanceof Media) {
                        
                        Media media = (Media) data.getSerializableExtra(SandriosCamera.MEDIA);
                        String path = media.getPath();
                        File imgFile = new File(path);
                        
                        JSONObject objResult = new JSONObject();

                            try {
                                objResult.put("result", imgFile.getAbsolutePath());
                                objResult.put("status", "OK");
                                mCallbackContext.success(objResult);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                      
                        Toast.makeText(webView.getContext(), "Media captured." + "\n"  + media.getPath(), Toast.LENGTH_SHORT).show();

                       
                    }
                }
            
        }
  
}