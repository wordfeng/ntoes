package org.feng.persistent.entity;

import java.io.Serializable;

public class Test extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -21257628657138829L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}