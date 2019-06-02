package br.com.richardmartins.flywarriors.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import br.com.richardmartins.flywarriors.FlyWarriors;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1360;
        config.height = 768;
        new LwjglApplication(new FlyWarriors(), config);
    }
}
