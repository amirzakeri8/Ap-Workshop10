import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            Socket client = new Socket("127.0.0.1", 8080);
            System.out.println("Write your name: ");
            String name = sc.nextLine();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject(name);

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = (String) in.readObject();
                        System.out.println(message);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            while (true) {
                String message = sc.nextLine();
                if (message.equals("#exit")) {
                    client.close();
                }
                out.writeObject(message);
            }
        } catch (IOException e) {
            System.out.println("Done");
        }
    }
}
