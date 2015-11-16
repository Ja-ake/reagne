package com.jakespringer.reagne.test;

import com.jakespringer.reagne.Reagne;
import com.jakespringer.reagne.fx.Window;
import com.jakespringer.reagne.game.World;
import com.jakespringer.reagne.res.Resources;
import java.io.File;

public class PositionTest {

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());

        final World world = new World();

        Window.initialize(1200, 800, "Reagen Test");
        world.add(new Player());

        System.out.println(Resources.getSprite("levels.test").get());
        
        Reagne.run();
    }
}
