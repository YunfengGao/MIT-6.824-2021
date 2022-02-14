package com.github.yunfeng.mit6824.kv;

import com.github.yunfeng.mit6824.kv.vo.GetArgs;
import com.github.yunfeng.mit6824.kv.vo.PutArgs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultKVService implements KVService {
    private final Map<String, String> data;

    public DefaultKVService() {
        data = new ConcurrentHashMap<>();
    }

    @Override
    public String get(GetArgs getArgs) {
        return data.get(getArgs.getKey());
    }

    @Override
    public void put(PutArgs putArgs) {
        data.put(putArgs.getKey(), putArgs.getValue());
    }

    @Override
    public int size() {
        return data.size();
    }
}