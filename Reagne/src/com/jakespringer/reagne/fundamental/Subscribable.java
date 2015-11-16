package com.jakespringer.reagne.fundamental;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Subscribable implements Notifier {

    private Queue<Subscriber> subscribers = new ConcurrentLinkedQueue<>();
    
    @Override
    public Subscriber subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        return () -> subscribers.remove(subscriber);
    }
    
    public void alert() {
        subscribers.forEach(Subscriber::call);
    }
}
