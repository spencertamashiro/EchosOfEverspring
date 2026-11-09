package dev.tentativerpg.tooltip;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class GearDataTest {
    private final Gear example=new Gear("example","Ashen Promise",Gear.Kind.LONGSWORD,Gear.Rarity.MYTHIC,Gear.Role.WARRIOR,"Fire",28,20,10,0,0,Gear.Utility.FIRE,"A promise in embers.","cinder");
    @Test void standaloneMetadataRoundTrips(){assertEquals(example,GearData.parse(GearData.JSON.toJson(example)));}
    @Test void malformedMetadataFallsBackAndDoesNotThrow(){
        for(String bad:new String[]{"", "null", "[]", "{}", "broken", "x".repeat(4097)})assertNull(GearData.parse(bad));
        var tree=GearData.JSON.toJsonTree(example).getAsJsonObject();
        tree.addProperty("style","rarity");assertNull(GearData.parse(tree.toString()));
        tree.addProperty("style","cinder");tree.addProperty("typo",true);assertNull(GearData.parse(tree.toString()));
    }
}
