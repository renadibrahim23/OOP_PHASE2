package Network;

import Service.AdminService;
import Service.CustomerService;

import java.io.*;
import java.util.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private static Map<String, ClientHandler> activeClients = new HashMap<>(); // key: username
    private static AdminService adminService = new AdminService();
    private static CustomerService customerService = new CustomerService(null);

    private Socket socket;
    private BufferedReader bufferedReader; //read messages from the client
    private BufferedWriter bufferedWriter;//send messages to the client
    private String username;
    private boolean isAdmin;

    //constructor
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //outputStreamWriter for charcters
            this.bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = bufferedReader.readLine();

        } catch(IOException e){
            closeEverything();

        }
    }

    //getters
    public static ClientHandler getClientHandlerByUsername(String username) {
        return activeClients.get(username);
    }

    public BufferedWriter getBufferedWriter() {
        return this.bufferedWriter;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    @Override
    public void run() {
        try {
            loginUser();
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println(username + ": " + message);
            }
        } catch (IOException e) {
            closeEverything();
        }
    }



    private void loginUser() throws IOException {
        bufferedWriter.write("Enter username: ");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        String usernameInput = bufferedReader.readLine();

        bufferedWriter.write("Enter password: ");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        String passwordInput = bufferedReader.readLine();

        // Check if admin or customer
        if (adminService.logIn(usernameInput, passwordInput)) {
            this.isAdmin = true;
            this.username = usernameInput;
            activeClients.put(usernameInput, this);
            bufferedWriter.write("Login successful as Admin.");
        } else if (customerService.logIn(usernameInput, passwordInput)) {
            this.isAdmin = false;
            this.username = usernameInput;
            activeClients.put(usernameInput, this);
            bufferedWriter.write("Login successful as Customer.");
        } else {
            bufferedWriter.write("Invalid credentials. Disconnecting.");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            closeEverything();
            return;
        }
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }




//    private void handleAdminMessage(String message) throws IOException {
//        String[] parts = message.split(":", 2);
//        String recipient = parts[0].trim();
//        String content = parts[1].trim();
//
//        ClientHandler recipientHandler = clientHandlers.get(recipient);
//        if (recipientHandler != null) {
//            recipientHandler.bufferedWriter.write("Admin: " + content);
//            recipientHandler.bufferedWriter.newLine();
//            recipientHandler.bufferedWriter.flush();
//        } else {
//            bufferedWriter.write("User not found.");
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        }
//    }


//    private void sendToAdmin(String message) throws IOException {
//        for (ClientHandler handler : clientHandlers.values()) {
//            if (handler.isAdmin) {
//                handler.bufferedWriter.write(username + ": " + message);
//                handler.bufferedWriter.newLine();
//                handler.bufferedWriter.flush();
//                break;
//            }
//        }
//    }

    private void closeEverything() {
        try {
            activeClients.remove(username);
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}