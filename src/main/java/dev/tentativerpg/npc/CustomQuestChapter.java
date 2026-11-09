package dev.tentativerpg.npc;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.neoforged.fml.loading.FMLPaths;

final class CustomQuestChapter {
    static boolean write(List<Cast.Resident> residents) {
        var path = FMLPaths.CONFIGDIR.get().resolve("ftbquests/quests/chapters/rpgtestingnpc_custom.snbt");
        var assignments = residents.stream().filter(Cast.Resident::hasQuest).toList();
        if (assignments.isEmpty() && !Files.exists(path)) {
            return true;
        }

        var chapter = new CompoundTag();
        chapter.putString("id", QuestChapters.id(0x5267504E50430002L));
        chapter.putString("filename", "rpgtestingnpc_custom");
        chapter.putString("title", "Custom NPC Assignments");
        chapter.putString("progression_mode", "linear");
        chapter.putString("default_quest_shape", "square");
        chapter.putBoolean("hide_quest_until_deps_complete", true);

        var quests = new ListTag();
        for (int index = 0; index < assignments.size(); index++) {
            appendAssignment(quests, assignments.get(index), (index / 12) * 10.0, index % 12 - 5.0);
        }
        chapter.put("quests", quests);
        try {
            Files.createDirectories(path.getParent());
            QuestChapters.replaceFile(path, chapter);
            return true;
        } catch (IOException exception) {
            NpcDialogue.LOGGER.error("Could not generate {}. Custom quest NPCs are disabled for this launch.",
                    path, exception);
            return false;
        }
    }

    private static void appendAssignment(ListTag quests, Cast.Resident npc, double x, double y) {
        var gate = quest(npc, 0, "Accept: " + npc.questTitle(), x - 3.0, y);
        gate.putBoolean("invisible", true);
        gate.putDouble("size", 0.6);
        gate.put("tasks", list(task(npc, 1, "custom", "Accept from " + npc.name())));
        quests.add(gate);

        var collect = quest(npc, 2, npc.questTitle(), x, y);
        collect.put("dependencies", list(StringTag.valueOf(QuestChapters.id(npc.questId(0)))));
        collect.put("description", list(StringTag.valueOf(npc.objective())));
        var supply = task(npc, 3, NpcSupplyTask.TYPE_ID, "");
        supply.put("item", item(npc.item()));
        supply.putLong("count", npc.count());
        supply.putBoolean("consume_items", true);
        collect.put("tasks", list(supply));
        quests.add(collect);

        var report = quest(npc, 4, "Turn in: " + npc.questTitle(), x + 3.0, y);
        report.put("dependencies", list(StringTag.valueOf(QuestChapters.id(npc.questId(2)))));
        report.put("description", list(StringTag.valueOf(
                "Bring the supplies to " + npc.name() + " and choose " + npc.turnInText() + ".")));
        report.put("tasks", list(task(npc, 5, "custom", "Turn in to " + npc.name())));
        if (npc.reward() != null) {
            var reward = task(npc, 6, "item", "");
            reward.put("item", item(npc.reward()));
            reward.putInt("count", npc.rewardCount());
            report.put("rewards", list(reward));
        }
        quests.add(report);
    }

    private static CompoundTag quest(Cast.Resident npc, int suffix, String title, double x, double y) {
        var quest = new CompoundTag();
        quest.putString("id", QuestChapters.id(npc.questId(suffix)));
        quest.putString("title", title);
        quest.putDouble("x", x);
        quest.putDouble("y", y);
        return quest;
    }

    private static CompoundTag task(Cast.Resident npc, int suffix, String type, String title) {
        var task = new CompoundTag();
        task.putString("id", QuestChapters.id(npc.questId(suffix)));
        task.putString("type", type);
        if (!title.isEmpty()) {
            task.putString("title", title);
        }
        return task;
    }

    private static CompoundTag item(String id) {
        var item = new CompoundTag();
        item.putString("id", id);
        item.putInt("count", 1);
        return item;
    }

    private static ListTag list(net.minecraft.nbt.Tag value) {
        var list = new ListTag();
        list.add(value);
        return list;
    }

    private CustomQuestChapter() {}
}
