package fr.kwizzy.configurator;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Configurator {

    private static List<IConfigurator> configs = new ArrayList<>();

    public static void registerClasses(String packageName, Function<Class<? extends Config>, IConfigurator> fn) {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
        Set<Class<? extends Config>> classes = reflections.getSubTypesOf(Config.class);
        classes.forEach(e -> configs.add(fn.apply(e)));
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
