package com.jakespringer.reagne.res;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import com.jakespringer.reagne.Reagne;

public class Resources {
    private Resources() {}
    
    private static Map<String, Resource<?>> resourceCache = new HashMap<>();
    
    /**
     * Gets a resource in the default resource directory and returns a checked
     * file pointing to this resource.
     * 
     * Example: Resources.getResource("images.level2.goblin") will return a File
     * pointing to <your resource folder>/images/level2/goblin.<image file extension>.
     * The image file extension is determined automatically. If there are multiple files
     * with the same name but different extensions, see {@link Resources#getResourceFile(String, String)}.
     */
    public static File getResourceFile(String resourceName) {
        return getResourceFile(resourceName, null);
    }
    
    /**
     * Gets a resource in the default resource directory and returns a checked
     * file pointing to this resource with the file extension provided.
     * 
     * Example: Resources.getResource("images.level2.goblin", "png") will return a File
     * pointing to <your resource folder>/images/level2/goblin.png
     */
    public static File getResourceFile(String resourceName, String type) {
        if (type != null) {
            File suspectedFile = new File(Reagne.getResourceFolder(), resourceName.replace('.', '/'));
            if (!suspectedFile.exists() || suspectedFile.isDirectory()) {
                throw new InvalidParameterException("Resource points to a directory or does not exist");
            }
            return suspectedFile;
        } else {
            int lastIndex = resourceName.lastIndexOf('.');
            File suspectedDirectory = new File(Reagne.getResourceFolder(), resourceName.substring(0, lastIndex > 0 ? lastIndex : 0).replace('.', '/'));
            if (!suspectedDirectory.exists() || suspectedDirectory.isFile()) {
                throw new InvalidParameterException("Resource points to a file or does not exist");
            }
            
            File[] subfiles = suspectedDirectory.listFiles((f, s) -> {
                int lastDot = s.lastIndexOf('.');
                return s.substring(0, lastDot > 0 ? lastDot : s.length())
                        .equals(resourceName.substring(lastIndex > 0 ? lastIndex+1 : 0, resourceName.length()));
            });
                                    
            if (subfiles.length != 1) {
                throw new InvalidParameterException("Resource does not exist");
            }
            
            if (subfiles[0].isDirectory()) {
                throw new InvalidParameterException("Resource points to a directory");
            }
            
            return subfiles[0];
        }
    }
    
    public static Resource<Sprite> getSprite(String resourceName) {
        return getSprite(resourceName, 1);
    }
    
    @SuppressWarnings("unchecked")
    public static Resource<Sprite> getSprite(String resourceName, int numFrames) {
        Resource<Sprite> cached = null;
        try {
            cached = (Resource<Sprite>) resourceCache.get(resourceName);
        } catch (ClassCastException e) {
            throw new InvalidParameterException(resourceName + " is not a sprite");
        }
        
        if (cached != null) return cached;
        File file = getResourceFile(resourceName);
        Resource<Sprite> spriteResource = new Resource<>(file, new Sprite(numFrames));
        resourceCache.put(resourceName, spriteResource);
        return spriteResource;
    }
}
