import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Server {
    // The port number on which the server will be listening
    private static int port = 2014;
    // The server socket.
    private static ServerSocket listener = null;

    public static void main(String[] args) throws Exception {

        boolean listening = true;
        try {
            listener = new ServerSocket(port);
            while (listening) {
                new ClientHandler(listener.accept()).run();
            }
        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }
        listener.close();
        /**
         Open a server socket on the specified port number(2014)
         and monitor the port for connection requests. When a
         connection request is received, create a client request
         thread, passing to its constructor a reference to the
         Socket object that represents the established connection
         with the client.
         */
    }
}

class ClientHandler extends Thread {
    Socket socket;

    //constructor
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    //implement the run method
    public void run() {
        handleConnection(socket);
    }

    //implement the handleConnection method here.
    public void handleConnection(Socket socket) {
        Vector<Contact> directory = new Vector<Contact>();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Set up input stream
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true); //Set up output stream
            output.println("Checking output stream... OK");

            String request = " ";

            while ((request = input.readLine()) != "quit") {
                System.out.println("New Request: '" + request); // Read one line and output it
                String[] instruction = request.split(" "); //Split the request into a command a parameter
                if (instruction[0].equalsIgnoreCase("get")) { //Return the phone number from the directory
                    for (Contact c : directory) {
                        if (c.getName().equals(instruction[1])) {
                            output.println("200 " + c.getNumber());
                            break;
                        }
                    }
                } else if (instruction[0].equalsIgnoreCase("remove")) { //Remove a contact from the directory
                    for (Contact c : directory) {
                        if (c.getName().equals(instruction[1])) {
                            output.println("100 OK");
                            directory.remove(c);
                            break;
                        }
                    }
                } else if (instruction[0].equalsIgnoreCase("store")) {
                    directory.add(new Contact(instruction[1], instruction[2])); //Add the contact listed to the directory
                    output.println("100 OK");
                } else {
                    output.println("400 Bad request"); //Something else went wrong...
                }
            }
            input.close();
        } catch (IOException err) {

        }
    }
}
