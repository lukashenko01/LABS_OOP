package db;

import java.io.Serializable;
import java.util.HashMap;

public class DbExamples implements Serializable {
    private IDbServices services;

    public DbExamples(IDbServices services) {
        this.services = services;
    }

    public void runAll() throws Exception {
        example1();
        example2();
        example3();
        example4();
    }

    public void example1() throws Exception {
        System.out.println("\nExample 1");

        services.deleteAirline(3);
        assert services.addAirline(3, "BBB");
        System.out.println(services.getAirlines());
        assert services.getAirline(3).name.equals("BBB");
        assert services.deleteAirline(3);
        System.out.println(services.getAirlines());
    }

    public void example2() throws Exception {
        System.out.println("\nExample 2");

        System.out.println(services.getFlights());
        services.addFlight(5, "RA5324", "KPB", "WAW", "Airbus 320",
                "2019-12-17 16:20:00", "2019-12-17 17:30:00", 1);
        System.out.println(services.getFlights());
        assert services.deleteFlight(5);
        System.out.println(services.getFlights());
    }

    public void example3() throws Exception {
        System.out.println("\nExample 3");

        System.out.println(services.getFlightsByAirline(1));
        System.out.println(services.getFlightsByAirline(2));
    }

    public void example4() throws Exception {
        System.out.println("\nExample 4");

        System.out.println(services.getFlightsByAirline(1));
        assert services.updateFlight(1, new HashMap<String, String>() {{
            put("airport_to", "LWO");
            put("arrival", "2019-12-17 17:45:00");
        }});
        System.out.println(services.getFlightsByAirline(1));
        assert services.updateFlight(1, new HashMap<String, String>() {{
            put("airport_to", "WAW");
            put("arrival", "2019-12-17 17:30:00");
        }});
    }
}
