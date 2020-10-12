package com.example.dobble.release.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: Rework the class: make it abstract and remove advance() method
//  extend new class and implement advance's methods for all kind transitions
//  to avoid double dispatching 

public class Fsm<S> {
    private S activeState;
    // TODO: Replace Map with Set
    private Map<Class<?>, S> stateCache = new HashMap<>();

    public Fsm(Class<? extends S> initialState, ArrayList<? extends S> states) {
        for (S state : states) {
            this.stateCache.put(state.getClass(), state);
        }

        S initState = this.stateCache.get(initialState);
        if (initState != null) {
            this.activeState = initState;
        }
        else {
            throw new IllegalArgumentException("Array states does not contain initial state class");
        };
    }

    public boolean advance(ITransition<S> transition) {
        Class<? extends S> newStateClass = transition.accept(activeState);
        if (newStateClass == null) {
            return false;
        }
        else {
            activeState = stateCache.get(newStateClass);
            return true;
        }
    }
}
