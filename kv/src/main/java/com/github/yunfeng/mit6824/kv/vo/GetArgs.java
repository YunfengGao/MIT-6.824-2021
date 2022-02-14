package com.github.yunfeng.mit6824.kv.vo;

import java.io.Serializable;

public class GetArgs implements Serializable {
    private String key;

    public GetArgs(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
