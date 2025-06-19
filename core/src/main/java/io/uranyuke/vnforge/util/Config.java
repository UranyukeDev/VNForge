package io.uranyuke.vnforge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.graphics.Color;

public class Config {

    /* ---------- JSON-driven fields ---------- */
    public String font             = "fonts/Roboto-Medium.ttf";
    public int    fontSize         = 26;

    public String speakerFont      = "fonts/Roboto-Bold.ttf";
    public int    speakerFontSize  = 32;

    public String nameColor        = "#ffc000";
    public float  textSpeed        = 40f;

    public static Config load(FileHandle file) {
        return file.exists()
             ? new Json().fromJson(Config.class, file)
             : new Config();
    }

    public BitmapFont createBodyFont()     { return build(font,        fontSize); }
    public BitmapFont createSpeakerFont()  { return build(speakerFont, speakerFontSize); }

    public static BitmapFont generateFont(Config cfg) {
        return cfg.createBodyFont();
    }
    public static BitmapFont generateSpeakerFont(Config cfg) {
        return cfg.createSpeakerFont();
    }

    /* ---------- private factory ---------- */
    private static BitmapFont build(String path, int size) {
        FreeTypeFontGenerator gen =
            new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter p =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size  = size;
        p.color = Color.WHITE;
        BitmapFont f = gen.generateFont(p);
        gen.dispose();
        return f;
    }
}
