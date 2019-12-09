package lab5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {
    private final static String QUEUE_TO_SERVER = "to_server";
    private final static String QUEUE_TO_CLIENT = "to_client";

    private Connection connection;
    private Channel channel;

    public Client(String host) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_TO_SERVER, false, false, false, null);
        channel.queueDeclare(QUEUE_TO_CLIENT, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received:\n'" + message + "'");
        };

        channel.basicConsume(QUEUE_TO_CLIENT, true, deliverCallback, consumerTag -> { });
    }

    public static void main(String[] argv) throws Exception {
        Client client = new Client("localhost");

        client.getFlights();
        client.addAirline(3, "BBB");
        client.addFlight(5, "BB1234", "AAA", "BBB", "Airbus 320",
                "2017-12-17 16:20:00", "2017-12-17 17:30:00", 3);
        client.addFlight(6, "BB2345", "XXX", "YYY", "Airbus 320",
                "2018-12-17 16:20:00", "2018-12-17 17:30:00", 3);
        client.getFlights();
        client.deleteFlight(5);
        client.deleteFlight(6);
        client.deleteAirline(3);
        client.getFlights();

        client.disconnect();
    }

    public void addAirline(int code, String name) throws Exception {
        String command = String.join(",", Arrays.asList("add_airline", String.valueOf(code), name));
        send(command);
    }

    public void deleteAirline(int code) throws Exception {
        String command = String.join(",", Arrays.asList("delete_airline", String.valueOf(code)));
        send(command);
    }

    public void addFlight(int code, String name, String airportFrom, String airportTo,
                   String aircraft, String departure, String arrival,
                   int airlineCode) throws Exception {
        String command = String.join(",", Arrays.asList("add_flight", String.valueOf(code), name, airportFrom, airportTo,
                aircraft, departure, arrival, String.valueOf(airlineCode)));
        send(command);
    }

    public void deleteFlight(int code) throws Exception {
        String command = String.join(",", Arrays.asList("delete_flight", String.valueOf(code)));
        send(command);
    }

    public void getFlights() throws Exception {
        send("get_flights");
    }

    public void send(String message) throws Exception {
        channel.basicPublish("", QUEUE_TO_SERVER, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("Sent '" + message + "'");
    }

    public void disconnect() throws Exception {
        channel.close();
        connection.close();
    }
}
