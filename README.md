anethum
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.anethum/com.io7m.anethum.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.anethum%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/com.io7m.anethum/com.io7m.anethum?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/anethum/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m/anethum.svg?style=flat-square)](https://codecov.io/gh/io7m/anethum)

![com.io7m.anethum](./src/site/resources/anethum.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/anethum/main.linux.temurin.current.yml)](https://github.com/io7m/anethum/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/anethum/main.linux.temurin.lts.yml)](https://github.com/io7m/anethum/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/anethum/main.windows.temurin.current.yml)](https://github.com/io7m/anethum/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/anethum/main.windows.temurin.lts.yml)](https://github.com/io7m/anethum/actions?query=workflow%3Amain.windows.temurin.lts)|

## anethum

A common API for parsers and serializers in [io7m](https://www.io7m.com)
packages.

### Features

  * Standardized interface for parsers.
  * Standardized interface for serializers.
  * Parse errors are [structured errors](https://www.io7m.com/software/seltzer).
  * Written in pure Java 17.
  * [OSGi](https://www.osgi.org/) ready.
  * [JPMS](https://en.wikipedia.org/wiki/Java_Platform_Module_System) ready.
  * ISC license.
  * High-coverage automated test suite.

### Motivation

[io7m](https://www.io7m.com) packages expose many different parsers and
serializers. The `anethum` package attempts to define a consistent and
strongly-typed API specification that these parsers and serializers can
implement in order to provide a uniform user experience between all of the
different packages.

### Building

```
$ mvn clean verify
```

### Usage

Parsers should implement the `ParserFactoryType` and `ParserType`
interfaces.

Serializers should implement the `SerializerFactoryType` and `SerializerType`
interfaces.

