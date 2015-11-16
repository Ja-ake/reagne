package com.jakespringer.reagne.res;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sprite implements Medium {
    private final List<Texture> animation = new ArrayList<>();
    private int numFrames;

    /*package*/ Sprite(int animationFrames) {
        numFrames = animationFrames;
    }
    
    @Override
    public void load(File validFile) {
        try {
            animation.clear();
            animation.addAll(TextureLoader.getTexture(validFile.getAbsolutePath(), numFrames));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
