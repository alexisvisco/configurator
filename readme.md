## Configurator

Create config that save and load data is a pain.
This lib do this pain for you.

### Example

Object configuration:
```java
public class TestObject implements Config {
    
    public static TestObject instance = new TestObject();

    public String name = "yolo";
    public int port = 9374;
    public String base = "ok";
    public boolean ok = true;
    public String[] values = {"hello", "world", "im", "cool"};
    
}
```

Static configuration:
```java
public class TestStatic implements Config {

    public static String name = "yolo";
    public static int port = 9374;
    public static String base = "ok";
    public static boolean ok = true;
    
}
```
(caution static configuration only work with native type (boolean, int, string, char, byte...), does not work with arrays or custom object)

Main:
```java
public static void main(String[] args) {
        Configurator.registerStaticClasses("fr.kwizzy.configurator.tests.statics");
        Configurator.registerObjectClasses("fr.kwizzy.configurator.tests.objects");
        Configurator.load(true);
}
```

Will generate two .json.

./config/TestObject.json:
```json
{
  "name": "yolo",
  "port": 9374,
  "base": "ok",
  "ok": true,
  "values": [
    "hello123",
    "world",
    "im",
    "cool"
  ]
}
```

./config/TestStatic.json:
```json
{
  "port": 93,
  "name": "yolo",
  "ok": true,
  "base": "ok"
}
```

Obviously if I modify the config and I start the objects / classes will get the elements that I would have modified.

### Add to your project

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependency>
	    <groupId>com.github.AlexisVisco</groupId>
	    <artifactId>Configurator</artifactId>
	    <version>10 chars of the latest commit</version>
	</dependency>
```