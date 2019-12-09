package lab3;

import db.DbAirline;
import db.DbFlight;
import db.IDbServices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class SocketServices implements IDbServices {
    private Socket sock = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public SocketServices(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
    }

    public void disconnect() throws IOException {
        sock.close();
    }

    @Override
    public boolean addAirline(int code, String name) throws Exception {
        out.writeInt(OperationTypes.ADD_AIRLINE);
        out.writeInt(code);
        out.writeUTF(name);
        return in.readBoolean();
    }

    @Override
    public DbAirline getAirline(int code) throws Exception {
        out.writeInt(OperationTypes.QUERY_AIRLINE);
        out.writeInt(code);
        return new DbAirline(in);
    }

    @Override
    public ArrayList<DbAirline> getAirlines() throws Exception {
        out.writeInt(OperationTypes.LIST_AIRLINES);
        ArrayList<DbAirline> airlines = new ArrayList<>();
        int cnt = in.readInt();
        for (int i = 0; i < cnt; ++i) {
            airlines.add(new DbAirline(in));
        }
        return airlines;
    }

    @Override
    public boolean deleteAirline(int code) throws Exception {
        out.writeInt(OperationTypes.DELETE_AIRLINE);
        out.writeInt(code);
        return in.readBoolean();
    }

    @Override
    public boolean addFlight(int code, String name, String airportFrom, String airportTo, String aircraft, String departure, String arrival, int airlineCode) throws Exception {
        out.writeInt(OperationTypes.ADD_FLIGHT);
        out.writeInt(code);
        out.writeInt(airlineCode);
        out.writeUTF(name);
        out.writeUTF(airportFrom);
        out.writeUTF(airportTo);
        out.writeUTF(aircraft);
        out.writeUTF(departure);
        out.writeUTF(arrival);
        return in.readBoolean();
    }

    @Override
    public boolean updateFlight(int code, Map<String, String> changes) throws Exception {
        out.writeInt(OperationTypes.UPDATE_FLIGHT);
        out.writeInt(code);
        out.writeInt(changes.size());
        for (Map.Entry entry : changes.entrySet()) {
            out.writeUTF((String)entry.getKey());
            out.writeUTF((String)entry.getValue());
        }
        return in.readBoolean();
    }

    @Override
    public DbFlight getFlight(int code) throws Exception {
        out.writeInt(OperationTypes.QUERY_FLIGHT);
        out.writeInt(code);
        return new DbFlight(in);
    }

    @Override
    public ArrayList<DbFlight> getFlights() throws Exception {
        out.writeInt(OperationTypes.LIST_FLIGHTS);
        ArrayList<DbFlight> flights = new ArrayList<>();
        int cnt = in.readInt();
        for (int i = 0; i < cnt; ++i) {
            flights.add(new DbFlight(in));
        }
        return flights;
    }

    @Override
    public ArrayList<DbFlight> getFlightsByAirline(int airlineCode) throws Exception {
        out.writeInt(OperationTypes.LIST_FLIGHTS_BY_AIRLINE);
        out.writeInt(airlineCode);
        ArrayList<DbFlight> flights = new ArrayList<>();
        int cnt = in.readInt();
        for (int i = 0; i < cnt; ++i) {
            flights.add(new DbFlight(in));
        }
        return flights;
    }

    @Override
    public boolean deleteFlight(int code) throws Exception {
        out.writeInt(OperationTypes.DELETE_FLIGHT);
        out.writeInt(code);
        return in.readBoolean();
    }
}
