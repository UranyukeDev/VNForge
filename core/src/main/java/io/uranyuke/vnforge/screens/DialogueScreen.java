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

    private static final float MARGIN        = 30f;   // padding inside textbox
    private static final float NAME_TEXT_GAP = 36f;   // gap under speaker name

    private final VNGame      game;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont  bodyFont;
    private final BitmapFont  nameFont;

    private Texture background;
    private final Texture textbox;

    /*  textbox placement (re-computed on resize)  */
    private float boxX, boxY, boxW, boxH;

    private final Script script;
    private int   sceneIdx  = 0;
    private int   lineIdx   = 0;
    private float elapsed   = 0f;       // seconds spent on current line

    private String bufferText = "";
    private String currentSegment = "";

    private Color currentNameColor;

    public DialogueScreen(VNGame g, String scriptFile) {
        this.game  = g;
        this.bodyFont = game.bodyFont;
        this.nameFont = game.nameFont;

        this.script = ScriptLoader.load(scriptFile);

        /* UI skin ----------------------------------------------------- */
        textbox = game.assets.get("gfx/ui/textbox.png", Texture.class);
        boxW = textbox.getWidth();
        boxH = textbox.getHeight();
        calculateBoxPosition();

        loadScene();
    }

    private void calculateBoxPosition() {
        boxX = (Gdx.graphics.getWidth() - boxW) / 2f; // centred horizontally
        boxY = 0f;                                    // flush with bottom
    }

    private void loadScene() {
        /* dispose previous background (avoid leaking GPU memory) */
        if (background != null) background.dispose();

        Scene sc = script.scenes.get(sceneIdx);
        background = new Texture(
                Gdx.files.internal("gfx/backgrounds/" + sc.background));

        lineIdx         = 0;
        elapsed         = 0f;
        bufferText      = "";
        currentSegment  = "";

        currentNameColor = Color.valueOf(game.config.nameColor); // reset colour
    }

    @Override
    public void resize(int width, int height) {
        calculateBoxPosition();      // keep textbox centred
    }

    @Override
    public void render(float delta) {
        handleInput();

        Scene sc = script.scenes.get(sceneIdx);
        Line  ln = sc.lines.get(lineIdx);

        elapsed += delta;
        int visibleChars = Math.min(
                ln.text.length(),
                (int) (elapsed * game.config.textSpeed));
        currentSegment = ln.text.substring(0, visibleChars);

        /* update sticky speaker colour if this line provides one */
        if (ln.color != null) {
            currentNameColor = Color.valueOf(ln.color);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        /* background stretched to window */
        batch.draw(background, 0, 0,
                   Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        /* textbox */
        batch.draw(textbox, boxX, boxY);

        /* speaker label */
        float nameY = boxY + boxH - MARGIN;
        if (ln.speaker != null) {
            nameFont.setColor(currentNameColor);
            nameFont.draw(batch, ln.speaker,
                          boxX + MARGIN, nameY);
        }

        /* dialogue body */
        float textY = nameY - NAME_TEXT_GAP;
        bodyFont.setColor(Color.WHITE);
        bodyFont.draw(batch, bufferText + currentSegment,
                      boxX + MARGIN, textY,
                      boxW - MARGIN * 2,
                      Align.left, true);

        batch.end();
    }

    private void handleInput() {
        boolean advance = Gdx.input.justTouched()
                        || Gdx.input.isKeyJustPressed(Keys.SPACE);
        if (!advance) return;

        Scene sc = script.scenes.get(sceneIdx);
        Line  ln = sc.lines.get(lineIdx);

        /* fast-forward current line if still typing */
        if (currentSegment.length() < ln.text.length()) {
            elapsed = ln.text.length() / game.config.textSpeed;
            return;
        }

        /* line finished — decide whether to append or clear */
        if (ln.append) {
            bufferText += ln.text + " ";
        } else {
            bufferText = "";
        }

        /* move to next line / scene / main menu */
        lineIdx++;
        if (lineIdx >= sc.lines.size()) {
            sceneIdx++;
            if (sceneIdx >= script.scenes.size()) {
                game.setScreen(new MainMenuScreen(game)); // VN finished
                return;
            }
            loadScene();
        } else {
            elapsed = 0f;
            currentSegment = "";
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (background != null) background.dispose();
    }
}

