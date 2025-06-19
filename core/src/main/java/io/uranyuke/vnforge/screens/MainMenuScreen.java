package io.uranyuke.vnforge.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Input.Keys;

import io.uranyuke.vnforge.VNGame;

public class MainMenuScreen extends ScreenAdapter {

    private final VNGame game;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font;

    public MainMenuScreen(VNGame g) {
        this.game = g;
        this.font = g.bodyFont;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Press [ENTER] to start", 
                  0, Gdx.graphics.getHeight() / 2f, 
                  Gdx.graphics.getWidth(), Align.center, false);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            game.setScreen(new DialogueScreen(game, "prologue.json"));
        }
    }

    @Override
    public void dispose() { batch.dispose(); }
}

