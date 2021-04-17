package com.amsy.mobileoffloading.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amsy.mobileoffloading.R;
import com.amsy.mobileoffloading.entities.ConnectedDevice;
import com.amsy.mobileoffloading.helper.Constants;

import java.util.List;

public class ConnectedDevicesAdapter extends RecyclerView.Adapter<ConnectedDevicesAdapter.ViewHolder>{

    private Context context;
    private List<ConnectedDevice> connectedDevices;

    public ConnectedDevicesAdapter(@NonNull Context context, List<ConnectedDevice> connectedDevices) {
        this.context = context;
        this.connectedDevices = connectedDevices;
    }

    @NonNull
    @Override
    public ConnectedDevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedDevicesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ClientId;
        private TextView BatteryLevel;
        private TextView RequestStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ClientId = itemView.findViewById(R.id.client_id);
            BatteryLevel = itemView.findViewById(R.id.battery_level);
            RequestStatus = itemView.findViewById(R.id.request_status);
        }

        public void setClientId(String endpointId, String endpointName) {
            this.ClientId.setText(endpointId + " (" + endpointName + ")");
        }

        public void setBatteryLevel(int batteryLevel) {
            if (batteryLevel > 0 && batteryLevel <= 100) {
                this.BatteryLevel.setText(batteryLevel + "%");
            } else {
                this.BatteryLevel.setText("- %");
            }
        }

        public void setRequestStatus(String requestStatus) {
            if (requestStatus.equals(Constants.RequestStatus.ACCEPTED)) {
                this.RequestStatus.setText("(request accepted)");
            } else if (requestStatus.equals(Constants.RequestStatus.REJECTED)) {
                this.RequestStatus.setText("(request rejected)");
            } else {
                this.RequestStatus.setText("(request pending)");
            }
        }
    }
}