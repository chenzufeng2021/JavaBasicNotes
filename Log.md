# Java 日志

参考资料：

1. [Java 日志框架入门教程：从 JDKLog 到 SLF4J - 陈树义的博客 (shuyi.tech)](https://shuyi.tech/archives/javalog)

在项目开发过程中，我们可以通过 debug 查找问题。而在线上环境我们查找问题只能通过==打印日志==的方式查找问题。

在 Java 开发中，常用的日志记录框架有 **JDKLog、Log4J、LogBack、SLF4J**。这些日志记录框架各有各的特点，各有各的应用场景。

# JDKLog

JDKLog 是 JDK 官方（`java.util.logging.Logger`）提供的一个记录日志的方式，直接在 JDK 中就可以使用。

```java
import java.util.logging.Logger;

public class JDKLog {
    public static void main(String[] args) {
        /*
         * Find or create a logger for a named subsystem.  If a logger has
         * already been created with the given name it is returned.  Otherwise
         * a new logger is created.
         */
        Logger logger = Logger.getLogger("JDKLog");
        logger.info("Hello World !");
    }
}
```

输出：

```markdown
七月 30, 2021 9:39:18 上午 JDKLog main
信息: Hello World !
```

JDKLog 功能比较太过于简单，不支持占位符显示，拓展性比较差！

# Log4J

Log4J 是 Apache 的一个日志开源框架，有多个分级（`DEBUG、INFO、WARN、ERROR`）记录级别，可以很好地将不同日志级别的日志分开记录，极大地方便了日志的查看。

但 Log4J 本身也存在一些缺点，比如==不支持使用占位符==，不利于代码阅读等缺点。

## 创建 Maven 项目、添加依赖

D:\Learning\Log\LogDemo\Log4J

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.6.2</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.6.2</version>
    </dependency>
</dependencies>
```

## 添加配置文件

增加配置文件 `log4j2.xml` 放在 resource 目录下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

其中节点的 `level` 属性表示输出的最低级别。

## 测试

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JLog {
    public static void main(String[] args) {
        /*
         * Returns a Logger using the fully qualified name of the Class as the Logger name.
         * @param clazz The Class whose name should be used as the Logger name.
         * If null it will default to the calling class.
         */
        Logger logger = LogManager.getLogger(Log4JLog.class);
        logger.debug("Debug Level");
        logger.info("Info Level");
        logger.warn("Warn Level");
        logger.error("Error Level");
    }
}
```

输出：

```markdown
10:06:22.543 [main] INFO  Log4JLog - Info Level
10:06:22.558 [main] WARN  Log4JLog - Warn Level
10:06:22.560 [main] ERROR Log4JLog - Error Level
```



如果没有配置 log4j2.xml 配置文件，那么 LOG4J 将自动启用的配置文件中 level 为：`Root level="error"`

```markdown
ERROR StatusLogger No log4j2 configuration file found. Using default configuration: logging only errors to the console.
10:08:15.250 [main] ERROR Log4JLog - Error Level
```



# LogBack

LogBack 除了具备 Log4j 的所有优点之外，还解决了 Log4J 不能使用占位符的问题。LogBack 比 Log4J 有更快的运行速度，更好的内部实现。并且 LogBack 内部集成了 SLF4J 可以更原生地实现一些日志记录的实现。

## 创建 Maven 项目、添加依赖

D:\Learning\Log\LogDemo\LogBack

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.1.7</version>
        <!-- <scope>test</scope> -->
    </dependency>
</dependencies>
```



## 添加配置文件

增加配置文件 `logback.xml` 放在 resource 目录下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
            </Pattern>
        </layout>
    </appender>
    
    <logger name="chenzf" level="TRACE"/>
    
    <root level="debug">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

==LogBack 的日志级别区分可以细分到类或者包==，这样就可以使日志记录变得更加灵活。之后在类文件中引入Logger类，并进行日志记录。

## 测试

```java
package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogBack {
    /**
     * Return a logger named corresponding to the class passed as parameter,
     * using the statically bound instance.
     */
    static final Logger logger = LoggerFactory.getLogger(LogBack.class);

    public static void main(String[] args) {
        logger.trace("Trace Level");
        logger.debug("Debug Level");
        logger.info("Info Level");
        logger.warn("Warn Level");
        logger.error("Error Level");
    }
}
```

输出：

```markdown
// <logger name="chenzf" level="TRACE"/>
10:36:50,388 |-INFO in ch.qos.logback.classic.joran.action.LoggerAction - Setting level of logger [chenzf] to TRACE
10:36:50,388 |-INFO in ch.qos.logback.classic.joran.action.RootLoggerAction - Setting level of ROOT logger to DEBUG

// com.example.LogBack 由 LogBack 所在包决定
10:34:44.941 [main] DEBUG com.example.LogBack - Debug Level
10:34:44.941 [main] INFO  com.example.LogBack - Info Level
10:34:44.941 [main] WARN  com.example.LogBack - Warn Level
10:34:44.941 [main] ERROR com.example.LogBack - Error Level
```





# SLF4J：适配器

JDKLog、Log4J、LogBack 这几个常用的日志记录框架都有各自的优缺点，适合在不同的场景下使用。简单的项目直接用 JDKLog 就可以了，而复杂的项目需要用上 Log4J。

很多时候我们做项目都是从简单到复杂，也就是我们很可能一开始使用的是 JDKLog，之后业务复杂了需要使用 Log4J，这时候我们如何将原来写好的日志用新的日志框架输出呢？

因为在实际的项目应用中，有时候可能会==从一个日志框架切换到另外一个日志框架的需求==，这时候往往需要在代码上进行很大的改动。为了避免切换日志组件时要改动代码，这时候一个叫做 SLF4J（Simple Logging Facade for Java，即 ==Java 简单日志记录接口集==）的东西出现了。

SLF4J（Simple Logging Facade for Java，即 Java 简单日志记录接口集）是一个==日志的接口规范==，它==对用户提供了统一的日志接口，屏蔽了不同日志组件的差异==。这样我们==在编写代码的时候只需要看 SLF4J 这个接口文档即可，不需要去理会不同日之框架的区别==。而当我们需要更换日志组件的时候，我们==只需要更换一个具体的日志组件 Jar 包就可以了==。

## SLF4J+JDKLog

### 创建 Maven 项目、导入依赖

在 Maven 中导入以下依赖包：

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.21</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-jdk14 -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-jdk14</artifactId>
        <version>1.7.21</version>
        <!-- <scope>test</scope> -->
    </dependency>
</dependencies>
```

### 测试

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jJDKLog {
    static final Logger logger = LoggerFactory.getLogger(Slf4jJDKLog.class);

    public static void main(String[] args) {
        logger.trace("Trace Level.");
        logger.info("Info Level.");
        logger.warn("Warn Level.");
        logger.error("Error Level.");
    }
}
```

输出：

```markdown
七月 30, 2021 10:52:18 上午 Slf4jJDKLog main
信息: Info Level.
七月 30, 2021 10:52:18 上午 Slf4jJDKLog main
警告: Warn Level.
七月 30, 2021 10:52:18 上午 Slf4jJDKLog main
严重: Error Level.
```



## SLF4J+LOG4J

### 创建 Maven 项目、导入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.21</version>
    </dependency>

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.21</version>
    </dependency>

    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
</dependencies>
```

### 添加配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">

<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/' >

    <appender name="myConsole" class="org.apache.log4j.ConsoleAppender">
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern"
                   value="[%d{dd HH:mm:ss,SSS\} %-5p] [%t] %c{2\} - %m%n" />
        </layout>
        <!--过滤器设置输出的级别-->
        <filter class="org.apache.log4j.varia.LevelRangeFilter">
            <param name="levelMin" value="debug" />
            <param name="levelMax" value="error" />
            <param name="AcceptOnMatch" value="true" />
        </filter>
    </appender>

    <!-- 根logger的设置-->
    <root>
        <priority value ="debug"/>
        <appender-ref ref="myConsole"/>
    </root>
</log4j:configuration>
```

`URI is not registered (Settings | Languages & Frameworks | Schemas and DTDs | Ignored schemas and DTDs)`

![image-20210730111310256](Log笔记.assets/image-20210730111310256.png)

### 测试

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4J_LOG4J {
    static final Logger logger = LoggerFactory.getLogger(SLF4J_LOG4J.class);

    public static void main(String[] args) {
        logger.trace("Trace Level.");
        logger.info("Info Level.");
        logger.warn("Warn Level.");
        logger.error("Error Level.");
    }
}
```



输出：

```java
[30 11:16:10,100 INFO ] [main] SLF4J_LOG4J - Info Level.
[30 11:16:10,101 WARN ] [main] SLF4J_LOG4J - Warn Level.
[30 11:16:10,101 ERROR] [main] SLF4J_LOG4J - Error Level.
```



## SLF4J+LogBack

### 创建 Maven 项目、导入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.21</version>
    </dependency>

    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.1.7</version>
    </dependency>

    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>1.1.7</version>
    </dependency>
</dependencies>
```

### 添加配置文件

配置 logback.xml 文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Pattern>
        </layout>
    </appender>
    
    <logger name="com.example" level="TRACE"/>

    <root level="trace">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

### 测试

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4J_LogBack {
    static final Logger logger = LoggerFactory.getLogger(SLF4J_LogBack.class);

    public static void main(String[] args) {
        String name = "chenzf";
        String password = "123456";

        logger.trace("Trace Level");
        logger.debug("Debug Level");
        logger.info("Info Level");
        logger.warn("Warn Level");
        logger.error("Error Level");

        // 支持使用占位符
        logger.info("name: {}, password: {}", name, password);
    }
}
```



输出：

```java
14:19:00.746 [main] TRACE SLF4J_LogBack - Trace Level
14:19:00.748 [main] DEBUG SLF4J_LogBack - Debug Level
14:19:00.748 [main] INFO  SLF4J_LogBack - Info Level
14:19:00.748 [main] WARN  SLF4J_LogBack - Warn Level
14:19:00.748 [main] ERROR SLF4J_LogBack - Error Level
14:19:00.748 [main] INFO  SLF4J_LogBack - name: chenzf, password: 123456
```



# 日志框架解析



现在最流的日志框架解决方案莫过于`SLF4J + LogBack`。原因有下面几点：

- LogBack 自身实现了 SLF4J 的日志接口，不需要 SLF4J 去做进一步的适配。
- LogBack 自身是在 Log4J 的基础上优化而成的，其运行速度和效率都比 LOG4J 高。
- SLF4J + LogBack 支持占位符，方便日志代码的阅读，而 LOG4J 则不支持。

LogBack 被分为 3 个组件：*logback-core、logback-classic 和 logback-access。*

- **logback-core** ==提供了 LogBack 的核心功能==，是另外两个组件的基础。
- **logback-classic** 则==实现了 SLF4J 的API==，所以当想配合 SLF4J 使用时，需要将 logback-classic 引入依赖中。
- **logback-access** 是为了集成 Servlet 环境而准备的，可提供 HTTP-access 的日志接口。

LogBack 的日志记录数据流是从 Class（Package）到 Logger，再从 Logger 到 Appender，最后从 Appender 到具体的输出终端。

![img](Log笔记.assets/595137-20171128125542112-629032237.png)

LogBack 配置文件可以分为几个节点，其中 Configuration 是根节点，Appender、Logger、Root是Configuration的子节点。

## appender 节点

appender 子节点是负责写日志的组件。appender有两个必要属性name、class 。name指定appender的名称，class指定appender的全限定名。

class，主要包括：

- ch.qos.logback.core.ConsoleAppender 控制台输出
- ch.qos.logback.core.FileAppender 文件输出
- ch.qos.logback.core.RollingFileAppender 文件滚动输出

