package main;

import java.lang.reflect.InvocationTargetException;
import mrapp.IMapReduce;

public class MrWorker {

    public static void main(String[] args) {
        IMapReduce mapReduce = loadPlugin(args[0]);
        new mr.Worker(mapReduce);
    }

    private static IMapReduce loadPlugin(String fileName) {
        IMapReduce mapReduce;
        try {
            @SuppressWarnings("unchecked")
            Class<IMapReduce> clazz = (Class<IMapReduce>) Class.forName(fileName);
            mapReduce = clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mapReduce;
    }
}
