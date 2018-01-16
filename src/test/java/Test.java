import fr.kwizzy.configurator.Configurator;
import fr.kwizzy.configurator.tests.objects.TestObject;
import fr.kwizzy.configurator.tests.statics.TestStatic;

public class Test {
    public static void main(String[] args) {
        Configurator.registerStaticClasses("fr.kwizzy.configurator.tests.statics");
        Configurator.registerObjectClasses("fr.kwizzy.configurator.tests.objects");
        Configurator.load(true);
    }
}
