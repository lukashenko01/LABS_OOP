package db;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbFlight implements Serializable {
    public final static String DATE_FORMAT = "YYYY-MM-DD HH:MM:SS";

    public int code;
    public int airline_id;
    public String name;
    public String airportFrom;
    public String airportTo;
    public String aircraft;
    public Date departure;
    public Date arrival;

    public DbFlight(int code, int airline_id, String name, String airportFrom, String airportTo,
                    String aircraft, Date departure, Date arrival) {
        this.code = code;
        this.airline_id = airline_id;
        this.name = name;
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.aircraft = aircraft;
        this.departure = departure;
        this.arrival = arrival;
    }


    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return "Flight{" +
                "code=" + code +
                ", airline_id=" + airline_id +
                ", name='" + name + '\'' +
                ", airportFrom='" + airportFrom + '\'' +
                ", airportTo='" + airportTo + '\'' +
                ", aircraft='" + aircraft + '\'' +
                ", departure=" + df.format(departure) +
                ", arrival=" + df.format(arrival) +
                '}';
    }
}
