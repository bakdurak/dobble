package com.example.dobble.release.dto;

public class RxStatusDto<T> {
    private String message;
    private T data;

    public RxStatusDto(String message) {
        this.message = message;
    }

    public RxStatusDto(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return message == null;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
