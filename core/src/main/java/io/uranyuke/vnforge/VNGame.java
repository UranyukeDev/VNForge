package io.uranyuke.vnforge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;

import io.uranyuke.vnforge.util.Config;
import io.uranyuke.vnforge.screens.MainMenuScreen;

public class VNGame extends Game {

    public AssetManager assets;
    public Config       config;
    public BitmapFont   bodyFont;
    public BitmapFont   nameFont;

    @Override
    public void create() {
        assets = new AssetManager();
        config = Config.load(Gdx.files.internal("config/config.json"));

        bodyFont = Config.generateFont(config);          // dialogue text
        nameFont = Config.generateSpeakerFont(config);   // speaker labels

        assets.load("gfx/ui/textbox.png", Texture.class);
        assets.finishLoading();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
        bodyFont.dispose();
        nameFont.dispose();
    }
}

