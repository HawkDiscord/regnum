# Regnum plugin
Regnum plugin is the plugin interface used for the Regnum server
## Index
 - [Download](#download)
 - [Usage](#usage)
 - [Documentation](#documentation)
 - [Examples](#examples)
### Download
You can download Regnum artifacts from the [GitHub releases](https://github.com/HawkDiscord/regnum/releases), [the CI](https://ci.schlaubi.me/viewLog.html?buildId=lastSuccessful&buildTypeId=Regnum_Build_2&tab=artifacts&branch_Regnum=feature%2Fpermissions) or from [JCenter](https://bintray.com/beta/#/hawk/maven/regnum-plugin?tab=overview)
Maven:
```xml
<project>
    <repositories>
        <repository>
            <id>jcenter</id>
            <url>https://jcenter.bintray.com</url>
        </repository>
    </repositories>
    
    <dependencies>
        <dependency>
            <groupId>cc.hawkbot.regnum.server</groupId>
            <artifactId>plugin</artifactId>
            <version>VERSION FROM BADGE AT THE TOP OF THE DOCUMENT</version>
</dependency>
    </dependencies>
</project>
```
Gradle:
```kotlin
repositories {
    jcenter()
}
dependencies {
    compile("cc.hawkbot.regnum.server:plugin:VERSION FROM BADGE AT THE TOP OF THE DOCUMENT")
}

```
### Usage
Every plugin has to contain exactly one class extending [`RegnumPlugin`](https://github.com/HawkDiscord/regnum/blob/master/plugin/src/main/java/cc/hawkbot/regnum/server/plugin/RegnumPlugin.java). Every plugin has it's own configuration (`RegnumPlugin#getConfig()`) and the global config (`RegnumPlugin#gerGlobalCOnfig()`)
### Documentation
You can find the documentation on [GitHub Page](https://pages.hawkbot.io/plugin) or on the [gh-pages branch](https://github.com/HawkDiscord/regnum/tree/gh-pages/plugin)
### Examples
You can find an small example [here](https://github.com/HawkDiscord/regnum/tree/master/example-plugina) or can take a look at [Hawk](https://github.com/Hawknum)