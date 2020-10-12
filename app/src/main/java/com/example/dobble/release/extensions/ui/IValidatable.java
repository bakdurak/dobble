package com.example.dobble.release.extensions.ui;

public interface IValidatable {
    boolean isValid();

    void subscribe(IValidatableSubscriber subscriber);

    void unsubscribe(IValidatableSubscriber subscriber);
}
