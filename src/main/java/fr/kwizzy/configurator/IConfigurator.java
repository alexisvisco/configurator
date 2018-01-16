package fr.kwizzy.configurator;

import java.io.File;

public interface IConfigurator {

    void load();

    Class getClassz();

    String getConfigName();

    File directory();

    default File config() {
        return new File(directory().getPath() + File.separator + getConfigName() + ".json");
    }

    default boolean exist() {
        return (directory().exists() && config().exists());
    }

}
