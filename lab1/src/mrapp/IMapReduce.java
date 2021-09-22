package mrapp;

import java.util.List;
import mr.KeyValue;

public interface IMapReduce {
    List<KeyValue> map(String contents);

    String reduce(List<String> values);
}
