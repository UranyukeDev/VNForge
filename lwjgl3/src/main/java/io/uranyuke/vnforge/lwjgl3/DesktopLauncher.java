package io.uranyuke.vnforge.lwjgl3;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.uranyuke.vnforge.VNGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("My Visual Novel");
        cfg.setWindowedMode(1280, 720);
        cfg.useVsync(true);
        new Lwjgl3Application(new VNGame(), cfg);
    }
}
