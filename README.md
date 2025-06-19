# VNForge – Visual Novel Engine (libGDX / Java)

A plug‑and‑play visual‑novel (VN) engine built on **libGDX 1.12**. Still in development.

---

## Core features

| Capability | Details |
|------------|---------|
| **Dialogue flow** | Typewriter text with per‑line speed (`textSpeed`), tap/space to advance. |
| **Speaker name** | Rendered in a *dedicated, larger* font; colour sticks until the next `color` override. |
| **Append or clear** | `"append": true` continues the current paragraph on the **same line**; omit or set to `false` to clear. |
| **Scenes / backgrounds** | Each scene specifies one background image (`gfx/backgrounds/…`). The engine swaps textures automatically and disposes the old one. |
| **Config‑driven fonts** | `assets/config/config.json` picks body/speaker TTFs, sizes, default name colour, and type speed, no recompilation. |

---

## Configuration (`assets/config/config.json`)

```jsonc
{
  "font"           : "fonts/Roboto-Medium.ttf",   // dialogue text
  "fontSize"       : 26,
  "speakerFont"    : "fonts/Roboto-Bold.ttf",    // speaker label
  "speakerFontSize": 32,
  "nameColor"      : "#ffc000",                  // default label colour
  "textSpeed"      : 40                           // letters per second
}
```

---

## Writing scripts (`assets/scripts/*.json`)

Minimal schema (see `DialogueModels.java`):

```jsonc
{
  "scenes": [
    {
      "background": "easterneurope.png",
      "lines": [
        { "append": true, "speaker": "Alice", "text": "Welcome to VNForge!" },
        { "append": false, "speaker": "Alice", "text": "Let's build something great." },
        { "color": "#66ccff", "speaker": "Bob", "text": "I'm in!" },
        { "text": "They shook hands." }
      ]
    }
  ]
}
```

Field | Type | Purpose
------|------|--------
`background` | *String* | Filename of scene BG (relative to `gfx/backgrounds`).
`speaker`    | *String?* | Name tag. Omit → narration line.
`color`      | *String?* | Hex colour for **this** speaker label *and all following* until changed.
`text`       | *String* | Dialogue content.
`append`     | *Boolean?* | `true` → continue on same paragraph; set to `false` to stop.

---

## To be implemented

* **Choices / branching:** add `choices` array to `Line` and let `DialogueScreen` push a `ChoiceScreen`.
* **Save & load:** serialise `sceneIdx`, `lineIdx`, `bufferText`, colours.
* **CG gallery / music:** preload additional assets in `VNGame.assets`.
* **Auto‑mode / skip:** vary text reveal speed, auto‑advance timer.
* **Transitions:** 
...

