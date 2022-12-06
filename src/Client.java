// Java implementation for a client
// Save file as Client.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class
public class Client
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            Scanner reader = new Scanner(System.in);

            // getting localhost ip
            InetAddress ipClient = InetAddress.getByName("localhost");

            // establish the connection with server port 1147
            Socket socket = new Socket(ipClient, 1147);

            // obtaining input and out streams
            DataInputStream read = new DataInputStream(socket.getInputStream());
            DataOutputStream write = new DataOutputStream(socket.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (true)
            {
                System.out.println(read.readUTF());
                String tosend = reader.nextLine();
                write.writeUTF(tosend);

                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit"))
                {
                    System.out.println("Closing this connection : "
                            +ipClient.getHostName());
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing date or time as requested by client
                String received = read.readUTF();
                System.out.println(received);
            }

            // closing resources
            reader.close();
            read.close();
            write.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
