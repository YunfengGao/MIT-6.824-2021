import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import vo.Constant;
import vo.GetArgs;
import vo.PutArgs;

public class Client {
    public String get(String key) {
        KVService client = connect();
        return client.get(new GetArgs(key));
    }

    public void put(String key, String value) {
        KVService client = connect();
        client.put(new PutArgs(key, value));
    }

    public int size() {
        KVService client = connect();
        return client.size();
    }

    private KVService connect() {
        return getClient(Constant.PORT);
    }

    private static KVService getClient(int port) {
        return (KVService) Proxy.newProxyInstance(
            Client.class.getClassLoader(), new Class<?>[] {KVService.class}, (proxy, method, args) -> {
                try (Socket socket = new Socket("127.0.0.1", port);
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                    out.writeUTF(method.getName());
                    out.writeObject(method.getParameterTypes());
                    out.writeObject(args);
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    return in.readObject();
                }
            });
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.put("subject", "6.824");
        System.out.println("Put(subject, 6.824) done");
        System.out.println("get(subject) -> " + client.get("subject"));
    }
}
