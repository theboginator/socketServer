/*
SocketServer Server Manager v1.1
Creates a server listener on port 2014 that listens for clients to connect.
Once a client connects, server sets up a vector of Contacts for the client.
Client may request "get" "add" or "remove" (or "quit")
(c) 2019 Jacob Bogner
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.net.*;

public class Server {
    // The port number on which the server will be listening
    private static int port = 2014;
    // The server socket.
    private static ServerSocket serverListener = null;

    public static void main(String[] args) throws Exception {

        boolean listening = true;
        try {
            serverListener = new ServerSocket(port);
            while (listening) {
                //Allow each client attempting to connect to start a new "handle connection" thread
                new ClientHandler(serverListener.accept()).run();
            }
        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage()); //Handle an error
        }
        serverListener.close();
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
        Vector<Contact> directory = new Vector<Contact>(); //Create the client's unique directory of Contacts

        try {
            //Set up input stream
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Set up output stream
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("Checking output stream... OK"); //Print a test message to client
            //Create a string to hold requests from the client
            String request = " ";

            while ((request = input.readLine()) != "quit") { //Handle client requests as long as no "quit" is sent
                boolean found = false;
                System.out.println("New Request: " + request); // Read one line and output it to server screen
                String[] instruction = request.split(" "); //Split the request into a command and a parameter

                if (instruction[0].equalsIgnoreCase("get")) { //Return the phone number from the directory
                    for (Contact c : directory) {
                        if (c.getName().equals(instruction[1])) { //Search directory for the name
                            output.println("200 " + c.getNumber()); //Send the found number to the client
                            System.out.println("Sent " +instruction[1] +" " +c.getNumber() +" to client");
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        output.println("400 Not Found"); //Return error if can't find name
                    }
                } else if (instruction[0].equalsIgnoreCase("remove")) {//Remove a contact from the directory
                    for (Contact c : directory) {
                        if (c.getName().equals(instruction[1])) {
                            output.println("100 OK");
                            System.out.println("Removed " +instruction[1] +" " +c.getNumber() +" from directory");
                            directory.remove(c);
                            found = true;
                            break;
                        }
                    }
                    if (!found){
                        output.println("400 Not Found"); //Return error if can't find name
                    }
                } else if (instruction[0].equalsIgnoreCase("store")) {
                    directory.add(new Contact(instruction[1], instruction[2]));//Add the contact listed to the directory
                    output.println("100 OK");
                    System.out.println("Stored " +instruction[1] +" " +instruction[2] +" to directory");
                } else {
                    output.println("300 Bad request"); //Something else went wrong...
                }
            }
            input.close();
        } catch (IOException err) { //Handle an error

        }
    }
}
