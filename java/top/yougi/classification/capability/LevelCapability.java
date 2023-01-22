package top.yougi.classification.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelCapability {
    private Map<String, List<String>> ClassMap = new HashMap<>();

    public Map<String, List<String>> getClassMap() {
        return this.ClassMap;
    }

    public void setClassMap(Map<String, List<String>> classMap) {
        this.ClassMap = classMap;
    }

    public void saveNBTData(CompoundTag nbt) {
        // 保存两个字段，都是列表，一个是classNames，一个是items
        ListTag classNamesTag = new ListTag();
        for (Map.Entry<String, List<String>> entry : ClassMap.entrySet()) {
            // 把值读成一个listTag
            ListTag listTag = new ListTag();
            for (String item: entry.getValue()) {
                listTag.add(StringTag.valueOf(item));
            }
            // 把listTag存到对应名称中
            classNamesTag.add(StringTag.valueOf(entry.getKey()));
            // 同时把名字也存进去，方便读取的时候遍历
            nbt.put(entry.getKey(), listTag);
        }
        nbt.put("classNames", classNamesTag);
    }

    public void loadNBTData(CompoundTag nbt) {
        Map<String, List<String>> classMap = new HashMap<>();
        for (int i=0; i<nbt.getList("classNames", Tag.TAG_STRING).size(); i++) {
            // 读一个名称
            String className = nbt.getList("classNames", Tag.TAG_STRING).get(i).getAsString();
            List<String> items = new ArrayList<>();
            // 把名称对应的物品都找出来，字符串存进列表
            for (Tag itemTag: nbt.getList(className, Tag.TAG_STRING)) {
                items.add(itemTag.getAsString());
            }

            classMap.put(className, items);
        }
        this.ClassMap = classMap;
    }
}
