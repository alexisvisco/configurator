package fr.kwizzy.configurator.tests.statics;

import fr.kwizzy.configurator.Config;

public class TestStatic implements Config {

    public static String name = "yolo";
    public static int port = 9374;
    public static String base = "ok";
    public static boolean ok = true;

    public static void toStrings() {
        System.out.println(name);
        System.out.println(port);
        System.out.println(base);
        System.out.println(ok);
    }
}
