package dev.tentativerpg.npc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.RecordComponent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.fml.loading.FMLPaths;

final class CustomNpcs {
    private static final Gson GSON = new Gson();
    private static final Set<String> FIELDS = Arrays.stream(Cast.Resident.class.getRecordComponents())
            .map(RecordComponent::getName).collect(Collectors.toUnmodifiableSet());

    static List<Cast.Resident> load() {
        Path folder = FMLPaths.CONFIGDIR.get().resolve("rpgtestingnpc/npcs");
        var result = new ArrayList<Cast.Resident>();
        var ids = new HashSet<String>();
        var questKeys = new HashSet<Integer>();
        for (var sample : Cast.RESIDENTS) {
            ids.add(sample.id());
            questKeys.add(sample.quest());
        }

        try {
            Files.createDirectories(folder);
            try (var files = Files.list(folder)) {
                for (Path file : files.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".json")).sorted().toList()) {
                    try (var reader = Files.newBufferedReader(file)) {
                        var json = JsonParser.parseReader(reader);
                        require(json.isJsonObject(), "expected one NPC object");
                        var object = json.getAsJsonObject();
                        // Version 1.3 definitions may still declare a presentation. All NPCs now use the same panel.
                        object.remove("style");
                        object.keySet().forEach(key -> require(FIELDS.contains(key), "unknown field: " + key));
                        validateColors(object);
                        validateInteger(object, "quest");
                        validateInteger(object, "count");
                        validateInteger(object, "rewardCount");
                        var npc = GSON.fromJson(object, Cast.Resident.class);
                        validate(npc);
                        if (ids.contains(npc.id())) {
                            throw new IllegalArgumentException("duplicate or reserved id: " + npc.id());
                        }
                        if (npc.hasQuest() && questKeys.contains(npc.quest())) {
                            throw new IllegalArgumentException("duplicate or reserved quest key: " + npc.quest());
                        }
                        ids.add(npc.id());
                        if (npc.hasQuest()) {
                            questKeys.add(npc.quest());
                        }
                        result.add(npc);
                    } catch (IOException | RuntimeException exception) {
                        NpcDialogue.LOGGER.error("Skipping invalid NPC definition {}: {}", file, exception.getMessage());
                    }
                }
            }
        } catch (IOException exception) {
            NpcDialogue.LOGGER.error("Could not read custom NPCs from {}", folder, exception);
        }
        NpcDialogue.LOGGER.info("Loaded {} custom RPGtestingNPC definitions from {}", result.size(), folder);
        return List.copyOf(result);
    }

    private static void validate(Cast.Resident npc) {
        require(npc != null, "expected one NPC object");
        require(npc.id() != null && npc.id().matches("[a-z0-9_]{1,32}"), "id must use 1–32 lowercase letters, digits or underscores");
        text(npc.name(), 40, "name");
        require(npc.model() != null && npc.model().matches("[a-z0-9_/]{1,64}")
                && BuiltInRegistries.ENTITY_TYPE.containsKey(npc.modelId()), "unknown Easy NPC model");
        text(npc.greeting(), 160, "greeting");
        text(npc.lore(), 160, "lore");
        text(npc.loreLabel(), 32, "loreLabel");
        optionalText(npc.caption(), 40, "caption");

        var theme = npc.frame() == null ? RelicTheme.ASTRAL : RelicTheme.parse(npc.frame());
        theme.colors(npc.colors());
        require(npc.quest() >= 0 && npc.quest() <= 4095, "quest must be omitted, 0 or a key from 13 to 4095");
        if (npc.hasQuest()) {
            require(npc.quest() >= 13, "custom quest keys 1–12 are reserved for the samples");
            text(npc.questTitle(), 80, "questTitle");
            text(npc.objective(), 200, "objective");
            text(npc.acceptLabel(), 32, "acceptLabel");
            optionalText(npc.turnInLabel(), 32, "turnInLabel");
            text(npc.accepted(), 160, "accepted");
            text(npc.finished(), 160, "finished");
            item(npc.item(), npc.count(), "item/count");
            if (npc.reward() != null) {
                item(npc.reward(), npc.rewardCount(), "reward/rewardCount");
            } else {
                require(npc.rewardCount() == 0, "rewardCount requires reward");
            }
            require(npc.extraLore() == null && npc.extraLoreLabel() == null,
                    "extraLore and extraLoreLabel are only for NPCs without a quest");
        } else {
            require(npc.questTitle() == null && npc.objective() == null && npc.item() == null && npc.count() == 0
                    && npc.reward() == null && npc.rewardCount() == 0 && npc.acceptLabel() == null
                    && npc.accepted() == null && npc.turnInLabel() == null && npc.finished() == null,
                    "quest fields require a quest key");
            if (npc.extraLore() != null || npc.extraLoreLabel() != null) {
                text(npc.extraLore(), 160, "extraLore");
                text(npc.extraLoreLabel(), 32, "extraLoreLabel");
            }
        }

        if (npc.responses() != null) {
            npc.responses().forEach((key, value) -> {
                require(key != null, "unknown responses key");
                text(value, 160, "responses." + key);
            });
        }
        if (npc.sounds() != null) {
            npc.sounds().forEach((key, value) -> {
                require(key != null, "unknown sounds key");
                require(resource(value) != null && value.length() <= 128, "invalid sound event for " + key);
            });
        }
    }

    private static void validateColors(JsonObject object) {
        var value = object.get("colors");
        if (value == null) {
            return;
        }
        require(value.isJsonObject(), "colors must be an object");
        value.getAsJsonObject().entrySet().forEach(entry -> {
            require(Set.of("accent", "border", "background", "text").contains(entry.getKey()),
                    "unknown colors field: " + entry.getKey());
            require(entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isString(),
                    "colors." + entry.getKey() + " must be a #RRGGBB color");
        });
    }

    private static void validateInteger(JsonObject object, String field) {
        var value = object.get(field);
        if (value == null) {
            return;
        }
        require(value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber(),
                field + " must be an integer");
        try {
            value.getAsBigDecimal().intValueExact();
        } catch (ArithmeticException | NumberFormatException exception) {
            throw new IllegalArgumentException(field + " must be an integer");
        }
    }

    private static void optionalText(String value, int limit, String field) {
        if (value != null) {
            text(value, limit, field);
        }
    }

    private static void text(String value, int limit, String field) {
        require(value != null && !value.isBlank() && value.length() <= limit
                && value.codePoints().noneMatch(Character::isISOControl),
                field + " must be 1–" + limit + " characters of single-line text");
    }

    private static void item(String value, int count, String field) {
        var id = resource(value);
        require(id != null && BuiltInRegistries.ITEM.getOptional(id).filter(item -> item != Items.AIR).isPresent()
                && count >= 1 && count <= 64, field + " must specify a registered item and a count from 1 to 64");
    }

    private static ResourceLocation resource(String value) {
        return value != null && value.contains(":") ? ResourceLocation.tryParse(value) : null;
    }

    private static void require(boolean condition, String reason) {
        if (!condition) {
            throw new IllegalArgumentException(reason);
        }
    }

    private CustomNpcs() {}
}
