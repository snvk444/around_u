package aroundu.snvk.com.aroundu_template_change;

import com.google.android.gms.maps.model.LatLng;

public class DisplayInfo {
    LatLng latLng;
    String title;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
