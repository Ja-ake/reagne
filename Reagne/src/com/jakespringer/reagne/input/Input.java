package com.jakespringer.reagne.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import com.jakespringer.reagne.Reagne;
import com.jakespringer.reagne.fundamental.Notifier;
import com.jakespringer.reagne.fundamental.Observable;
import com.jakespringer.reagne.fx.Window;
import com.jakespringer.reagne.math.Vec2;

public class Input {

    public static final boolean KEY_PRESSED = true;
    public static final boolean KEY_RELEASED = false;

    public static final Observable<Integer> onKeyPress = new Observable<>(0);
    public static final Observable<Integer> onKeyRelease = new Observable<>(0);
    public static final Observable<Integer> onMousePress = new Observable<>(0);
    public static final Observable<Integer> onMouseRelease = new Observable<>(0);

    private static Vec2 mouse;
    private static Vec2 mouseDelta;
    private static Vec2 mouseScreen;

    public static Vec2 getMouse() {
        return mouse;
    }

    public static Vec2 getMouseDelta() {
        return mouseDelta;
    }

    public static Vec2 getMouseScreen() {
        return mouseScreen;
    }

    static {
        Reagne.continuous.forEach(dt -> {
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState() == KEY_PRESSED) {
                    onKeyPress.set(Keyboard.getEventKey());
                } else /* ..getEventKeyState() == KEY_RELEASED */ {
                    onKeyRelease.set(Keyboard.getEventKey());
                }
            }

            while (Mouse.next()) {
                if (Mouse.getEventButtonState() == KEY_PRESSED) {
                    onMousePress.set(Mouse.getEventButton());
                } else /* ..getEventButtonState() == KEY_RELEASED */ {
                    onMouseRelease.set(Mouse.getEventButton());
                }
            }

            double w = Display.getWidth();
            double h = Display.getHeight();
            double ar = Window.aspectRatio();
            double vw, vh;

            if (w / h > ar) {
                vw = ar * h;
                vh = h;
            } else {
                vw = w;
                vh = w / ar;
            }
            double left = (w - vw) / 2;
            double bottom = (h - vh) / 2;

            mouseScreen = new Vec2((Mouse.getX() - left) / vw, (Mouse.getY() - bottom) / vh).multiply(Window.viewSize);
            mouse = mouseScreen.subtract(Window.viewSize.multiply(.5)).add(Window.viewPos);
            mouseDelta = new Vec2(Mouse.getDX() / vw, Mouse.getDY() / vh).multiply(Window.viewSize);
        });
    }

    public static Observable<Boolean> isKeyPressed(final int key) {
        Observable<Boolean> observable = new Observable<>(false);
        onKeyPress.filter(x -> x == key).trigger(observable, () -> true);
        onKeyRelease.filter(x -> x == key).trigger(observable, () -> false);
        return observable;
    }

    public static Notifier whenKeyPressed(final int key) {
        return onKeyPress.filter(x -> x == key).toNotifier();
    }

    public static Notifier whenKeyReleased(final int key) {
        return onKeyPress.filter(x -> x == key).toNotifier();
    }

    public static Notifier whenMousePressed(final int key) {
        return onMousePress.filter(x -> x == key).toNotifier();
    }

    public static Notifier whenMouseReleased(final int key) {
        return onMousePress.filter(x -> x == key).toNotifier();
    }

    public static Observable<Double> whileKeyDown(final int key) {
        return Reagne.continuous.filter(isKeyPressed(key));
    }

    public static Observable<Double> whileKeyUp(final int key) {
        return Reagne.continuous.filter(x -> !isKeyPressed(key).get());
    }

    private Input() {
    }
}
