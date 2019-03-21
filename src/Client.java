/*
SocketServer Client Manager v1.1
Creates a server communicator on port 2014 that connects to server.
Once connected, server sets up a vector of Contacts for the client.
Client may request "get" "add" or "remove" (or "quit")
(c) 2019 Jacob Bogner
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main (String []args){
        int port = 2014;
        Scanner keyboard = new Scanner(System.in);
        String command;
        String response;
        try {
            Socket uplink = new Socket("localhost", port);
            PrintWriter output = new PrintWriter(uplink.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(uplink.getInputStream()));
            do{
                response = input.readLine(); //Get response from server
                System.out.println("\nMSG from Server: " +response); //print msg from server
                System.out.print("Enter an instruction: "); //prompt user for command
                command = keyboard.nextLine(); //get user input
                command = command.trim(); //trim user input
                output.println(command); //send command to server via output over socket

            } while (command != "quit"); //exit when command is quit
        }
        catch (IOException e){ //handle errors
            System.out.println(e);
        }
    }


}
