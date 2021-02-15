# Import This

Tired of Googling for that annoying SBT import expression to pull in your favorite library? Here's a tiny CLI tool
that pulls the SBT import expression for your favorite libraries off Scaladex right into your terminal, ready to be copy pasted
into build.sbt.

### Usage
```
import-this 1.0
Usage: import-this query

query  the package to search for
```

### Example
```
> ./import-this sttp
found 20 options:
> softwaremill/sttp
  softwaremill/tapir
  ocadotechnology/sttp-oauth2
  softwaremill/sttp-model
  fullfacing/keycloak4s
  ragb/sttp-play-ws
  circe/circe
  stringbean/sttp-scribe
  typelevel/cats-effect
  allantl/jira4s
  lihaoyi/requests-scala
  endpoints4s/endpoints4s
  bartholomews/fsclient
  bot4s/telegram
  janstenpickle/trace4cats
  nryanov/consul4s
  softwaremill/sttp-shared
  softwaremill/sttp-client
  fullfacing/sttp-akka-monix
  softwaremill/tapir-sttp-client
fetching your import statement...
Link: http://index.scala-lang.org/softwaremill/sttp/core/3.1.1?target=_3.0.0-M3
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.1.1"
```

### Building
(TODO: Publish some binaries)

1. `sbt nativeImageRunAgent foo`
    - A transitive dependency, jline, loads resources from the classpath at runtime to configure terminal output. This additional step 
     allows the [GraalVM native image agent](https://www.graalvm.org/reference-manual/native-image/BuildConfiguration/#assisted-configuration-of-native-image-builds)
     to pick up the resource file paths (amongst other things) and create the configuration objects to be passed to the native image tool.

2. `sbt nativeImage`
    - This actually builds the native image. `sbt-native-image` automatically pulls in GraalVM and installs the native image extension.

### Under The Hood
- https://github.com/jhy/jsoup
- https://github.com/scalameta/sbt-native-image
- https://github.com/kovszilard/smenu

### FAQ
- Shouldn't this be better off as an IDE extension/SBT Plugin?
    - Yes.
- ...and?
    - This is really a toy project for me to 1) learn and use Cats (more specifically Cats-Effects), and 2) try out GraalVM's native image
    tool.
- ...are you scraping the Scaladex HTML output?
    - oops. (and so yes, this is likely very fragile. Would be really nice if there's an open API!)
- How about also searching Maven central/Bintray/Sonatype for Java libraries?
    - Good idea!
