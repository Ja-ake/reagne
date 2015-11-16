package com.jakespringer.reagne.test;

import org.lwjgl.input.Keyboard;
import com.jakespringer.reagne.Reagne;
import com.jakespringer.reagne.fundamental.Observable;
import com.jakespringer.reagne.fx.Graphics2D;
import com.jakespringer.reagne.game.AbstractEntity;
import com.jakespringer.reagne.input.Input;
import com.jakespringer.reagne.math.Color4;
import com.jakespringer.reagne.math.Vec2;

public class Player extends AbstractEntity {

    private final double SPEED_COEFFICIENT = 0.002;

    @Override
    public void create() {
        Observable<Vec2> velocity = new Observable<Vec2>(Vec2.ORIGIN);
        Observable<Vec2> position = new Observable<Vec2>(Vec2.ORIGIN);
        
        Movement.makePositionUpdateSystem(position, velocity);
        Movement.makeWasdVelocitySystem(velocity, SPEED_COEFFICIENT);
        Movement.makeFrictionSystem(velocity);
        
        mark(Reagne.continuous.forEach(dt -> Graphics2D.fillEllipse(position.get(), new Vec2(16, 16), Color4.RED, 20)));
        mark(Input.whenKeyReleased(Keyboard.KEY_Q).subscribe(() -> destroy()));
        mark(velocity, position);
    }
}
