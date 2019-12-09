package lab1;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AirServices {
    private ArrayList<Airline> airlines;
    private ArrayList<Flight> flights;

    private DocumentBuilderFactory dbf;

    public AirServices() throws SAXException {
        airlines = new ArrayList<>();
        flights = new ArrayList<>();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema s = sf.newSchema(new File("lab1-airservices.xsd"));

        dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setSchema(s);
    }

    public void saveToFile(String filename) throws Exception {
        DocumentBuilder db = null;
        Document doc = null;
        db = dbf.newDocumentBuilder();

        doc = db.newDocument();

        Element root = doc.createElement("airservices");
        doc.appendChild(root);

        Map<Airline, Element> airlineElMap = new HashMap<>();

        for (Airline airline : airlines) {
            Element airlineElement = airline.serialize(doc);
            root.appendChild(airlineElement);
            airlineElMap.put(airline, airlineElement);
        }

        for (Flight flight : flights) {
            Element flightElement = flight.serialize(doc);
            airlineElMap.get(flight.airline).appendChild(flightElement);
        }

        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(filename));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, fileResult);
    }

    public void loadFromFile(String filename) throws Exception {
        airlines.clear();
        flights.clear();

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();

        Document doc = null;
        doc = db.parse(new File(filename));

        Element root = doc.getDocumentElement();
        if (root.getTagName().equals("airservices")) {
            NodeList listAirlines = root.getElementsByTagName("airline");

            for (int i = 0; i < listAirlines.getLength(); i++) {
                Element airlineElement = (Element) listAirlines.item(i);
                Airline airline = new Airline(airlineElement);
                airlines.add(airline);

                NodeList listFlights = airlineElement.getElementsByTagName("flight");

                for (int j = 0; j < listFlights.getLength(); j++) {
                    Element flightElement = (Element) listFlights.item(j);
                    Flight flight = new Flight(flightElement, airline);
                    flights.add(flight);
                }
            }
        }
    }

    public void addAirline(int code, String name) throws Exception {
        if (getAirline(code) != null) throw new Exception("Airline with code " + code + " already exists");
        airlines.add(new Airline(code, name));
    }

    public Airline getAirline(int code) {
        for (Airline airline : airlines) {
            if (airline.code == code) return airline;
        }
        return null;
    }

    public Airline getAirlineInd(int index) {
        return airlines.get(index);
    }

    public ArrayList<Airline> getAirlines() {
        return airlines;
    }

    public int countAirlines() {
        return airlines.size();
    }

    public void deleteAirline(int code) throws Exception {
        Airline airline = getAirline(code);
        if (airline == null) throw new Exception("Cannot delete airline with code " + code + " as it doesn't exist");
        flights.removeIf(f -> f.airline == airline);
        airlines.remove(airline);
    }

    public void addFlight(int code, String name, String airportFrom, String airportTo,
                          String aircraft, String departure, String arrival,
                          int airlineCode) throws Exception {
        if (getFlight(code) != null) throw new Exception("Flight with code " + code + " already exists");

        Airline airline = getAirline(airlineCode);
        if (airline == null)
            throw new Exception("Cannot add flight with code " + code + " because airline with code " + airlineCode + " doesn't exist");

        DateFormat df = new SimpleDateFormat(Flight.DATE_FORMAT);

        Flight flight = new Flight(code, name, airportFrom, airportTo,
                aircraft, df.parse(departure), df.parse(arrival),
                airline);

        flights.add(flight);
    }

    public Flight getFlight(int code) {
        for (Flight flight : flights) {
            if (flight.code == code) return flight;
        }
        return null;
    }

    public Flight getFlightInd(int index) {
        return flights.get(index);
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public ArrayList<Flight> getFlightsByAirline(int airlineCode) {
        Airline airline = getAirline(airlineCode);
        ArrayList<Flight> result = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.airline == airline) {
                result.add(flight);
            }
        }
        return result;
    }

    public int countFlights() {
        return flights.size();
    }

    public void deleteFlight(int code) throws Exception {
        Flight flight = getFlight(code);
        if (flight == null) throw new Exception("Cannot delete flight with code " + code + " as it doesn't exist");
        flights.remove(flight);
    }
}
