package lab6;

import db.DbAirServices;
import db.DbConfigs;
import db.DbFlight;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
public class MainController {
    DbAirServices services;

    public MainController() throws Exception {
        services = new DbAirServices();
        services.connect(DbConfigs.URL,DbConfigs.USER, DbConfigs.PASSWORD);
    }
    
    @RequestMapping("/")
    public String index() {
        StringBuilder builder = new StringBuilder();
        builder.append("<head>");
        builder.append("<title>Flights Table</title>");
        builder.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<h1>" + "Flights Table:" + "</h1>");
        builder.append("<table class=\"table table-bordered\">");

        builder.append("<tr>");
        builder.append("<th scope=\"col\">Airline</th>");
        builder.append("<th scope=\"col\">Name</th>");
        builder.append("<th scope=\"col\">From</th>");
        builder.append("<th scope=\"col\">To</th>");
        builder.append("<th scope=\"col\">Departure</th>");
        builder.append("<th scope=\"col\">Arrival</th>");
        builder.append("<th scope=\"col\">Aircraft</th>");
        builder.append("</tr>");

        StringBuilder dataRows = new StringBuilder();
        try {
            DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:MM");
            for (DbFlight flight : services.getFlights()) {
                dataRows.append("<tr>");
                dataRows.append("<td>" + services.getAirline(flight.airline_id).name + "</td>");
                dataRows.append("<td>" + flight.name + "</td>");
                dataRows.append("<td>" + flight.airportFrom + "</td>");
                dataRows.append("<td>" + flight.airportTo + "</td>");
                dataRows.append("<td>" + df.format(flight.departure) + "</td>");
                dataRows.append("<td>" + df.format(flight.arrival) + "</td>");
                dataRows.append("<td>" + flight.aircraft + "</td>");
                dataRows.append("</tr>");
            }
        } catch (Exception e) {
            dataRows.append("<tr>");
            dataRows.append("<td>Failed to load data...</td>");
            dataRows.append("</tr>");
        }

        builder.append(dataRows.toString());

        builder.append("</table>");
        builder.append("</body>");
        return builder.toString();
    }
}
