package com.jakespringer.reagne.fx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.jakespringer.reagne.Reagne;
import com.jakespringer.reagne.res.Texture;
import com.jakespringer.reagne.res.TextureLoader;

public abstract class SpriteContainer {

	private static HashMap<String, ArrayList<Texture>> spriteMap = new HashMap();
	private static String path = Reagne.getResourceFolder() + "sprite/";
	private static String type = ".png";

	public static ArrayList<Texture> loadSprite(String name, int n) throws IOException {
		if (spriteMap.containsKey(name)) { return spriteMap.get(name); }
		spriteMap.put(name, TextureLoader.getTexture(path + name + type, n));
		return spriteMap.get(name);
	}
}
