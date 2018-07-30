package aroundu.snvk.com.aroundu_template_change;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class LocationInfo implements ClusterItem {
    private double latitude;
    private double longitude;
    private long time_stamp;
    private LatLng mPosition;
    private static final String TAG = "Loc_Class";

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTime_stamp(long time_stamp) {
        Log.i(TAG, String.valueOf(time_stamp));
        this.time_stamp = time_stamp;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public LatLng getPosition() {
        mPosition = new LatLng(latitude, longitude);
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

//    public LocationInfo(double lat, double lng) {
//        mPosition = new LatLng(lat, lng);
//        }
//
//    public LocationInfo(double lat, double lng, long time_stamp) {
//        this.latitude = lat;
//        this.longitude = lng;
//        this.time_stamp = time_stamp;
//    }
}