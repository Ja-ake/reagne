package com.jakespringer.reagne.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.jakespringer.reagne.fundamental.Observable;
import com.jakespringer.reagne.fundamental.Subscriber;

public abstract class AbstractEntity implements Entity {

    private final List<Observable<?>> observables = new LinkedList<>();
    private final List<Subscriber> subscribers = new LinkedList<>();

    public void mark(Observable<?>... observableArray) {
        observables.addAll(Arrays.asList(observableArray));
    }
    
    public void mark(Subscriber... subscriberList) {
        subscribers.addAll(Arrays.asList(subscriberList));
    }

    @Override
    public void destroy() {
        observables.forEach(Observable::clear);
        subscribers.forEach(Subscriber::call);
    }
}
