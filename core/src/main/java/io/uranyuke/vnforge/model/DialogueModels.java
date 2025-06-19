package io.uranyuke.vnforge.model;

import java.util.List;

public class DialogueModels {

    /** one sentence in the script */
    public static class Line {
        public String speaker; // null = narration
        public String color;   // optional hex, overrides default
        public String text;
    }

    /** a group of lines that share one background */
    public static class Scene {
        public String background;    // path under images/backgrounds
        public List<Line> lines;
    }

    /** top-level JSON object */
    public static class Script {
        public List<Scene> scenes;
    }
}

