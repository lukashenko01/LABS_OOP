package lab1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main {

    public static String DIR = "results/lab1/";

    public static void main(String[] args) throws Exception {
        new File(DIR).mkdirs();

        example1();
        example2();
        example3();
        example4();

        System.out.println("END!!!");
    }

    public static void example1() throws Exception {
        System.out.println("\nExample 1");
        AirServices services = new AirServices();
        services.addAirline(1, "Ryanair");
        services.addFlight(1, "RA5324", "KPB", "WAW", "Airbus 320",
                "17/12/2019 16:20", "17/12/2019 17:30", 1);
        services.addAirline(2, "British airlines");
        services.addFlight(2, "BA1543", "KPB", "LHR", "Airbus 320",
                "17/12/2019 11:00", "17/12/2019 13:40", 2);
        services.addFlight(3, "BA1544", "LHR", "KBP", "Airbus 320",
                "17/12/2019 15:35", "17/12/2019 19:25", 2);
        services.saveToFile(DIR + "checkpoint-1.xml");
    }

    public static void example2() throws Exception {
        System.out.println("\nExample 2");
        AirServices services = new AirServices();
        services.loadFromFile(DIR + "checkpoint-1.xml");
        services.deleteAirline(2);
        services.saveToFile(DIR + "checkpoint-2.xml");
    }

    public static void example3() throws Exception {
        System.out.println("\nExample 3");
        AirServices services = new AirServices();
        services.loadFromFile(DIR + "checkpoint-1.xml");
        try {
            services.deleteAirline(3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ArrayList<Flight> flights = services.getFlightsByAirline(2);
        assert flights.size() == 2;
        assert services.getFlight(2) != null && services.getFlight(3) != null;
        assert flights.contains(services.getFlight(2));
        assert flights.contains(services.getFlight(3));
        services.deleteFlight(3);
        assert services.getFlight(2) != null && services.getFlight(3) == null;
        flights = services.getFlightsByAirline(2);
        assert flights.size() == 1;
        assert flights.contains(services.getFlight(2));
        services.saveToFile(DIR + "checkpoint-3.xml");
    }

    public static void example4() throws Exception {
        System.out.println("\nExample 4");
        AirServices services = new AirServices();
        services.loadFromFile(DIR + "checkpoint-2.xml");
        try {
            services.deleteFlight(3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        services.loadFromFile(DIR + "checkpoint-1.xml");
        services.getFlight(1).airportTo = "LWO";
        services.getFlight(1).arrival = new SimpleDateFormat(Flight.DATE_FORMAT).parse("17/12/2019 17:45");
        services.deleteFlight(2);
        services.saveToFile(DIR + "checkpoint-4.xml");
    }
}
