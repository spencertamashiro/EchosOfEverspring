package dev.tentativerpg.client.tooltip;

import dev.tentativerpg.TentativeRpg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;

/** Cosmetic only: every menu retains its original slots, widgets, hit targets and dimensions. */
public final class ContainerThemes {
    private static final ResourceLocation ABYSS = ResourceLocation.fromNamespaceAndPath("tooltiprpg", "abyss");

    private static String theme() {
        var level = Minecraft.getInstance().level;
        return level != null && level.dimension().location().equals(ABYSS) ? "corrupt" : "peaceful";
    }

    public static void background(ContainerScreenEvent.Render.Background event) {
        var screen = event.getContainerScreen();
        GuiGraphics g = event.getGuiGraphics();
        int x=screen.getGuiLeft(), y=screen.getGuiTop(), w=screen.getXSize(), h=screen.getYSize();
        // Tiny rose-and-scroll inlays fit within the native rim. No extra trim
        // extends into the world, creative tabs, labels or slot hit targets.
        rose(g,"nw",x,y); rose(g,"ne",x+w-32,y);
        rose(g,"sw",x,y+h-32); rose(g,"se",x+w-32,y+h-32);
    }
    private static void rose(GuiGraphics g,String corner,int x,int y) {
        var id=TentativeRpg.id("textures/gui/themes/"+theme()+"/rose_"+corner+".png");
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        g.blit(id,x,y,0,0,32,32,32,32);
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
    }
    private ContainerThemes() {}
}
