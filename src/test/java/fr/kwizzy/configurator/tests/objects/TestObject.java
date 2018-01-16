package fr.kwizzy.configurator.tests.objects;

import fr.kwizzy.configurator.Config;

import java.util.Arrays;

public class TestObject implements Config {

    public static TestObject instance = new TestObject();

    public String name = "yolo";
    public int port = 9374;
    public String base = "ok";
    public boolean ok = true;
    public String[] values = {"hello", "world", "im", "reliable"};

    @Override
    public String toString() {
        return "TestObject{" +
                "name='" + name + '\'' +
                ", port=" + port +
                ", base='" + base + '\'' +
                ", ok=" + ok +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
