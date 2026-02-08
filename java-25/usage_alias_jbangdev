# JBang Catalog



### How to run

This is a catalog to use with [jbang](https://jbang.dev).

Each alias below shows how to run it with JBang.
For this you need to have `jbang` installed and available in your PATH.
See the [JBang website](https://jbang.dev/download) for options on how to install JBang.

## Aliases


### hello

Script that says hello back for each argument

 ```
 jbang hello
 ```

### properties

Dump table of System properties

 ```
 jbang properties
 ```

### deps

Analyze JBang script dependencies

 ```
 jbang deps
 ```

### env

Dump table of Environment Variables

 ```
 jbang env
 ```

### gavsearch

`gavsearch` lets you use search.maven.org from command line.
Example: `gavsearch hibernate` will search for artifacts with hibernate in its name.
You can use any of the search modifiers search.maven.org supports, i.e.:
`gavsearch c:QuarkusTest` will search for artifacts with class `QuarkusTest`

 ```
 jbang gavsearch
 ```

### git

Git command line tool implemented with jgit. Lets you do basic git features without installing git!

 ```
 jbang git
 ```

### bouncinglogo



 ```
 jbang bouncinglogo
 ```

### h2



 ```
 jbang h2
 ```

### catalog2readme



 ```
 jbang catalog2readme
 ```

### httpd

`httpd` runs a webserver serving out the content of a directory.
Example: `jbang httpd@jbangdev -d _site` will serve out the `_site` folder on localhost:8000.

 ```
 jbang httpd
 ```

### getjava

Experimental utility to download Java distributions using api.foojay.io.

 ```
 jbang getjava
 ```

### ec



 ```
 jbang ec
 ```

### faker



 ```
 jbang faker
 ```

### dalle



 ```
 jbang dalle
 ```

### bootstrap

Bootstrap a jbang script to make it self-contained.

 ```
 jbang bootstrap
 ```

### jmc



 ```
 jbang jmc
 ```

### mf

# mf.java - JAR Manifest Reader CLI

`mf` is a command-line tool for extracting and displaying JAR manifest information.

## Usage

```sh
mf <jar-file> [--json] [--yaml] [--structured|-s]
```

- **`<jar-file>`**: Path to the JAR file to analyze (required). Use '-' for stdin.
- **`--json`**: Output manifest attributes as JSON.
- **`--yaml`**: Output manifest attributes as YAML.
- **`--structured`, `-s`**: Parse known attributes (e.g., Import-Package, Export-Package) into structured data (lists/maps).

## Features

- Reads the `META-INF/MANIFEST.MF` from the specified JAR file.
- By default, prints manifest attributes as manifest.mf.
- Supports machine-readable output with `--json` or `--yaml`.
- Structured parsing of known attributes with `--structured`.
- Returns a nonzero exit code if the manifest is missing or the JAR cannot be read.

## Examples

```sh
mf mylib.jar
mf mylib.jar --json
mf `jbang info jar org.junit.jupiter:junit-jupiter:5.10.0` --yaml--structured
```

 ```
 jbang mf
 ```

### jbang-fmt

Format Java code (without messing up JBang directives).

 ```
 jbang jbang-fmt
 ```

### jbang-jupyter



 ```
 jbang jbang-jupyter
 ```

### trylink



 ```
 jbang trylink
 ```

## Templates


### github

Simple cli to querying github

 ```
 jbang init -t github
 ```

### qmcp

Simple cli to querying github

 ```
 jbang init -t qmcp
 ```

### jitpack

Initializes a bare-bone jitpack.yml to enable publishing a jbang script as a maven artifact via jitpack.

Example: `jbang init -t jitpack@jbangdev myapp.java` and then commit this to github and visit jitpack.io to trigger its build.

 ```
 jbang init -t jitpack
 ```

### renovate

Initializes a renovate.json to enable automatic management of any .java file //DEPS section.

Example: `jbang init -t renovate@jbangdev .github/renovate.json` and then commit this to github and if you installed https://github.com/apps/renovate renovate will make issues and PR's for dependency updates.

 ```
 jbang init -t renovate
 ```

### junit

Basic template for JUnit tests

 ```
 jbang init -t junit
 ```

