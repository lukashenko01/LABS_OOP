package db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbFlight implements Serializable {
    public final static String DATE_FORMAT = "YYYY-MM-dd HH:MM:SS";

    public int code;
    public int airline_id;
    public String name;
    public String airportFrom;
    public String airportTo;
    public String aircraft;
    public Date departure;
    public Date arrival;

    private static final DateFormat df = new SimpleDateFormat(DATE_FORMAT);

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


    public DbFlight(DataInputStream in) throws Exception {
        code = in.readInt();
        airline_id = in.readInt();
        name = in.readUTF();
        airportFrom = in.readUTF();
        airportTo = in.readUTF();
        aircraft = in.readUTF();
        departure = df.parse(in.readUTF());
        arrival = df.parse(in.readUTF());
    }

    public void serialize(DataOutputStream out) throws Exception {
        out.writeInt(code);
        out.writeInt(airline_id);
        out.writeUTF(name);
        out.writeUTF(airportFrom);
        out.writeUTF(airportTo);
        out.writeUTF(aircraft);
        out.writeUTF(df.format(departure));
        out.writeUTF(df.format(arrival));
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
