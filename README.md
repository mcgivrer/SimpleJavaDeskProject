# README

This is a Simple java Application for test purposes.

## Build

```bash
sdk env install
sdk env use
build.sh a
```

> [!NOTE]
> See [README build script](build.readme.md) for more details.

## Execute

Retrieve in the properties file the project name and project version to adapt the command line bellow:

```properties
project.name=sjdp
project.title=SJDP
project.version=0.0.1
...
```

Execute the simple command line:

```bash
java -jar target/[project.name]-[project.version].jar
```

Or if you are under a Linux machine of a git bash session on Windows:

```bash
. target/build/[project.name]-[project.version].run
```

You will get something like this window :

<figure>
  <img src="docs/images/capture-test-002-behavior-sensor.png" alt="Executing the App class"/>
  <figcaption>figure 1 - Executing the App class</figcaption>
</figure>



https://github.com/mcgivrer/SimpleJavaDeskProject/assets/216852/79eb81dc-9987-4146-a1b5-7ee54ccc17b9



## Contribute

Open this project

-

with [Visual Studio Code](https://code.visualstudio.com/download "Download the Visual Studio Code for your preferred OS platform")
as a java project

<figure>
  <img src="docs/images/capture-editing-with-vscode.png" alt="Editing your project with Visual Studio Code"/>
  <figcaption>figure 2 - Editing your project with Visual Studio Code</figcaption>
</figure>

- or with the
  JetBrain [IntelliJ IDEA](https://www.jetbrains.com/idea/download/ "Download IntelliJ for your own preferred OS Platform").

<figure>
  <img src="docs/images/capture-editing-with-intellij.png" alt="Editing your project with Jetbrains IntelliJ IDEA"/>
  <figcaption>figure 3 - Editing your project with Jetbrains IntelliJ IDEA</figcaption>
</figure>

McG.
