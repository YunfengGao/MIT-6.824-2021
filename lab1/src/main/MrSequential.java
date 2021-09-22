package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mr.KeyValue;
import mrapp.IMapReduce;

public class MrSequential {
    public static void main(String[] args) {
        File file = new File(args[1]);
        // todo: 如何用Java模拟出Linux路径格式的正则？
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("warn: no file in dir");
            return;
        }

        List<KeyValue> intermediate = new ArrayList<>(files.length);
        IMapReduce mapReduce = loadPlugin(args[0]);
        for (File f : files) {
            String content = readAll(f);
            intermediate.addAll(mapReduce.map(content));
        }

        intermediate.sort(Comparator.comparing(KeyValue::getKey));

        String outputName = "mr-out-0";
        File outputFile = new File(outputName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < intermediate.size(); ) {
                int j = i + 1;
                String keyI = intermediate.get(i).getKey();

                while (j < intermediate.size() && (intermediate.get(j).getKey().equals(keyI))) {
                    j++;
                }
                List<String> values = new ArrayList<>(j - i);
                for (int k = i; k < j; k++) {
                    values.add(intermediate.get(k).getValue());
                }
                String output = mapReduce.reduce(values);
                bw.write(keyI + " " + output + System.lineSeparator());
                i = j;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static String readAll(final File f) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
