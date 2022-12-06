// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 1147
        ServerSocket serverSocket = new ServerSocket(1147);

        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket socket = null;

            try
            {
                // socket object to receive incoming client requests
                socket = serverSocket.accept();
                InetAddress ipAddress = socket.getInetAddress();
                System.out.println("A new client is connected : " + ipAddress.getHostName());

                // obtaining input and out streams
                DataInputStream read = new DataInputStream(socket.getInputStream());
                DataOutputStream write = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for "+ ipAddress.getHostName());

                // create a new thread object
                Thread clientThread = new ClientHandler(socket, read, write, ipAddress);

                // Invoking the start() method
                clientThread.start();

            }
            catch (Exception exception){
                socket.close();
                exception.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
    DateFormat forDate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat forTime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream read;
    final DataOutputStream write;
    final Socket socket;
    final InetAddress ipClient;

    // Constructor
    public ClientHandler(Socket socket, DataInputStream read, DataOutputStream write, InetAddress ipClient)
    {
        this.socket = socket;
        this.read = read;
        this.write = write;
        this.ipClient = ipClient;
    }

    @Override
    public void run()
    {
        String text;
        while (true)
        {
            try {

                // Ask user what he wants
                write.writeUTF("\nWhat do you want?[Date | Time]...\n"+
                        "Type >Exit< to terminate connection.");

                // receive the answer from client
                text = read.readUTF();

                if(text.equals("Exit"))
                {
                    System.out.println("Client "+this.ipClient.getHostName()+
                            " wants to Exit...");
                    System.out.println("Closing this connection.");
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                switch (text) {

                    case "Date" :
                        text = forDate.format(date);
                        write.writeUTF(text);
                        break;

                    case "Time" :
                        text = forTime.format(date);
                        write.writeUTF(text);
                        break;

                    default:
                        write.writeUTF("Invalid input\n"+
                                "Please, try again!");
                        break;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.read.close();
            this.write.close();

        }catch(IOException exception){
            exception.printStackTrace();
        }
    }
}
