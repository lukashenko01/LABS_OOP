package lab3;

import db.DbConfigs;
import db.DbAirServices;
import db.DbAirline;
import db.DbFlight;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket server = null;
    private Socket sock = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private DbAirServices services = new DbAirServices();

    public void run(int port) throws Exception {
        server = new ServerSocket(port);
        services.connect(DbConfigs.URL, DbConfigs.USER, DbConfigs.PASSWORD);

        while (true) {
            sock = server.accept();

            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());

            while (processQuery()) ;
        }
    }

    public void stop() throws Exception {
        server.close();
        services.disconnect();
    }

    private boolean processQuery() {
        try {
            int oper = in.readInt();
            System.out.println(oper);

            if (oper == OperationTypes.ADD_AIRLINE) {
                int code = in.readInt();
                String name = in.readUTF();
                out.writeBoolean(services.addAirline(code, name));
            } else if (oper == OperationTypes.QUERY_AIRLINE) {
                int code = in.readInt();
                services.getAirline(code).serialize(out);
            } else if (oper == OperationTypes.LIST_AIRLINES) {
                ArrayList<DbAirline> airlines = services.getAirlines();
                out.writeInt(airlines.size());
                for (DbAirline airline : airlines) {
                    airline.serialize(out);
                }
            } else if (oper == OperationTypes.DELETE_AIRLINE) {
                int code = in.readInt();
                out.writeBoolean(services.deleteAirline(code));
            } else if (oper == OperationTypes.ADD_FLIGHT) {
                int code = in.readInt();
                int airlineCode = in.readInt();
                String name = in.readUTF();
                String airportFrom = in.readUTF();
                String airportTo = in.readUTF();
                String aircraft = in.readUTF();
                String departure = in.readUTF();
                String arrival = in.readUTF();
                out.writeBoolean(services.addFlight(code, name, airportFrom, airportTo, aircraft, departure, arrival, airlineCode));
            } else if (oper == OperationTypes.UPDATE_FLIGHT) {
                int code = in.readInt();
                int cnt = in.readInt();
                Map<String, String> changes = new HashMap<>();
                for (int i = 0; i < cnt; ++i) {
                    changes.put(in.readUTF(), in.readUTF());
                }
                out.writeBoolean(services.updateFlight(code, changes));
            } else if (oper == OperationTypes.QUERY_FLIGHT) {
                int code = in.readInt();
                services.getFlight(code).serialize(out);
            } else if (oper == OperationTypes.LIST_FLIGHTS) {
                ArrayList<DbFlight> flights = services.getFlights();
                out.writeInt(flights.size());
                for (DbFlight flight : flights) {
                    flight.serialize(out);
                }
            } else if (oper == OperationTypes.LIST_FLIGHTS_BY_AIRLINE) {
                int code = in.readInt();
                ArrayList<DbFlight> flights = services.getFlightsByAirline(code);
                out.writeInt(flights.size());
                for (DbFlight flight : flights) {
                    flight.serialize(out);
                }
            } else if (oper == OperationTypes.DELETE_FLIGHT) {
                int code = in.readInt();
                out.writeBoolean(services.deleteFlight(code));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        Server srv = new Server();
        srv.run(12345);
        srv.stop();
    }

}
