package fr.kwizzy.configurator;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Configurator {

    static List<IConfigurator> configs = new ArrayList<>();

    public static void registerClasses(String packageName, Function<Class<? extends Config>, IConfigurator> fn) {
        try {
            ClassPath from = ClassPath.from(Thread.currentThread().getContextClassLoader());
            ImmutableList<ClassPath.ClassInfo> c = from.getTopLevelClassesRecursive(packageName).asList();
            c.forEach(e -> {
                Class<?> cl = e.load();
                if (Arrays.stream(cl.getInterfaces()).anyMatch(x -> x == Config.class))
                    configs.add(fn.apply((Class<? extends Config>) cl));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Optional<IConfigurator> getConfig(Class t)
    {
        return configs.stream().filter(e -> e.getClassz() == t).findFirst();
    }

    public static void registerClass(IConfigurator cfg) {
        configs.add(cfg);
    }

    public static void registerObjectClasses(String packageName) {
        registerClasses(packageName, ObjectConfigurator::new);
    }

    public static void registerStaticClasses(String packageName) {
        registerClasses(packageName, StaticConfigurator::new);
    }

    public static StaticConfigurator registerStaticClass(Class<? extends Config> classz) {
        StaticConfigurator staticConfigurator = new StaticConfigurator(classz);
        configs.add(staticConfigurator);
        return staticConfigurator;
    }

    public static ObjectConfigurator registerObjectClass(Class<? extends Config> classz) {
        ObjectConfigurator objectConfigurator = new ObjectConfigurator(classz);
        configs.add(objectConfigurator);
        return objectConfigurator;
    }

    public static void load(boolean debug)
    {
        configs.forEach(e -> {
            if (debug)
                System.out.println("Configurator load: " + e.getClassz().getSimpleName());
            e.load();
        });
    }

    public static void save(Class t)
    {
        configs.stream().filter(f -> f.getClassz() == t).forEach(IConfigurator::save);
    }

    public static void saveOnly(Predicate<IConfigurator> pre)
    {
        configs.stream().filter(pre).forEach(IConfigurator::save);
    }

}
