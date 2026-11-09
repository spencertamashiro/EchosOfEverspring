package dev.tentativerpg.client.tooltip;

import dev.tentativerpg.TentativeRpg;
import dev.tentativerpg.tooltip.Gear;
import dev.tentativerpg.tooltip.GearData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

@Mod(value = TentativeRpg.ID, dist = Dist.CLIENT)
public final class RpgClient {
    public RpgClient() {
        NeoForge.EVENT_BUS.addListener(RpgClient::tooltip);
        NeoForge.EVENT_BUS.addListener(RpgClient::text);
        NeoForge.EVENT_BUS.addListener(ContainerThemes::background);
    }
    private static void tooltip(RenderTooltipEvent.Pre event) {
        Gear gear=GearData.read(event.getItemStack());
        if(gear==null)return;
        // Tooltip Overhaul replays this public event before drawing. Only our tagged items are handled.
        TooltipCards.render(event.getGraphics(),event.getItemStack(),gear,event.getX(),event.getY(),event.getScreenWidth(),event.getScreenHeight());
        event.setCanceled(true);
    }
    private static void text(ItemTooltipEvent event) {
        Gear g=GearData.read(event.getItemStack());if(g==null)return;
        // Accessible/searchable text remains available to narration and other inventory tools.
        var lines=event.getToolTip();lines.clear();
        lines.add(Component.literal(g.name()).withStyle(s->s.withColor(g.rarity().color)));
        lines.add(Component.literal(Gear.label(g.rarity())+" "+Gear.label(g.type())).withStyle(ChatFormatting.GRAY));
        lines.add(Component.literal(Gear.label(g.role())+" - "+g.element()));
        lines.add(Component.literal(g.type()==Gear.Kind.ARMOR?"Armor: "+g.armor():"Damage: "+TooltipCards.number(g.damage())+" | Crit: "+TooltipCards.number(g.crit())+"%"));
        if(g.stamina()>0)lines.add(Component.literal("Stamina: "+g.stamina()));
        if(g.mana()>0)lines.add(Component.literal("Mana: "+g.mana()));
        if(g.utility()!=Gear.Utility.NONE)lines.add(Component.literal(g.utility().description));
        lines.add(Component.literal(g.lore()).withStyle(ChatFormatting.GRAY));
    }
}
