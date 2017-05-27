package com.teamcadia.mathcadia.desktop;

import Test.MapTest;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teamcadia.mathcadia.Mathcadia;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Mathcadia";
		config.width = 1000;
		config.height = 510;
		new LwjglApplication(new Mathcadia(), config);
	}
}
