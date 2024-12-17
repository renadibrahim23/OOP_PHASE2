package Network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch(IOException e){
            closeEverything();
        }
    }

    public void sendMessage(){ //this message which the clientHandler wll read
        try(Scanner scanner = new Scanner(System.in)){
            while(socket.isConnected()){
                System.out.println("Your message: ");
                String messageToSent = scanner.nextLine();
                bufferedWriter.write(messageToSent);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch(IOException e){
            closeEverything();
        }
    }


    public void ListenForMessage(){
        //every client has separate thread waiting for messages
        //The thread object is annoynomus object
        new Thread(new Runnable(){ //take an object that is Runnable or implements the Runnable interface
            @Override
            public void run(){
                String msgFromChat;

                while(socket.isConnected()){
                    try{
                        msgFromChat = bufferedReader.readLine();
                        System.out.println(msgFromChat);
                    } catch (IOException e) {
                        closeEverything();
                    }
                }
            }
        }).start();
    }


    private void closeEverything() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{

        Socket socket = new Socket("localhost", 5050);
        Client client = new Client(socket);

        //blocking methods
        client.ListenForMessage();
        client.sendMessage();
    }

}