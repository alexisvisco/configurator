import fr.kwizzy.configurator.Configurator;
import fr.kwizzy.configurator.HotReload;
import fr.kwizzy.configurator.tests.objects.TestObject;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        Configurator.registerObjectClasses("fr.kwizzy.configurator.tests.objects");
        Configurator.load(true);

        HotReload testObject = new HotReload(TestObject.class, TimeUnit.SECONDS, 10).setDebug(true);

        testObject.addAction(e -> System.out.println(TestObject.instance));
        testObject.start();
    }
}
