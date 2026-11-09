package dev.tentativerpg.npc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

final class Cast {
    enum Line { GREETING, LORE, ACCEPTED, FINISHED, WAITING, REPORTED, ACTIVE, UNAVAILABLE, LOCKED, EXTRA_LORE }

    record Resident(String id, String name, String model, String greeting,
                    String acceptLabel, String loreLabel, String lore, String accepted,
                    String finished, int quest, String questTitle, Map<Line, String> responses,
                    String objective, String item, int count, String reward, int rewardCount,
                    String caption, String frame, RelicTheme.ColorOverrides colors, String turnInLabel, String extraLoreLabel,
                    String extraLore, Map<Line, String> sounds) {
        String tag() {
            return NpcDialogue.ID + "." + id;
        }

        ResourceLocation modelId() {
            return ResourceLocation.fromNamespaceAndPath("easy_npc", model);
        }

        boolean hasQuest() {
            return quest > 0;
        }

        String turnInText() {
            return Objects.requireNonNullElse(turnInLabel, "Turn in supplies");
        }

        DialogueNetwork.View view() {
            List<String> choices = hasQuest()
                    ? List.of(acceptLabel, loreLabel, turnInText())
                    : List.of("", loreLabel, Objects.requireNonNullElse(extraLoreLabel, ""));
            var theme = RelicTheme.configuredFrame(frame);
            return new DialogueNetwork.View(id, name, Objects.requireNonNullElse(caption, ""),
                    theme, theme.configuredColors(frame, colors), choices);
        }

        String sound(Line line) {
            return sounds == null ? "" : sounds.getOrDefault(line, "");
        }

        long questId(int suffix) {
            return 0x5267504E50430000L | ((long) quest << 4) | suffix;
        }

        String text(Line line) {
            if (responses != null && responses.containsKey(line)) {
                return responses.get(line);
            }
            return switch (line) {
                case GREETING -> greeting;
                case LORE -> lore;
                case EXTRA_LORE -> extraLore;
                case ACCEPTED -> accepted;
                case FINISHED -> finished;
                case WAITING -> "Bring the requested supplies here and try again.";
                case REPORTED -> "You already reported back. Your reward is in the quest book.";
                case ACTIVE -> "The order is already in your quest book.";
                case LOCKED -> "Accept the assignment first.";
                case UNAVAILABLE -> "I cannot find the order in the quest book just now.";
            };
        }
    }

    static final List<Resident> RESIDENTS = load();
    private static Map<String, Resident> catalog = index(List.of());

    static void installCustom(List<Resident> residents) {
        catalog = index(residents);
    }

    private static Map<String, Resident> index(List<Resident> custom) {
        var all = new LinkedHashMap<String, Resident>();
        RESIDENTS.forEach(resident -> all.put(resident.id(), resident));
        custom.forEach(resident -> all.put(resident.id(), resident));
        return Collections.unmodifiableMap(all);
    }

    static Set<String> ids() {
        return catalog.keySet();
    }

    static Resident byId(String id) {
        return catalog.get(id);
    }

    static Resident forEntity(Entity entity) {
        if (!BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals("easy_npc")) {
            return null;
        }
        String prefix = NpcDialogue.ID + ".";
        Resident found = null;
        for (String tag : entity.getTags()) {
            Resident candidate = tag.startsWith(prefix) ? byId(tag.substring(prefix.length())) : null;
            if (candidate != null) {
                if (found != null) {
                    return null;
                }
                found = candidate;
            }
        }
        return found;
    }

    private static List<Resident> load() {
        try (var reader = new InputStreamReader(Objects.requireNonNull(
                Cast.class.getResourceAsStream("/rpgtestingnpc/cast.json")), StandardCharsets.UTF_8)) {
            List<Resident> residents = new Gson().fromJson(reader, new TypeToken<List<Resident>>() {}.getType());
            return List.copyOf(residents);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read the RPGtestingNPC cast", exception);
        }
    }

    private Cast() {}
}
