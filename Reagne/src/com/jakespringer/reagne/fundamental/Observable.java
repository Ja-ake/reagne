package com.jakespringer.reagne.fundamental;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Observable<U> implements Notifier, Signal<U> {
    private U currentValue;
    private Queue<Observer<U>> observers = new ConcurrentLinkedQueue<>();
    private Queue<Subscriber> subscribers = new ConcurrentLinkedQueue<>();
    private Queue<Observable<?>> children = new ConcurrentLinkedQueue<>();
    private Subscriber toDestroyMe = () -> {};
    private boolean destroyed = false;
    
    private Observable() {
    }
    
    public Observable(U value) {
        checkNull(value);
        currentValue = value;
    }

    public void set(U value) {
        checkNull(value);
        currentValue = value;
        alert();
    }
    
    public void replace(Function<U, U> editor) {
        set(editor.apply(currentValue));
    }
    
    public void modify(Consumer<U> modifier) {
        modifier.accept(currentValue);
        alert();
    }
    
    public void alert() {
        observers.forEach(c -> c.observe(currentValue));
        subscribers.forEach(Subscriber::call);
    }

    @Override
    public U get() {
        return currentValue;
    }

    @Override
    public Subscriber subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        return () -> subscribers.remove(subscriber);
    }
    
    public void clear() {
        children.forEach(Observable::clear);
        toDestroyMe.call();
        observers.clear();
        subscribers.clear();
        children.clear();
        destroyed = true;
    }
    
    public Subscriber forEach(Observer<U> observer) {
        observers.add(observer);
        return () -> observers.remove(observer);
    }
    
    public Observable<U> filter(Predicate<U> predicate) {
        checkNull(predicate);
        final Observable<U> filteredObservable = new Observable<>();
        children.add(filteredObservable);
        appendToDestroy(forEach(x -> {
            if (predicate.test(x)) {
                filteredObservable.set(x);
            }
        }));
        
        return filteredObservable;
    }
    
    public Observable<U> filter(Signal<Boolean> signal) {
        return filter(x -> signal.get());
    }
    
    public Observable<U> combine(Observable<U> other) {
        checkNull(other);
        final Observable<U> combined = new Observable<>(get());
        children.add(combined);
        other.children.add(combined);
        appendToDestroy(forEach(x -> combined.set(x)));
        appendToDestroy(other.forEach(x -> combined.set(x)));
        return combined;
    }
    
    public <V> Subscriber trigger(Observable<V> triggered, Supplier<V> valueSupplier) {
        children.add(triggered);
        return forEach(x -> triggered.set(valueSupplier.get()));
    }
    
    public Notifier toNotifier() {
        return (Notifier) this;
    }
    
    public Signal<U> toSignal() {
        return (Signal<U>) this;
    }
    
    public boolean collectGarbage() {
        children.removeIf(c -> c.collectGarbage() || c.destroyed);
        if (observers.isEmpty() && subscribers.isEmpty() && children.isEmpty()) {
            clear();
            return true;
        } else {
            return false;
        }
    }
    
    private void checkNull(Object item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }
    
    private void appendToDestroy(Subscriber destroyMethod) {
        final Subscriber temp = toDestroyMe;
        toDestroyMe = () -> {
            temp.call();
            destroyMethod.call();
        };
    }
}
