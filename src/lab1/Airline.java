package lab1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Airline {
    public int code;
    public String name;

    public Airline(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Airline(Element airline) {
        this.code = Integer.parseInt(airline.getAttribute("id"));
        this.name = airline.getAttribute("name");
    }

    public Element serialize(Document doc) {
        Element airline = doc.createElement("airline");
        airline.setAttribute("id", String.valueOf(this.code));
        airline.setAttribute("name", this.name);
        return airline;
    }
}
