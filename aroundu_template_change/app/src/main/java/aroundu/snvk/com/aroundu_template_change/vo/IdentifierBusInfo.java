package aroundu.snvk.com.aroundu_template_change.vo;

public class IdentifierBusInfo {
    public int lineid;
    public String busno;
    public String sourceLocation;
    public String destinationLocation;
    public int direction;
    public int sequence;

    public IdentifierBusInfo(){}


    public IdentifierBusInfo(int lineid, String busno, String source_station, String destination_station, int direction, int sequence) {
        this.lineid = lineid;
        this.busno = busno;
        this.sourceLocation = source_station;
        this.destinationLocation = destination_station;
        this.direction = direction;
        this.sequence = sequence;
    }


    public int getLineid() {
        return lineid;
    }

    public void setLineid(int lineid) {
        this.lineid = lineid;
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
