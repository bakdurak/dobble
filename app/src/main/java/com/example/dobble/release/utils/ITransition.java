package com.example.dobble.release.utils;

public interface ITransition <S> {
    Class<? extends S> accept(S state);
}
