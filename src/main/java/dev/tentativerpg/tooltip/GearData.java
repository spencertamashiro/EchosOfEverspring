package dev.tentativerpg.tooltip;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Public tooltip metadata API. Attaching data does not change gameplay attributes. */
public final class GearData {
    public static final Gson JSON=new Gson();
    private static final Map<String,Gear> CACHE=new ConcurrentHashMap<>();
    public static Gear read(ItemStack stack){
        var data=stack.get(DataComponents.CUSTOM_DATA);
        return data==null?null:parse(data.copyTag().getString("tooltiprpg"));
    }
    public static Gear parse(String json){
        if(json==null || json.isEmpty() || json.length()>4096)return null;
        try {
            if(CACHE.size()>512)CACHE.clear();
            return CACHE.computeIfAbsent(json,GearData::parseGear);
        } catch(JsonParseException | IllegalArgumentException | IllegalStateException e){return null;}
    }
    public static Gear parseGear(String json){
        try {
            var object=com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            var fields=java.util.Set.of("id","name","type","rarity","role","element","damage","crit","stamina","mana","armor","utility","lore","style");
            for(String key:object.keySet())if(!fields.contains(key))throw new IllegalArgumentException("Unknown tooltip field: "+key);
            return JSON.fromJson(object,Gear.class);
        }
        catch(RuntimeException failure){
            for(Throwable cause=failure;cause!=null;cause=cause.getCause())
                if(cause instanceof IllegalArgumentException invalid)throw invalid;
            throw failure;
        }
    }
    public static CustomData data(Gear gear){
        CompoundTag tag=new CompoundTag();tag.putString("tooltiprpg",JSON.toJson(gear));return CustomData.of(tag);
    }
    public static void attach(ItemStack stack,Gear gear){
        var existing=stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag=existing==null?new CompoundTag():existing.copyTag();
        tag.putString("tooltiprpg",JSON.toJson(gear));stack.set(DataComponents.CUSTOM_DATA,CustomData.of(tag));
    }
    private GearData(){}
}
