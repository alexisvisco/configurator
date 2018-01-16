package fr.kwizzy.configurator;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

public class ObjectConfigurator implements IConfigurator {

    private Class<? extends Config> current;
    private String name;
    private String directory;

    public ObjectConfigurator(Class<? extends Config> t) {
        this.current = t;
        this.directory = "config/";
        this.name =  t.getSimpleName();
    }

    public ObjectConfigurator(Class<? extends Config> t, String name) {
        this.current = t;
        this.directory = "config/";
        this.name = name;
    }

    public ObjectConfigurator setName(String name) {
        this.name = name;
        return this;
    }

    public ObjectConfigurator setDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    @Override
    public void load() {
        try {
            Field instance = current.getField("instance");
            if (Modifier.isStatic(instance.getModifiers()) && Modifier.isPublic(instance.getModifiers()))
            {
                if (exist()) {
                    fillInstance();
                    writeFileFromInstance();
                }
                else {
                    directory().mkdirs();
                    config().createNewFile();
                    writeFileFromInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            writeFileFromInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class getClassz() {
        return this.current;
    }

    @Override
    public String getConfigName() {
        return this.name;
    }

    @Override
    public File directory() {
        return new File(this.directory);
    }

    private void fillInstance() {
        Optional<JSONObject> j = Util.getJsonFromFile(config());
        j.ifPresent(e -> {

            try {
                Object c = Util.gson.fromJson(e.toString(), current.getField("instance").getGenericType());
                Field instance = current.getField("instance");
                instance.set(null, c);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void writeFileFromInstance() throws Exception {
        Field instance = current.getField("instance");
        String s = Util.gson.toJson(instance.get(null));
        Util.writeInFile(s, config());
    }
}
