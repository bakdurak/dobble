package com.example.dobble.release.extensions.ui;

public interface IValidatableSubscriber {
    void notify(IValidatable validatable);

    void removeValidatable(IValidatable validatable);

    IValidatableSubscriber addValidatable(IValidatable validatable);
}
