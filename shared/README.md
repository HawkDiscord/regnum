# Regnum shared
[![Download](https://api.bintray.com/packages/hawk/maven/regnum-shared/images/download.svg)](https://bintray.com/beta/#/hawk/maven/regnum-shared?tab=overview)

Regnum shared is a library containing shared classes between Regnum server and Regnum client. Event if you can download the library we don't recommend you to do so because it's added as a dependency when its needed anyway.
## Index
 - [Download](#download)
 - [Documentation](#documentation)

### Download
You can download Regnum artifacts from the [GitHub releases](https://github.com/HawkDiscord/regnum/releases), [the CI](https://ci.schlaubi.me/viewLog.html?buildId=lastSuccessful&buildTypeId=Regnum_Build_2&tab=artifacts&branch_Regnum=feature%2Fpermissions) or from [JCenter](https://bintray.com/beta/#/hawk/maven/regnum-shared?tab=overview)
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
            <groupId>cc.hawkbot.regnum</groupId>
            <artifactId>shared</artifactId>
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
    compile("cc.hawkbot.regnum:shared:VERSION FROM BADGE AT THE TOP OF THE DOCUMENT")
}

```
### Documentation
You can find the documentation on [GitHub Page](https://pages.hawkbot.io/shared) or on the [gh-pages branch](https://github.com/HawkDiscord/regnum/tree/gh-pages/shared)