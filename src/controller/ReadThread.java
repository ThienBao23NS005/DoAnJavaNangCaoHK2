package controller;

import DAO.DAO;
import model.Message;
import model.User;
import views.ClientFrm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ReadThread implements Runnable{
    private ObjectInputStream reader;
    private Socket socket;
    private ClientFrm client;
    public ReadThread(ClientFrm c, Socket s) {
        this.client = c;
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Message response = null;
        do {
            try {
                response = (Message) reader.readObject();
                switch (response.getType()) {
                    case "FETCH_USERS" -> {
                        System.out.println(response.getType());
                        client.updateListUsers((ArrayList<User>) response.getPayload());
                        client.setDefaultUserSelection();
                        break;
                    }
                    case "USER_CONN" -> {
                        System.out.println("USER_CONN");
                        client.setUserOnline((User) response.getPayload());
                        break;
                    }
                    case "PRIVATE_MESSAGE" -> {
                        System.out.println("Has new message");
                        client.onPrivateMessage(response);
                        break;
                    }
                    case "PRIVATE_FILE_MESSAGE" -> {
                        System.out.println("Has new file");
                        User from = new DAO().getUserById(response.getFrom());
                        client.onPrivateFileMessage(reader, from);
                        break;
                    }
                    case "USER_DISCONNECT" -> {
                        System.out.println("A user disconnected");
                        client.setUserOffLine((User) response.getPayload());
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }

        } while (true);
    }
}
