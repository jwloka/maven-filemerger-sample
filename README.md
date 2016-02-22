# Maven File Merger
A Maven plug-in project to demonstrate how a plug-in can collect data in a file and how the data can be merged across 
multiple sub-modules. 


## General Overview
The project consists of two Maven sub-projects demonstrating how to implement the Maven plug-in and how to use the 
plug-in in a multi-module Maven project. 

The Maven plug-in project ‘maven-filemerger’ implements two Mojos to collect and merge file data during the standard 
Maven build process. The **collect** Mojo gathers some data in a separate file for every project that is built. 
The **merge** Mojo defines a common location for a multi-module project to merge the data for each sub-project into 
a single file.  

The project ‘maven-filemerger-example’ illustrates how the Maven plug-in can be used. It represents a Maven project 
with three sub-modules. Every module collects data in a separate file and which is also merged into the parent 
project’s data file. The different configurations in each module show how a file location can be configured. 

## Use the Maven Plug-in
Before you can use the *File Merger Plug-in* you'll have to build and install it into your local Maven repository. Read 
the next section if you have any trouble installing it.

You can **collect data** in a Maven project by adding the File Merger plug-in to your <build> section, as follows:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.wloka.maven</groupId>
            <artifactId>maven-filemerge</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <executions>
                <execution>
                    <phase>process-sources</phase>
                    <goals>
                        <goal>collect</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <targetFile>${project.build.directory}/classes.log</targetFile>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Specify a phase in which the data should be gathered and use the goal ‘collect’. You also can specify where the data 
are stored on disk, or just go with the default value and use the project’s build directory.

You can merge the gathered data from transitively defined sub-modules by adding the File Merger plug-in to the 
<build> section of a parent project:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.wloka.maven</groupId>
            <artifactId>maven-filemerge</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <executions>
                <execution>
                    <phase>initialize</phase>
                    <goals>
                        <goal>merge</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <targetFile>${project.build.directory}/maven-filemerger/classes.log</targetFile>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Specify a phase in which the target location for the merged file data is provided and use the goal ‘merge’. All 
sub-modules with a ‘collect’ goal will add there data to the merge location. You may also specify the location on 
disk where the merged data are stored.

## Build the Project
As this plug-in is for demonstration only, you wont find it in any public Maven repository. You'll have to download 
it's source, build and install it into your local Maven Repository.
 
To install the File Merger Plug-in and run the example, you simply run the following command in the project's root 
folder:

```bash
$ mvn clean install
```

If you want to install the File Merger plug-in only, go to the directory 'maven-filemerger' and run the same command:

```bash
$ cd maven-filemerger
$ mvn clean install
```
