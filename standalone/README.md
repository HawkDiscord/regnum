# Regnum standalone
[![Download](https://api.bintray.com/packages/hawk/maven/regnum-standalone/images/download.svg)](https://bintray.com/beta/#/hawk/maven/regnum-standalone?tab=overview)

Regnum standalone is an extension for the Regnum client library which allows you to start a Regnum client without connecting to a Regnum server 
## Index
 - [Download](#download)
 - [Documentation](#documentation)
 - [Examples](#examples)

### Download
You can download Regnum artifacts from the [GitHub releases](https://github.com/HawkDiscord/regnum/releases), [the CI](https://ci.schlaubi.me/viewLog.html?buildId=lastSuccessful&buildTypeId=Regnum_Build_2&tab=artifacts&branch_Regnum=feature%2Fpermissions) or from [JCenter](https://bintray.com/beta/#/hawk/maven/regnum-standalone?tab=overview)
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
            <groupId>cc.hawkbot.regnum.client</groupId>
            <artifactId>standalone</artifactId>
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
    compile("cc.hawkbot.regnum.client:standalone:VERSION FROM BADGE AT THE TOP OF THE DOCUMENT")
}

```
### Documentation
You can find the documentation on [GitHub Page](https://pages.hawkbot.io/standalone) or on the [gh-pages branch](https://github.com/HawkDiscord/regnum/tree/master/standalone/src/test/java/StandaloneLauncher.java)
### Examples
You can find an example of the Regnum standalone client [here]()