package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class DbAirServices implements IDbServices {
    private Connection con;
    private Statement stmt;

    public DbAirServices() {
        con = null;
        stmt = null;
    }

    public void connect(String url, String user, String password) throws Exception {
        if (con != null) throw new Exception("Connection already exists");
        Class.forName("org.mariadb.jdbc.Driver").newInstance();
        con = DriverManager.getConnection(url, user, password);
        stmt = con.createStatement();
    }

    public void disconnect() throws Exception {
        if (con == null) throw new Exception("There is no active connection");
        con.close();
        con = null;
        stmt = null;
    }

    public boolean addAirline(int code, String name) throws Exception {
        String sql = "INSERT INTO airlines (id, NAME)" +
                "VALUES (" + code + ", '" + name + "')";
        try {
            stmt.executeUpdate(sql);
            System.out.println("Successfully added airline with code " + code);
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to add airline with code " + code);
            return false;
        }
    }

    public DbAirline getAirline(int code) throws Exception {
        String sql = "SELECT id, name FROM airlines WHERE id=" + code;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                return new DbAirline(id, name);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting airline with code " + code);
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<DbAirline> getAirlines() throws Exception {
        ArrayList<DbAirline> airlines = new ArrayList<>();
        String sql = "SELECT id, name FROM airlines";
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                airlines.add(new DbAirline(id, name));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting all airline");
            System.out.println(e.getMessage());
        }
        return airlines;
    }

    public boolean deleteAirline(int code) throws Exception {
        String sql = "DELETE FROM airlines WHERE id = " + code;
        try {
            int c = stmt.executeUpdate(sql);
            if (c > 0) {
                System.out.println("Airline with code " + code + " successfully deleted!");
                return true;
            } else {
                System.out.println("Airline with code " + code + " wasn't found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("An error occured while deleting airline with code " + code);
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean addFlight(int code, String name, String airportFrom, String airportTo,
                             String aircraft, String departure, String arrival,
                             int airlineCode) {
        String sql = "INSERT INTO flights (id, airline_id, name, airport_from, airport_to, aircraft, departure, arrival)" +
                "VALUES (" + code + ", " + airlineCode + ", '" + name + "', '" + airportFrom + "', '" + airportTo + "', '" + aircraft + "', '" + departure + "', '" + arrival + "')";
        try {
            stmt.executeUpdate(sql);
            System.out.println("Successfully added flight with code " + code);
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to add flight with code " + code);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateFlight(int code, Map<String,String> changes) {
        StringBuilder assignments = new StringBuilder();
        for (Map.Entry change : changes.entrySet()) {
            if (assignments.length() > 0)
                assignments.append(", ");
            assignments.append(change.getKey() + "='" + change.getValue() + "'");
        }

        String sql = "UPDATE flights" +
                " SET " + assignments.toString() + " WHERE id=" + code;
        System.out.println(sql);

        try {
            stmt.executeUpdate(sql);
            System.out.println("Successfully updated flight with code " + code);
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update flight with code " + code);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public DbFlight getFlight(int code) {
        String sql = "SELECT id FROM flights WHERE id=" + code;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int airline_id = rs.getInt("airline_id");
                String name = rs.getString("name");
                String airportFrom = rs.getString("airport_from");
                String airportTo = rs.getString("airport_to");
                String aircraft = rs.getString("aircraft");
                Date departure = rs.getDate("departure");
                Date arrival = rs.getDate("arrival");

                return new DbFlight(id, airline_id, name, airportFrom, airportTo, aircraft, departure, arrival);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting flight with code " + code);
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<DbFlight> getFlights() {
        ArrayList<DbFlight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int airline_id = rs.getInt("airline_id");
                String name = rs.getString("name");
                String airportFrom = rs.getString("airport_from");
                String airportTo = rs.getString("airport_to");
                String aircraft = rs.getString("aircraft");
                Date departure = rs.getDate("departure");
                Date arrival = rs.getDate("arrival");

                flights.add(new DbFlight(id, airline_id, name, airportFrom, airportTo, aircraft, departure, arrival));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting all flights");
            System.out.println(e.getMessage());
        }
        return flights;
    }

    public ArrayList<DbFlight> getFlightsByAirline(int airlineCode) {
        ArrayList<DbFlight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE airline_id=" + airlineCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int airline_id = rs.getInt("airline_id");
                String name = rs.getString("name");
                String airportFrom = rs.getString("airport_from");
                String airportTo = rs.getString("airport_to");
                String aircraft = rs.getString("aircraft");
                Date departure = rs.getDate("departure");
                Date arrival = rs.getDate("arrival");

                flights.add(new DbFlight(id, airline_id, name, airportFrom, airportTo, aircraft, departure, arrival));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("An error occured while requesting flights by arline");
            System.out.println(e.getMessage());
        }
        return flights;
    }

    public boolean deleteFlight(int code) {
        String sql = "DELETE FROM flights WHERE id = " + code;
        try {
            int c = stmt.executeUpdate(sql);
            if (c > 0) {
                System.out.println("Flight with code " + code + " successfully deleted!");
                return true;
            } else {
                System.out.println("Flight with code " + code + " wasn't found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("An error occured while deleting flight with code " + code);
            System.out.println(e.getMessage());
            return false;
        }
    }
}