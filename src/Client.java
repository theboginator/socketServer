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
                response = input.readLine();
                System.out.println("\nMSG from Server: " +response);
                System.out.print("Enter an instruction: ");
                command = keyboard.nextLine();
                command = command.trim();
                output.println(command);

            } while (command != "quit");
        }
        catch (IOException e){
            System.out.println(e);
        }
    }


}
