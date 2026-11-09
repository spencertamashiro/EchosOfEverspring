package dev.tentativerpg.npc;

import java.util.List;
import java.util.Locale;
import net.neoforged.neoforge.common.ModConfigSpec;

public enum RelicTheme {
    ASTRAL(0xFFBDA6FF, 0xFF7054AF, 0xFF18122D,
            "  #  ", " #+# ", "#+++#", " #+# ", "  #  "),
    TIDE(0xFF82EADC, 0xFF377B79, 0xFF0D2127,
            "  ## ", " #++#", "#++# ", "#+# #", " ##  "),
    CINDER(0xFFFFAF74, 0xFFA9573D, 0xFF2A161A,
            "  #  ", "  ## ", " #+##", "#+++#", " ### "),
    AURORA(0xFFBEF6CA, 0xFF6A89B0, 0xFF142132,
            "#   #", "## ##", "#+#+#", " #+# ", "  #  "),
    CLOCKWORK(0xFFF5D18C, 0xFF977749, 0xFF242019,
            " # # ", "#####", "## ##", "#####", " # # ");

    static final ModConfigSpec SPEC;
    private static final ModConfigSpec.EnumValue<RelicTheme> DEFAULT_FRAME;
    private static final ModConfigSpec.ConfigValue<String> DEFAULT_ACCENT;
    private static final ModConfigSpec.ConfigValue<String> DEFAULT_BORDER;
    private static final ModConfigSpec.ConfigValue<String> DEFAULT_BACKGROUND;
    private static final ModConfigSpec.ConfigValue<String> DEFAULT_TEXT;
    static {
        var builder = new ModConfigSpec.Builder();
        DEFAULT_FRAME = builder.comment("Default dialogue frame for NPCs without a frame in their JSON.")
                .defineEnum("defaultFrame", ASTRAL);
        builder.comment("Optional #RRGGBB overrides for NPCs using the default frame. An empty string uses the preset.")
                .push("defaultColors");
        DEFAULT_ACCENT = defaultColor(builder, "accent");
        DEFAULT_BORDER = defaultColor(builder, "border");
        DEFAULT_BACKGROUND = defaultColor(builder, "background");
        DEFAULT_TEXT = defaultColor(builder, "text");
        builder.pop();
        SPEC = builder.build();
    }

    private static ModConfigSpec.ConfigValue<String> defaultColor(ModConfigSpec.Builder builder, String name) {
        return builder.define(name, "", value -> value instanceof String color
                && (color.isEmpty() || color.matches("#[0-9a-fA-F]{6}")));
    }

    public record Palette(int accent, int border, int background, int text) {}

    record ColorOverrides(String accent, String border, String background, String text) {
        Palette apply(Palette base) {
            return new Palette(color("accent", accent, base.accent()), color("border", border, base.border()),
                    color("background", background, base.background()), color("text", text, base.text()));
        }

        private static int color(String field, String value, int fallback) {
            if (value == null) {
                return fallback;
            }
            if (!value.matches("#[0-9a-fA-F]{6}")) {
                throw new IllegalArgumentException("colors." + field + " must be a #RRGGBB color");
            }
            return 0xFF000000 | Integer.parseInt(value.substring(1), 16);
        }
    }

    final Palette palette;
    private final List<String> seal;

    public List<String> seal() {
        return seal;
    }

    RelicTheme(int accent, int border, int background, String... seal) {
        this.palette = new Palette(accent, border, background, 0xFFF5EEE9);
        this.seal = List.of(seal);
    }

    static RelicTheme parse(String frame) {
        try {
            return valueOf(frame.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("unknown frame: " + frame);
        }
    }

    static RelicTheme configuredFrame(String frame) {
        return frame == null ? DEFAULT_FRAME.get() : parse(frame);
    }

    Palette colors(ColorOverrides overrides) {
        return overrides == null ? palette : overrides.apply(palette);
    }

    Palette configuredColors(String explicitFrame, ColorOverrides overrides) {
        Palette base = explicitFrame == null
                ? new ColorOverrides(orUnset(DEFAULT_ACCENT.get()), orUnset(DEFAULT_BORDER.get()),
                        orUnset(DEFAULT_BACKGROUND.get()), orUnset(DEFAULT_TEXT.get())).apply(palette)
                : palette;
        return overrides == null ? base : overrides.apply(base);
    }

    private static String orUnset(String color) {
        return color.isEmpty() ? null : color;
    }
}
