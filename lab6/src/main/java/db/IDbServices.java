package db;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Map;

public interface IDbServices extends Remote {
    boolean addAirline(int code, String name) throws Exception;
    DbAirline getAirline(int code) throws Exception;
    ArrayList<DbAirline> getAirlines() throws Exception;
    boolean deleteAirline(int code) throws Exception;

    boolean addFlight(int code, String name, String airportFrom, String airportTo,
                      String aircraft, String departure, String arrival,
                      int airlineCode) throws Exception;
    boolean updateFlight(int code, Map<String, String> changes) throws Exception;
    DbFlight getFlight(int code) throws Exception;
    ArrayList<DbFlight> getFlights() throws Exception;
    ArrayList<DbFlight> getFlightsByAirline(int airlineCode) throws Exception;
    boolean deleteFlight(int code) throws Exception;
}