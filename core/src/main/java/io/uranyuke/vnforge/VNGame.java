package io.uranyuke.vnforge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.uranyuke.vnforge.util.Config;
import io.uranyuke.vnforge.screens.MainMenuScreen;

public class VNGame extends Game {

    public AssetManager assets;
    public Config config;
    public BitmapFont uiFont;

    @Override
    public void create() {
        assets = new AssetManager();

        // ---------- load global config ----------
        FileHandle cfgFile = Gdx.files.internal("config/config.json");
        config = Config.load(cfgFile);

        // ---------- dynamic font ----------
        uiFont = Config.generateFont(config);

        // ---------- pre-load basic UI things ----------
        assets.load("gfx/ui/textbox.png", Texture.class);
        assets.finishLoading();

        // ---------- first screen ----------
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
        uiFont.dispose();
    }
}

