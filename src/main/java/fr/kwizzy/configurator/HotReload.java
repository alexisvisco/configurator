package fr.kwizzy.configurator;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HotReload extends TimerTask {

    private static List<HotReload> hotReloads = new ArrayList<>();

    private Class t;
    private IConfigurator cfg;
    private Timer timer = null;
    private TimeUnit timeUnit;
    private long delay;
    private List<Consumer<HotReload>> actions = new ArrayList<>();
    private boolean debug = false;

    public HotReload(Class t, TimeUnit timeUnit, long delay) {
        this.t = t;
        this.timeUnit = timeUnit;
        this.delay = delay;
        setInfos();
    }

    private void setInfos() {
        Optional<IConfigurator> first = Configurator.configs.stream().filter(e -> e.getClassz() == this.t).findFirst();
        first.ifPresent(iConfigurator -> this.cfg = iConfigurator);
    }

    public void addAction(Consumer<HotReload> action) {
        actions.add(action);
    }

    public HotReload setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    @Override
    public void run() {
        cfg.load();
        actions.forEach(e -> e.accept(this));
        if (debug)
            System.out.println("Reload class/object: " + t.getSimpleName());
    }

    public void start() {
        if (timer == null)
        {
            if (debug)
                System.out.println("Start hot reloading for: " + t.getSimpleName());
            timer = new Timer();
            timer.schedule(this, timeUnit.toMillis(delay), timeUnit.toMillis(delay));
            hotReloads.add(this);
        }
    }

    public void stop() {
        if (timer != null) {
            if (debug)
                System.out.println("Stop hot reloading for: " + t.getSimpleName());
            timer.cancel();
            hotReloads.remove(this);
        }
    }

    public static void stopAll()
    {
        hotReloads.forEach(HotReload::stop);
        hotReloads.clear();
    }

    public static void debug() {
        System.out.println("Hotreloading classes:");
        hotReloads.forEach(e -> System.out.println("\t- " + e.t.getSimpleName() + " every " +
                e.timeUnit.toMinutes(e.delay) + " minutes"));
    }
}
