package fr.kwizzy.configurator;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.function.Consumer;

public class StaticConfigurator implements IConfigurator {

    private Class<? extends Config> current;
    private String name;
    private String directory;

    public StaticConfigurator(Class<? extends Config> t) {
        this.current = t;
        this.directory = "config/";
        this.name = t.getSimpleName();
    }

    public StaticConfigurator(Class<? extends Config> t, String name) {
        this.current = t;
        this.directory = "config/";
        this.name = directory + name;
    }

    public StaticConfigurator setName(String name) {
        this.name = name;
        return this;
    }

    public StaticConfigurator setDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    @Override
    public void load() {
        if (exist()) {
            fillClass();
            writeFileFromClass();
        }
        else

            try {
                directory().mkdirs();
                System.out.println(config().getAbsolutePath());
                config().createNewFile();
                writeFileFromClass();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public String getConfigName() {
        return this.name;
    }

    @Override
    public File directory() {
        return new File(this.directory);
    }

    @Override
    public Class getClassz() {
        return current;
    }

    private void writeFileFromClass() {
        JSONObject jsonObject = new JSONObject();
        forEachStaticFields(f -> {
            try {
                f.setAccessible(true);
                jsonObject.put(f.getName(), f.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        Util.writeInFile(jsonObject, config());
    }

    private void fillClass() {
        Optional<JSONObject> config = Util.getJsonFromFile(config());
        config.ifPresent(jsonObject -> forEachStaticFields(f -> {
            try {
                if (config.get().has(f.getName()))
                {
                    f.setAccessible(true);
                    f.set(null, jsonObject.get(f.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }));
    }

    private void forEachStaticFields(Consumer<Field> f) {
        Field[] declaredFields = current.getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                f.accept(field);
            }
        }
    }

    @Override
    public void save() {
        writeFileFromClass();
    }
}
