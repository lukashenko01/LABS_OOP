package lab1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight {
    public final static String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    public int code;
    public String name;
    public String airportFrom;
    public String airportTo;
    public String aircraft;
    public Date departure;
    public Date arrival;
    public Airline airline;

    private static final DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    public Flight(int code, String name, String airportFrom, String airportTo,
                  String aircraft, Date departure, Date arrival,
                  Airline airline) {
        this.code = code;
        this.name = name;
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.aircraft = aircraft;
        this.departure = departure;
        this.arrival = arrival;
        this.airline = airline;
    }

    public Flight(Element flight, Airline airline) throws ParseException {
        this.code = Integer.parseInt(flight.getAttribute("id"));
        this.name = flight.getAttribute("name");
        this.airportFrom = flight.getAttribute("airportFrom");
        this.airportTo = flight.getAttribute("airportTo");
        this.departure = df.parse(flight.getAttribute("departure"));
        this.arrival = df.parse(flight.getAttribute("arrival"));
        this.aircraft = flight.getAttribute("aircraft");
        this.airline = airline;
    }

    public Element serialize(Document doc) {
        Element flight = doc.createElement("flight");
        flight.setAttribute("id", String.valueOf(this.code));
        flight.setAttribute("name", this.name);
        flight.setAttribute("airportFrom", this.airportFrom);
        flight.setAttribute("airportTo", this.airportTo);
        flight.setAttribute("departure", df.format(this.departure));
        flight.setAttribute("arrival", df.format(this.arrival));
        flight.setAttribute("aircraft", this.aircraft);
        return flight;
    }
}
