package com.amsy.mobileoffloading.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amsy.mobileoffloading.callback.ClientConnectionListener;
import com.amsy.mobileoffloading.callback.PayloadListener;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import java.util.HashSet;

public class NearbyConnectionsManager {

    private static NearbyConnectionsManager nearbyConnectionsManager;
    private Context context;

    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private HashSet<ClientConnectionListener> clientConnectionListenerSet = new HashSet<>();

    private HashSet<PayloadListener> payloadListenersSet = new HashSet<>();

    public NearbyConnectionsManager(Context context) {
        this.context = context;
        this.connectionLifecycleCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                for (ClientConnectionListener clientConnectionListener : clientConnectionListenerSet) {
                    try {
                        clientConnectionListener.onConnectionInitiated(endpointId, connectionInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution connectionResolution) {
                for (ClientConnectionListener clientConnectionListener : clientConnectionListenerSet) {
                    try {
                        clientConnectionListener.onConnectionResult(endpointId, connectionResolution);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDisconnected(@NonNull String endpointId) {
                Toast.makeText(context, "DISCONNECTED", Toast.LENGTH_SHORT).show();
//                NearbyConnectionsManager.getInstance(context).rejectConnection(endpointId);
                for (ClientConnectionListener clientConnectionListener : clientConnectionListenerSet) {
                    try {
                        clientConnectionListener.onDisconnected(endpointId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public static NearbyConnectionsManager getInstance(Context context) {
        if (nearbyConnectionsManager == null) {
            nearbyConnectionsManager = new NearbyConnectionsManager(context);
        }

        return nearbyConnectionsManager;
    }
    public void requestConnection(String endpointId, String clientId) {
        Nearby.getConnectionsClient(context)
                .requestConnection(clientId, endpointId, connectionLifecycleCallback)
                .addOnSuccessListener(unused -> {
                    Log.d("OFLOD", "CONNECTION REQUESTED");
                })
                .addOnFailureListener((Exception e) -> {
                    Log.d("OFLOD", "CONNECTION FAILED");
                    e.printStackTrace();
                });
    }

    public void acceptConnection(String endpointId) {
        Nearby.getConnectionsClient(context).acceptConnection(endpointId, new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                for (PayloadListener payloadListener : payloadListenersSet) {
                    try {
                        payloadListener.onPayloadReceived(endpointId, payload);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

            }
        });
    }
}