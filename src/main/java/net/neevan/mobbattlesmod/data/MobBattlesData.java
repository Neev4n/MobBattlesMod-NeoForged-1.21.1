package net.neevan.mobbattlesmod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.UUID;

public class MobBattlesData extends SavedData {

    private boolean bossExists = false;
    private UUID bossUUID = null;


    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {

        tag.putBoolean("BossExists", bossExists);
        tag.putUUID("BossUUID", bossUUID);
        return tag;
    }

    public static MobBattlesData load(CompoundTag tag, HolderLookup.Provider provider){

        MobBattlesData data = new MobBattlesData();
        data.bossExists = tag.getBoolean("BossExists");
        data.bossUUID = tag.getUUID("BossUUID");
        return data;

    }

    public static final SavedData.Factory<MobBattlesData> FACTORY = new SavedData.Factory<>(
            MobBattlesData::new,
            MobBattlesData::load
    );

    public boolean bossExists() {
        return bossExists;
    }

    public void setBossExists(boolean bossExists) {
        this.bossExists = bossExists;
    }

    public UUID getBossUUID(){
        return bossUUID;
    }

    public void setBossUUID(UUID uuid){
        this.bossUUID = uuid;
    }

    public static MobBattlesData get(ServerLevel level){
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(FACTORY,"mob_battles_save_data");
    }
}
