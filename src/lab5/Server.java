package lab5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import db.DbAirServices;
import db.DbConfigs;
import db.DbFlight;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    private final static String QUEUE_TO_SERVER = "to_server";
    private final static String QUEUE_TO_CLIENT = "to_client";

    private Connection connection;
    private Channel channel;

    public Server(String host) throws Exception {
        DbAirServices services = new DbAirServices();
        services.connect(DbConfigs.URL, DbConfigs.USER, DbConfigs.PASSWORD);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.queueDeclare(QUEUE_TO_SERVER, false, false, false, null);
        channel.queueDeclare(QUEUE_TO_CLIENT, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received:\n'" + message + "'");
                String[] parts = message.split(",");

                if (parts[0].equals("add_airline")) {
                    int code = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    services.addAirline(code, name);
                } else if (parts[0].equals("delete_airline")) {
                    int code = Integer.parseInt(parts[1]);
                    services.deleteAirline(code);
                } else if (parts[0].equals("add_flight")) {
                    int code = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    String airportFrom = parts[3];
                    String airportTo = parts[4];
                    String aircraft = parts[5];
                    String departure = parts[6];
                    String arrival = parts[7];
                    int airlineCode = Integer.parseInt(parts[8]);
                    services.addFlight(code, name, airportFrom, airportTo, aircraft,
                            departure, arrival, airlineCode);
                } else if (parts[0].equals("delete_flight")) {
                    int code = Integer.parseInt(parts[1]);
                    services.deleteFlight(code);
                } else if (parts[0].equals("get_flights")) {
                    ArrayList<DbFlight> flights = services.getFlights();

                    StringBuilder response = new StringBuilder();
                    DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:MM");
                    for (DbFlight flight : flights) {
                        String line = String.join("\t", Arrays.asList(String.valueOf(flight.code),
                                flight.name, flight.airportFrom, flight.airportTo, flight.aircraft,
                                df.format(flight.departure), df.format(flight.arrival),
                                services.getAirline(flight.airline_id).name));
                        response.append(line);
                        response.append("\n");
                    }
                    send(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        channel.basicConsume(QUEUE_TO_SERVER, true, deliverCallback, consumerTag -> { });

        System.err.println("Server ready");
    }

    public void send(String message) throws Exception {
        channel.basicPublish("", QUEUE_TO_CLIENT, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("Sent '" + message + "'");
    }

    public static void main(String[] argv) throws Exception {
        Server server = new Server("localhost");
    }
}
