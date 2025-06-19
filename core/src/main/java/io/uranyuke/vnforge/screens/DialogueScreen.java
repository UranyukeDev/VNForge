package io.uranyuke.vnforge.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Align;
import io.uranyuke.vnforge.VNGame;
import io.uranyuke.vnforge.model.DialogueModels.*;
import io.uranyuke.vnforge.util.ScriptLoader;

public class DialogueScreen extends ScreenAdapter {

    // ---------- constants ----------
    private static final float MARGIN = 30f;   // inner padding in px

    // ---------- engine ----------
    private final VNGame game;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font;

    // ---------- data ----------
    private final Script script;
    private int sceneIdx = 0;
    private int lineIdx  = 0;
    private float elapsed = 0f;     // seconds spent on current line
    private String currentText = "";

    // ---------- graphics ----------
    private Texture background;
    private final Texture textbox;
    private float boxX, boxY, boxW, boxH;      // textbox placement

    public DialogueScreen(VNGame g, String scriptFile) {
        this.game  = g;
        this.font  = g.uiFont;
        this.script = ScriptLoader.load(scriptFile);

        // UI skin
        textbox = game.assets.get("gfx/ui/textbox.png", Texture.class);
        boxW = textbox.getWidth();
        boxH = textbox.getHeight();
        calculateBoxPosition();

        loadScene();
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------
    private void calculateBoxPosition() {
        boxX = (Gdx.graphics.getWidth() - boxW) / 2f; // centre-horiz
        boxY = 0f;                                    // flush bottom
    }

    /** pull the next scene's background and reset counters */
    private void loadScene() {
        Scene sc = script.scenes.get(sceneIdx);

        if (background != null) background.dispose();
        background = new Texture(Gdx.files.internal("gfx/backgrounds/" + sc.background));

        lineIdx  = 0;
        elapsed  = 0f;
        currentText = "";
    }

    @Override
    public void resize(int width, int height) {
        calculateBoxPosition();   // stay centred on window resize
    }

    @Override
    public void render(float delta) {
        handleInput();

        // progress typewriter
        Scene sc = script.scenes.get(sceneIdx);
        Line  ln = sc.lines.get(lineIdx);
        elapsed += delta;
        int lettersVisible = Math.min(ln.text.length(), (int) (elapsed * game.config.textSpeed));
        currentText = ln.text.substring(0, lettersVisible);

        // draw ----------------------------------------------------------------
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // textbox
        batch.draw(textbox, boxX, boxY);

        // speaker (if any)
        if (ln.speaker != null) {
            font.setColor(Color.valueOf(ln.color != null ? ln.color : game.config.nameColor));
            font.draw(batch, ln.speaker, boxX + MARGIN, boxY + boxH - MARGIN);
        }

        // main text
        font.setColor(Color.WHITE);
        font.draw(batch, currentText,
                  boxX + MARGIN,              // x
                  boxY + boxH - MARGIN * 2,   // y
                  boxW - MARGIN * 2,          // wrap width
                  Align.left,
                  true);

        batch.end();
    }

    // ------------------------------------------------------------------
    // user input
    // ------------------------------------------------------------------
    private void handleInput() {
        boolean nextRequested = Gdx.input.justTouched()
                             || Gdx.input.isKeyJustPressed(Keys.SPACE);

        if (!nextRequested) return;

        Scene sc = script.scenes.get(sceneIdx);
        Line  ln = sc.lines.get(lineIdx);

        // fast-forward if text still rolling
        if (currentText.length() < ln.text.length()) {
            elapsed = ln.text.length() / game.config.textSpeed;
            return;
        }

        // otherwise proceed
        lineIdx++;
        if (lineIdx >= sc.lines.size()) {
            sceneIdx++;
            if (sceneIdx >= script.scenes.size()) {
                game.setScreen(new MainMenuScreen(game));   // back to menu
                return;
            }
            loadScene();
        } else {
            elapsed = 0f;
            currentText = "";
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (background != null) background.dispose();
    }
}
