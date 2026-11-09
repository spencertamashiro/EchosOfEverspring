package dev.tentativerpg.npc;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

final class NpcSupplyTask extends ItemTask {
    static final String TYPE_ID = NpcDialogue.ID + ":npc_supplies";
    private static TaskType type;

    static void register() {
        type = TaskTypes.register(NpcDialogue.id("npc_supplies"), NpcSupplyTask::new,
                () -> Icon.getIcon("minecraft:item/book"));
    }

    private NpcSupplyTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return type;
    }

    @Override
    public void onButtonClicked(Button button, boolean canClick) {}

    @Override
    public void addMouseOverText(TooltipList list, TeamData team) {
        if (!team.isCompleted(this)) {
            list.blankLine();
            list.add(Component.translatable("rpgtestingnpc.quest.turn_in_at_npc"));
        }
    }

    @Override
    public void submitTask(TeamData team, ServerPlayer player, ItemStack craftedItem) {}

    @Override
    public boolean canInsertItem() {
        return false;
    }

    @Override
    public ItemStack insert(TeamData team, ItemStack stack, boolean simulate) {
        return stack;
    }

    boolean turnIn(TeamData team, ServerPlayer player) {
        var inventory = player.getInventory();
        long required = Math.max(0, getMaxProgress() - team.getProgress(this));
        long remaining = required;
        Map<Integer, ItemStack> originals = new LinkedHashMap<>();
        for (int slot = 0; slot < inventory.getContainerSize() && remaining > 0; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && test(stack)) {
                originals.put(slot, stack);
                remaining -= stack.getCount();
            }
        }
        if (remaining > 0) {
            return false;
        }

        remaining = required;
        for (var entry : originals.entrySet()) {
            ItemStack stack = entry.getValue();
            int taken = (int) Math.min(remaining, stack.getCount());
            inventory.setItem(entry.getKey(), stack.copyWithCount(stack.getCount() - taken));
            remaining -= taken;
        }

        team.setProgress(this, getMaxProgress());
        boolean complete = team.isCompleted(this);
        if (!complete) {
            originals.forEach(inventory::setItem);
        }
        inventory.setChanged();
        player.containerMenu.broadcastChanges();
        return complete;
    }
}
