package io.uranyuke.vnforge.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import io.uranyuke.vnforge.model.DialogueModels.Script;

public class ScriptLoader {

    public static Script load(String path) {
        FileHandle fh = com.badlogic.gdx.Gdx.files.internal("scripts/" + path);
        return new Json().fromJson(Script.class, fh);
    }
}

