package dev.tentativerpg.client.npc;

import dev.tentativerpg.npc.DialogueNetwork;
import dev.tentativerpg.npc.NpcDialogue;
import dev.tentativerpg.npc.RelicTheme;

import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;

final class RelicDialogueScreen extends Screen {
    private final DialogueNetwork.View resident;
    private DialogueNetwork.Page page;
    private String speech;
    private final RelicTheme theme;
    private final RelicTheme.Palette palette;
    private List<String> lines = List.of();
    private int ticks;
    private boolean fullyRevealed;
    private int left;
    private int top;
    private int panelWidth;
    private int panelHeight;
    private int bodyBottom;

    RelicDialogueScreen(DialogueNetwork.Page page) {
        super(Component.literal(page.resident().name()));
        this.page = page;
        this.resident = page.resident();
        this.theme = resident.frame();
        this.palette = resident.palette();
        this.speech = page.text();
    }

    boolean isFor(DialogueNetwork.Page next) {
        return page.entityId() == next.entityId() && resident.equals(next.resident());
    }

    void update(DialogueNetwork.Page next) {
        page = next;
        speech = next.text();
        ticks = 0;
        fullyRevealed = false;
        wrapSpeech();
    }

    private void wrapSpeech() {
        lines = font.getSplitter().splitLines(Component.literal(speech), panelWidth - 30, Style.EMPTY)
                .stream().map(FormattedText::getString).toList();
    }

