package ru.myitschool.sungdx;

import static ru.myitschool.sungdx.MainGame.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(144);
		config.setTitle("Mihairu's food defense");
		config.setWindowedMode(SCR_WIDTH, SCR_HEIGHT);
		new Lwjgl3Application(new MainGame(), config);
	}
}
