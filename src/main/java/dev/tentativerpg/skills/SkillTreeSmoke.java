package dev.tentativerpg.skills;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.SkillsAPI;

@GameTestHolder("tentativerpg")
@PrefixGameTestTemplate(false)
public final class SkillTreeSmoke {
    @GameTest(template = "smoke_empty")
    public static void loadsAndSpends(GameTestHelper test) throws java.io.IOException {
        var categoryPath = ResourceLocation.fromNamespaceAndPath(
            "skill_tree_rpgs", "puffish_skills/categories/weapon_skills/category.json");
        try (var reader = test.getLevel().getServer().getResourceManager().openAsReader(categoryPath)) {
            var background = com.google.gson.JsonParser.parseReader(reader).getAsJsonObject().getAsJsonObject("background");
            test.assertTrue(background.get("texture").getAsString().equals("everspring_skills:textures/gui/weapon_atlas.png"),
                "Weapons tab should use the Everspring landscape");
        }
        for (String name : List.of("warrior", "mage", "archer", "priest")) {
            test.assertTrue(category(name).streamSkills().count() == 35, "Wrong node count for " + name);
        }
        var player = new FakePlayer(test.getLevel(), new GameProfile(UUID.randomUUID(), "EverspringTest"));
        var warrior = category("warrior");
        test.assertTrue(warrior.getPointsLeft(player) == 49, "Expected a 49-SP budget");
        skill(warrior, "path").unlock(player);
        skill(warrior, "endurance_training").unlock(player);
        var secondWind = skill(warrior, "second_wind");
        test.assertTrue(secondWind.getState(player) == Skill.State.LOCKED, "Second Wind requires two Tier I nodes");
        skill(warrior, "combat_footwork").unlock(player);
        test.assertTrue(secondWind.getState(player) == Skill.State.AFFORDABLE, "Second Wind should now be affordable");
        secondWind.unlock(player);
        test.assertTrue(warrior.getPointsLeft(player) == 45, "Expected a four-point purchase total");
        skill(warrior, "path_duelist").unlock(player);
        test.assertTrue(skill(warrior, "path_juggernaut").getState(player) == Skill.State.EXCLUDED, "Juggernaut must be excluded");
        for (String id : List.of("swift_hands", "deflection", "blade_dance", "perfect_form", "dance_of_steel")) {
            Skill next = skill(warrior, id);
            test.assertTrue(next.getState(player) == Skill.State.AFFORDABLE, "Expected a valid path to " + id);
            next.unlock(player);
        }
        test.assertTrue(skill(warrior, "master_riposteur").getState(player) == Skill.State.EXCLUDED, "Second Ascension must be excluded");
        test.succeed();
    }

    private static Category category(String name) {
        return SkillsAPI.getCategory(ResourceLocation.fromNamespaceAndPath("everspring_skills", name)).orElseThrow();
    }

    private static Skill skill(Category category, String id) {
        return category.getSkill(id).orElseThrow();
    }
}
