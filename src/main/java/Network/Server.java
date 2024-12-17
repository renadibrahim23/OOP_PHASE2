package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server  {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!"); //the server will be running till a client connect to it
                ClientHandler clinetHandler = new ClientHandler(socket); //will handle each client each in a new thread

                Thread thread = new Thread(clinetHandler); //run a new thread for each client
                thread.start();

            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5050); //the server will listen to the client sending at port 1234
        Server server = new Server(serverSocket);
        server.startServer();
    }



}