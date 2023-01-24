package top.yougi.classification.client;

import java.util.List;

public class ClientLevelData {
    private static List<String> classNames;

    public static void setClassNames(List<String> classNames) {
        ClientLevelData.classNames = classNames;
    }

    public static List<String> getClassNames() {
        return classNames;
    }
}
