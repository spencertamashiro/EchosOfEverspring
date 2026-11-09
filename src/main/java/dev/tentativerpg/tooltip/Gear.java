package dev.tentativerpg.tooltip;
import java.util.Locale;
import java.util.Set;
/** Immutable, bounded item definition shared by commands, items and rendering. */
public record Gear(String id, String name, Kind type, Rarity rarity, Role role,
 String element, double damage, double crit, int stamina, int mana,
 int armor, Utility utility, String lore, String style) {
 public enum Rarity {
  COMMON(0xB8C5CD), UNCOMMON(0x83CE9A), RARE(0x66B9F1), EPIC(0xBB8AF5), LEGENDARY(0xFFCC33), MYTHIC(0xEF3E36);
  public final int color; Rarity(int color) { this.color = color; }
 }
 public enum Role { WARRIOR, MAGE, ARCHER, PRIEST }
 public enum Kind {
  LONGSWORD, DAGGER, WARHAMMER, BATTLEAXE, BOW, CROSSBOW, STAFF, WAND, CATALYST, CHIME, SCYTHE, ARMOR;
  public boolean ranged() { return this == BOW || this == CROSSBOW; }
  public boolean magic() { return this == STAFF || this == WAND || this == CATALYST || this == CHIME; }
  public double speed() { return switch (this) { case DAGGER -> 2.6; case WARHAMMER -> 0.8; case BATTLEAXE, SCYTHE -> 1.1; default -> 1.6; }; }
 }
 public enum Utility {
  NONE(""), FIRE("Ignites targets for 4 seconds"), POISON("Poisons targets for 5 seconds"),
  FROST("Slows targets for 3 seconds"), LIFESTEAL("Restores 2 health on hit"),
  SHOCK("Weakens targets for 3 seconds"), HEAL("Restores 4 health on use");
  public final String description; Utility(String description) { this.description = description; }
 }
 public static final Set<String> STYLES = Set.of("rarity","cinder","glacier","tempest","eclipse","seraph",
  "venom","tide","astral","bloom","bloodmoon","meteor","sandstorm","aurora","clockwork",
  "soulfire","thunderhead","crystal","rift","dragon","requiem");
 public Gear {
  require(id != null && id.matches("[a-z0-9_]{1,48}"), "id: 1-48 lowercase letters, digits or underscores");
  require(name != null && !name.isBlank() && name.length() <= 48 && name.codePoints().noneMatch(Character::isISOControl), "name: 1-48 printable characters");
  require(type != null && rarity != null && role != null && utility != null, "type, rarity, role and utility are required");
  require(element != null && element.matches("[A-Za-z /-]{1,28}"), "element: 1-28 letters, spaces, / or -");
  require(Double.isFinite(damage) && damage >= 0 && damage <= 500, "damage must be 0-500");
  require(Double.isFinite(crit) && crit >= 0 && crit <= 100, "crit must be 0-100 percent");
  require(stamina >= 0 && stamina <= 100 && mana >= 0 && mana <= 100, "resource costs must be 0-100");
  require(armor >= 0 && armor <= 20, "armor must be 0-20");
  require(lore != null && lore.length() <= 240 && lore.codePoints().noneMatch(Character::isISOControl), "lore: at most 240 printable characters");
  require(style != null && STYLES.contains(style), "unknown tooltip style");
  require(rarity != Rarity.MYTHIC || !style.equals("rarity"), "Mythic items need a signature style");
 }
 private static void require(boolean condition, String message) { if (!condition) throw new IllegalArgumentException(message); }
 public static String label(Enum<?> value) { return label(value.name()); }
 public static String label(String value) { String s=value.toLowerCase(Locale.ROOT).replace('_',' '); return Character.toUpperCase(s.charAt(0))+s.substring(1); }
}
