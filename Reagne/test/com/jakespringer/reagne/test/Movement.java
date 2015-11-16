package com.jakespringer.reagne.test;

import org.lwjgl.input.Keyboard;
import com.jakespringer.reagne.Reagne;
import com.jakespringer.reagne.fundamental.Observable;
import com.jakespringer.reagne.input.Input;
import com.jakespringer.reagne.math.Vec2;

public class Movement {
    public static void makePositionUpdateSystem(Observable<Vec2> position, Observable<Vec2> velocity) {
        Reagne.continuous.trigger(position, () -> position.get().add(velocity.get().multiply(Reagne.dt())));
    }
    
    public static void makeWasdVelocitySystem(Observable<Vec2> wasd, final double SPEED_COEFFICIENT) {
        Input.whileKeyDown(Keyboard.KEY_W).trigger(wasd, () -> wasd.get().add(new Vec2(0., Reagne.dt()*SPEED_COEFFICIENT)));
        Input.whileKeyDown(Keyboard.KEY_A).trigger(wasd, () -> wasd.get().add(new Vec2(-Reagne.dt()*SPEED_COEFFICIENT, 0.)));
        Input.whileKeyDown(Keyboard.KEY_S).trigger(wasd, () -> wasd.get().add(new Vec2(0., -Reagne.dt()*SPEED_COEFFICIENT)));
        Input.whileKeyDown(Keyboard.KEY_D).trigger(wasd, () -> wasd.get().add(new Vec2(Reagne.dt()*SPEED_COEFFICIENT, 0.)));
    }
    
    public static void makeFrictionSystem(Observable<Vec2> friction) {
        Reagne.continuous.trigger(friction, () -> friction.get().multiply(0.9));
    }
    
    private Movement() {}
}
