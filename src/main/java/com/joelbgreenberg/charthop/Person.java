package com.joelbgreenberg.charthop;

import java.time.Instant;

public class Person {

    private String id;
    private  Name name;
    private  Instant createAt;

    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }
}
