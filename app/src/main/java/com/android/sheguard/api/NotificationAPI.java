package com.android.sheguard.api;

import com.android.sheguard.model.NotificationSenderModel;
import com.android.sheguard.util.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAuFD7zRc:APA91bHLOzU7j2I6CLI1Gr8oo1fiXH0kDy3IhiiWkWFDB5Ckf6lbGuEbPontfWV0B-19PC1jz7AEDEVVtCigfpi5UV6sz6RamZymv-I37OHcK67AUl40HU0ZAyCGcC02ES8IjTAq8Kpg" // Replace with your server key from Firebase Console
            }
    )

    @POST("fcm/send")
    Call<NotificationResponse> sendNotification(@Body NotificationSenderModel body);
}
