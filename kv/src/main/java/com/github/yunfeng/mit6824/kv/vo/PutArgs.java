package com.github.yunfeng.mit6824.kv.vo;

import java.io.Serializable;

public class PutArgs implements Serializable {
    private String key;
    private String value;

    public PutArgs(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
