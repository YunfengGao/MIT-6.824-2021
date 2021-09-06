import vo.GetArgs;
import vo.PutArgs;

public interface KVService {
    String get(GetArgs getArgs);

    void put(PutArgs putArgs);

    int size();
}
