package aroundu.snvk.com.uaroundu.beans;

public class IdentifierBusInfo {
    public String lineid;
    public String busno;
    public String sourceLocation;
    public String destinationLocation;
    public int direction;
    public int sequence;
    public String stop_name;
    public double latitude;
    public double longitude;

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public IdentifierBusInfo(){}


    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid;
    }

    public IdentifierBusInfo(String lineid, String busno, String source_station, String destination_station, int direction, int sequence) {
        this.lineid = lineid;
        this.busno = busno;
        this.sourceLocation = source_station;
        this.destinationLocation = destination_station;
        this.direction = direction;
        this.sequence = sequence;
    }



    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
