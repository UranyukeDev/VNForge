package io.uranyuke.vnforge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;

public class Config {
    public String font = "fonts/default.ttf";
    public int fontSize = 28;
    public String nameColor = "#ffc000";
    public float textSpeed = 30f;

    /** load config.json (or fall back to default values) */
    public static Config load(FileHandle file) {
        if (!file.exists()) return new Config();
        return new Json().fromJson(Config.class, file);
    }

    /** build a BitmapFont with current settings */
    public static BitmapFont generateFont(Config cfg) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(cfg.font));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size  = cfg.fontSize;
        p.color = Color.WHITE;
        BitmapFont font = gen.generateFont(p);
        gen.dispose();
        return font;
    }
}
