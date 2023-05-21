package org.qaautomation.config;

public enum TestEnv {
    LOCALHOST("localhost"),
    DEVELOP("develop"),
    STAGING("staging");

    TestEnv(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}