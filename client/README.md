# Regnum client
[![Download](https://api.bintray.com/packages/hawk/maven/regnum-client/images/download.svg)](https://bintray.com/beta/#/hawk/maven/regnum-client?tab=overview)

Regnum client is a standard client for Regnum implemented in Kotlin and Java also containing some helpful tools
## Index
 - [Download](#download)
 - [Usage](#usage)
 - [Documentation](#documentation)
 - [Features](#features)
 - [Examples](#examples)

### Download
You can download Regnum artifacts from the [GitHub releases](https://github.com/HawkDiscord/regnum/releases), [the CI](https://ci.schlaubi.me/viewLog.html?buildId=lastSuccessful&buildTypeId=Regnum_Build_2&tab=artifacts&branch_Regnum=feature%2Fpermissions) or from [JCenter](https://bintray.com/beta/#/hawk/maven/regnum-client?tab=overview)
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
            <artifactId>client</artifactId>
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
    compile("cc.hawkbot.regnum:client:VERSION FROM BADGE AT THE TOP OF THE DOCUMENT")
}

```
### Usage
A detailed guide about how to use the client is coming soon
### Documentation
You can find the documentation on [GitHub Page](https://pages.hawkbot.io/client) or on the [gh-pages branch](https://github.com/HawkDiscord/regnum/tree/gh-pages/client)
### Features
A detailed list of features is coming soon
### Examples
You can find an small example [here](https://github.com/HawkDiscord/regnum/tree/master/client/src/main/java/cc/hawkbot/regnum/util/ClientLauncher.java) or can take a look at [Hawk](https://github.com/HawkDiscord/bot)