    @Override
    protected void init() {
        panelWidth = Math.min(width - 20, 272);
        var labels = resident.choices();
        var options = IntStream.range(0, labels.size()).filter(resident::hasChoice).toArray();
        int innerWidth = panelWidth - 26;
        int labelWidth = labels.stream().mapToInt(font::width).sum();
        int padding = (innerWidth - 4 * (options.length - 1) - labelWidth) / options.length;
        boolean wide = padding >= 8;
        panelHeight = wide ? 99 : 99 + 15 * (options.length - 1);
        left = (width - panelWidth) / 2;
        top = height - panelHeight - 9;
        bodyBottom = top + 69;
        wrapSpeech();

        int right = left + panelWidth;
        var close = new DialogueButton(right - 20, top + 8, 14, 13, "×", ignored -> onClose());
        close.setTooltip(Tooltip.create(Component.literal("Leave (Esc)")));
        addRenderableWidget(close);

        int nextX = left + 13;
        for (int index = 0; index < options.length; index++) {
            int option = options[index];
            String label = labels.get(option);
            int buttonWidth = wide ? font.width(label) + padding : Math.min(innerWidth, 154);
            int x = wide ? nextX : (width - buttonWidth) / 2;
            int y = bodyBottom + 4 + (wide ? 0 : index * 15);
            nextX += buttonWidth + 4;
            var button = new DialogueButton(x, y, buttonWidth, 14, label,
                    ignored -> ClientHooks.sendChoice(page, option));
            button.setTooltip(Tooltip.create(Component.literal(label)));
            addRenderableWidget(button);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && x >= left + 10 && x < left + panelWidth - 10
                && y >= top + 35 && y < bodyBottom) {
            fullyRevealed = true;
            return true;
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public void tick() {
        var actor = minecraft.level == null ? null : minecraft.level.getEntity(page.entityId());
        if (minecraft.player == null || actor == null
                || minecraft.player.distanceToSqr(actor) > NpcDialogue.RANGE_SQUARED
                || ++ticks > NpcDialogue.SESSION_TICKS) {
            onClose();
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int right = left + panelWidth;
        int bottom = top + panelHeight;
        g.fill(left + 3, top + 3, right + 3, bottom + 3, 0x66000000);
        g.fillGradient(left, top, right, bottom, palette.background(), darker(palette.background()));
        g.fill(left, top, right, top + 1, palette.border());
        g.fill(left, bottom - 1, right, bottom, palette.border());
        g.fill(left, top + 1, left + 1, bottom - 1, palette.border());
        g.fill(right - 1, top + 1, right, bottom - 1, palette.border());
        g.fillGradient(left + 2, top + 2, right - 2, top + 33, alpha(palette.border(), 62), 0x00000000);

        g.fillGradient(left + 8, top + 35, right - 8, bodyBottom, 0x66090D18, 0x98090D18);
        paintCorner(g, left + 3, top + 3, 1, 1);
        paintCorner(g, right - 4, top + 3, -1, 1);
        paintCorner(g, left + 3, bottom - 4, 1, -1);
        paintCorner(g, right - 4, bottom - 4, -1, -1);
        divider(g, top + 33);
        paintSeal(g, width / 2, bottom - 5);
        g.drawString(font, font.plainSubstrByWidth(getTitle().getString(), panelWidth - 39),
                left + 13, top + 9, palette.text(), false);
        g.drawString(font, font.plainSubstrByWidth(resident.captionOr("DIALOGUE"), panelWidth - 26),
                left + 13, top + 21, palette.accent(), false);
        paintSpeech(g);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void paintCorner(GuiGraphics g, int x, int y, int dx, int dy) {
        int arms = switch (theme) {
            case CINDER, CLOCKWORK -> 13;
            default -> 10;
        };
        for (int step = 0; step < arms; step++) {
            int inset = switch (theme) {
                case ASTRAL -> step < 3 ? 2 : step < 7 ? 1 : 0;
                case TIDE -> step < 4 ? 2 : step % 4 == 0 ? 1 : 0;
                case CINDER -> step < 3 ? 3 : step < 5 ? 2 : step < 8 ? 1 : 0;
                case AURORA -> step % 3;
                case CLOCKWORK -> step < 3 || step == 7 ? 2 : 0;
            };
            int color = step < 4 ? palette.accent() : alpha(palette.accent(), 143);
            pixel(g, x + dx * step, y + dy * inset, color);
            pixel(g, x + dx * inset, y + dy * step, color);
        }
        pixel(g, x + dx * 3, y + dy * 3, palette.accent());
    }

    private void divider(GuiGraphics g, int y) {
        int center = width / 2;
        g.fill(left + 11, y, center - 5, y + 1, alpha(palette.accent(), 87));
        g.fill(center + 6, y, left + panelWidth - 11, y + 1, alpha(palette.accent(), 87));
        pixel(g, center, y - 1, palette.accent());
        g.fill(center - 1, y, center + 2, y + 1, palette.accent());
        pixel(g, center, y + 1, palette.accent());
    }

    private void paintSeal(GuiGraphics g, int x, int y) {
        g.fill(x - 7, y - 4, x + 8, y + 4, 0xFF0C1019);
        for (int row = 0; row < theme.seal().size(); row++) {
            String pattern = theme.seal().get(row);
            for (int col = 0; col < pattern.length(); col++) {
                char mark = pattern.charAt(col);
                if (mark != ' ') {
                    pixel(g, x + col - 2, y + row - 3, mark == '+' ? palette.border() : palette.accent());
                }
            }
        }
    }

    private void paintSpeech(GuiGraphics g) {
        int remaining = fullyRevealed ? Integer.MAX_VALUE : Typewriter.revealed(ticks);
        int y = 0;
        float scale = Math.min(1f, 27f / Math.max(9, lines.size() * 9));
        g.pose().pushPose();
        try {
            g.pose().translate(left + 15, top + 40, 0);
            g.pose().scale(scale, scale, 1);
            for (String row : lines) {
                int length = Typewriter.length(row);
                String text = Typewriter.prefix(row, remaining);
                g.drawString(font, text, 0, y, palette.text(), false);
                if (remaining < length) {
                    if (Typewriter.cursorVisible(ticks)) {
                        int cursorX = font.width(text) + 1;
                        g.fill(cursorX, y + 7, cursorX + 3, y + 8, palette.accent());
                    }
                    break;
                }
                remaining -= length;
                y += 9;
            }
        } finally {
            g.pose().popPose();
        }
    }

    private static int darker(int color) {
        return 0xFF000000 | ((color & 0x00FEFEFE) >>> 1);
    }

    private static int alpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    private static void pixel(GuiGraphics g, int x, int y, int color) {
        g.fill(x, y, x + 1, y + 1, color);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        ClientHooks.stopVoice();
        super.removed();
    }

    private final class DialogueButton extends Button {
        DialogueButton(int x, int y, int width, int height, String text, OnPress action) {
            super(x, y, width, height, Component.literal(text), action, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
            int x = getX();
            int y = getY();
            boolean focus = isHoveredOrFocused();
            g.fill(x, y, x + getWidth(), y + getHeight(), focus ? palette.border() : alpha(palette.background(), 240));
            g.fill(x, y + getHeight() - 1, x + getWidth(), y + getHeight(),
                    focus ? palette.accent() : alpha(palette.accent(), 107));
            String text = font.plainSubstrByWidth(getMessage().getString(), getWidth() - 6);
            g.drawString(font, text, x + (getWidth() - font.width(text)) / 2, y + 3,
                    palette.text(), false);
        }
    }
}
