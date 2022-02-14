package com.github.yunfeng.mit6824.kv;

import com.github.yunfeng.mit6824.kv.vo.Constant;
import com.github.yunfeng.mit6824.kv.vo.GetArgs;
import com.github.yunfeng.mit6824.kv.vo.PutArgs;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final KVService kvClient;

    public Client() {
        kvClient = this.getClient(Constant.PORT);
    }

    public String get(String key) {
        return this.kvClient.get(new GetArgs(key));
    }

    public void put(String key, String value) {
        this.kvClient.put(new PutArgs(key, value));
    }

    public int size() {
        return this.kvClient.size();
    }

    private KVService getClient(int port) {
        return (KVService) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class<?>[]{KVService.class},
                (proxy, method, args) -> {
                    try (Socket socket = new Socket("127.0.0.1", port); ObjectOutputStream out =
                            new ObjectOutputStream(socket.getOutputStream())) {
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
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(" ");
            String method = words[0];
            if ("put".equals(method)) {
                String key = words[1];
                client.put(key, words[2]);
                System.out.println("put(" + key + "," + words[2] + ") done");
            } else if ("get".equals(method)) {
                String key = words[1];
                String value = client.get(key);
                System.out.println("get(" + key + ") -> " + value);
            } else {
                System.out.println("size() -> " + client.size());
            }
        }
    }
}
