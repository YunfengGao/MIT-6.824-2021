import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import vo.Constant;
import vo.GetArgs;
import vo.PutArgs;

public class Server {
    private final ExecutorService threadPool;

    static class KV implements KVService {
        Map<String, String> data;

        KV() {
            data = new ConcurrentHashMap<>();
        }

        public String get(GetArgs getArgs) {
            return data.get(getArgs.getKey());
        }

        public void put(PutArgs putArgs) {
            data.put(putArgs.getKey(), putArgs.getValue());
        }

        public int size() {
            return data.size();
        }
    }

    private Server() throws IOException {
        threadPool = Executors.newFixedThreadPool(10);
        final KV kv = new KV();
        register(kv);
    }

    private void register(Object Service) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constant.PORT);
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            threadPool.submit(new Processor(socket, Service));
        }
        serverSocket.close();
    }

    static class Processor implements Runnable {
        Socket socket;
        Object service;

        public Processor(Socket socket, Object service) {
            this.socket = socket;
            this.service = service;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String methodName = in.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
                Object[] parameters = (Object[]) in.readObject();
                Method method = KVService.class
                    .getMethod(
                        methodName,
                        parameterTypes
                    );
                Object result = method.invoke(service, parameters);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
