package com.jakespringer.reagne.res;

import java.io.File;

public class Resource<M extends Medium> {
    private final File file;
    private final M medium;
    private boolean loaded;
    
    public Resource(File f, M m) {
        file = f;
        medium = m;
        loaded = false;
    }
    
    public M get() {
        if (!loaded) {
            medium.load(file);
            loaded = true;
        }
        
        return medium;
    }
}
