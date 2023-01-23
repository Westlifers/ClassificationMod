package top.yougi.classification.capability;

import net.minecraft.nbt.CompoundTag;

public class ChestCapability {
    private String ClassName = "";

    public String getClassName() {
        return this.ClassName;
    }

    public void setClassName(String className) {
        this.ClassName = className;
    }

    public void saveNBTData(CompoundTag nbt) {
        // BUG: save | load fail sometimes
        nbt.putString("ClassName", this.ClassName);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.ClassName = nbt.getString("ClassName");
    }
}
