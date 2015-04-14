package com.gdp.thirtysecondsnippets.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdp.thirtysecondsnippets.TSS;
import com.gdp.thirtysecondsnippets.ThirtySecondSnippets;

public class DesktopLauncher { 
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=TSS.WIDTH; // sets window width
                config.height=TSS.HEIGHT;  // sets window height
                new LwjglApplication(new TSS(), config);
	}
}
