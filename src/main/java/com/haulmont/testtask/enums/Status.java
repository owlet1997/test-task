package com.haulmont.testtask.enums;

public enum Status {
    PLANNED("Запланирован"),
    FINISHED("Выполнен"),
    RECEIVED("Принят клиентом");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
