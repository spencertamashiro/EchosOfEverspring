package dev.tentativerpg.npc;

import dev.ftb.mods.ftblibrary.snbt.SNBT;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.neoforged.fml.loading.FMLPaths;

final class QuestChapters {
    private static final String OLD_SUBTITLE = "Accept work from the residents. Submit supplies in the quest book.";
    private static final String SUBTITLE = "Accept work from the residents and turn in supplies to them in person.";

    static void install() {
        var folder = FMLPaths.CONFIGDIR.get().resolve("ftbquests/quests");
        installChapter(folder.resolve("chapters/rpgtestingnpc_fieldwork.snbt"));
        installChapter(folder.resolve("chapters/rpgtestingnpc_beacon.snbt"));
        updateTranslations(folder.resolve("lang/en_us.snbt"));
    }

    private static void installChapter(Path chapter) {
        try (var source = Objects.requireNonNull(
                QuestChapters.class.getResourceAsStream("/rpgtestingnpc/" + chapter.getFileName()))) {
            Files.createDirectories(chapter.getParent());
            Files.copy(source, chapter);
        } catch (FileAlreadyExistsException existing) {
            migrateChapter(chapter);
        } catch (IOException exception) {
            NpcDialogue.LOGGER.error("Could not install RPGtestingNPC chapter {}", chapter, exception);
        }
    }

    private static void migrateChapter(Path path) {
        try {
            var chapter = SNBT.tryRead(path);
            if (chapter == null) {
                return;
            }
            boolean changed = replaceLine(chapter, "subtitle", OLD_SUBTITLE, SUBTITLE);
            for (var entry : chapter.getList("quests", Tag.TAG_COMPOUND)) {
                var quest = (CompoundTag) entry;
                for (var npc : Cast.RESIDENTS) {
                    if (id(npc.questId(2)).equalsIgnoreCase(quest.getString("id"))) {
                        for (var taskEntry : quest.getList("tasks", Tag.TAG_COMPOUND)) {
                            var task = (CompoundTag) taskEntry;
                            String type = task.getString("type");
                            if (id(npc.questId(3)).equalsIgnoreCase(task.getString("id"))
                                    && (type.isEmpty() || type.equals("item") || type.equals("ftbquests:item"))) {
                                task.putString("type", NpcSupplyTask.TYPE_ID);
                                changed = true;
                            }
                        }
                    }
                    if (id(npc.questId(4)).equalsIgnoreCase(quest.getString("id"))) {
                        changed |= replaceLine(quest, "description", oldReport(npc), report(npc));
                    }
                }
            }
            if (changed) {
                replaceFile(path, chapter);
            }
        } catch (IOException exception) {
            NpcDialogue.LOGGER.error("Could not migrate RPGtestingNPC chapter {}", path, exception);
        }
    }

    private static void updateTranslations(Path path) {
        if (!Files.isRegularFile(path)) {
            return;
        }
        try {
            var translations = SNBT.tryRead(path);
            if (translations == null) {
                return;
            }
            boolean changed = replaceLine(translations, "chapter.5267504E50430000.chapter_subtitle",
                    OLD_SUBTITLE, SUBTITLE);
            changed |= replaceLine(translations, "chapter.5267504E50430001.chapter_subtitle", OLD_SUBTITLE, SUBTITLE);
            for (var npc : Cast.RESIDENTS) {
                String key = "quest." + id(npc.questId(4)) + ".quest_desc";
                changed |= replaceLine(translations, key, oldReport(npc), report(npc));
            }
            if (changed) {
                replaceFile(path, translations);
            }
        } catch (IOException exception) {
            NpcDialogue.LOGGER.error("Could not update RPGtestingNPC turn-in instructions in {}", path, exception);
        }
    }

    private static boolean replaceLine(CompoundTag tag, String key, String before, String after) {
        var lines = tag.getList(key, Tag.TAG_STRING);
        if (lines.size() != 1 || !lines.getString(0).equals(before)) {
            return false;
        }
        lines.set(0, StringTag.valueOf(after));
        return true;
    }

    static String id(long id) {
        return String.format(Locale.ROOT, "%016X", id);
    }

    private static String oldReport(Cast.Resident npc) {
        return "Return to " + npc.name() + " and choose the report option.";
    }

    private static String report(Cast.Resident npc) {
        return "Bring the supplies to " + npc.name() + " and choose Turn in supplies.";
    }

    static void replaceFile(Path path, CompoundTag data) throws IOException {
        Path temp = Files.createTempFile(path.getParent(), "rpgtestingnpc-", ".tmp");
        try {
            SNBT.tryWrite(temp, data);
            try {
                Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private QuestChapters() {}
}
