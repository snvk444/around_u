package aroundu.snvk.com.uaroundu.beans;

public class BusLinesData {
    public int line_id;
    public String bus_no;
    public String source_station;
    public String destination_station;
    public int direction;
    public int sequence;

    public BusLinesData(int line_id, String bus_no, String source_station, String destination_station, int direction, int sequence) {
        this.line_id = line_id;
        this.bus_no = bus_no;
        this.source_station = source_station;
        this.destination_station = destination_station;
        this.direction = direction;
        this.sequence = sequence;
    }

    public BusLinesData() {

    }

    public int getLine_id() {
        return line_id;
    }

    public void setLine_id(int line_id) {
        this.line_id = line_id;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getSource_station() {
        return source_station;
    }

    public void setSource_station(String source_station) {
        this.source_station = source_station;
    }

    public String getDestination_station() {
        return destination_station;
    }

    public void setDestination_station(String destination_station) {
        this.destination_station = destination_station;
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
