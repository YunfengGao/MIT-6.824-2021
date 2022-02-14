package com.github.yunfeng.mit6824.kv;

import com.github.yunfeng.mit6824.kv.vo.Constant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private final ExecutorService threadPool;

    private Server() throws IOException {
        threadPool = Executors.newFixedThreadPool(10);
        KVService kvService = new DefaultKVService();
        register(kvService);
    }

    private void register(Object Service) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constant.PORT);
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            threadPool.submit(new Processor(socket, Service));
        }
        serverSocket.close();
    }

    /**
     * JDK14的record关键字，
     * https://www.baeldung.com/java-record-keyword
     */
    private record Processor(Socket socket, Object service) implements Runnable {
        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream())) {
                String methodName = in.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
                Object[] parameters = (Object[]) in.readObject();
                Method method = KVService.class.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service, parameters);
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
