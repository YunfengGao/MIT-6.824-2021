package com.github.yunfeng.mit6824.kv;

import com.github.yunfeng.mit6824.kv.vo.GetArgs;
import com.github.yunfeng.mit6824.kv.vo.PutArgs;

public interface KVService {
    String get(GetArgs getArgs);

    void put(PutArgs putArgs);

    int size();
}
