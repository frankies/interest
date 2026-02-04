# Simple Maven Project

A Maven-based Java Web project.

## Project Information

- **JDK Version**: 1.8
- **Maven Version**: 3.3.9 (using Maven Wrapper)
- **Project Type**: Web Application (WAR)
- **Main Dependencies**: Apache Oltu OAuth2 Client

## Project Structure

```
simple-maven-pj/
├── .mvn/                   # Maven Wrapper configuration
│   ├── .m2/                # Maven local repository directory (specified by .mvn/settings.xml)
│   ├── wrapper/
│   │   └── maven-wrapper.properties
│   └── settings.xml        # Maven settings (local repository configuration)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── HelloServlet.java
│   │   │       └── JndiCheckServlet.java
│   │   ├── resources/          # Resource files directory (optional configuration files, etc.)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       └── index.html
│   └── test/
│       └── java/
│           └── com/example/
│               ├── HelloServletTest.java
│               ├── JndiResourcesTest.java
│               └── TomcatTestSupport.java
├── .vscode/                 # VS Code configuration (tasks/launch/settings, etc.)
│   └── tomcat/              # Local Tomcat/JNDI related scripts and Context XML
├── mvnw                    # Maven Wrapper script (Unix/Linux/Mac)
├── mvnw.cmd                # Maven Wrapper script (Windows)
├── pom.xml                 # Maven project configuration
└── README.md               # Project documentation
```

## Dependencies

- **javax.servlet:servlet-api** (2.4, provided): Servlet API for web development (compatible with legacy containers/projects)
- **org.apache.oltu.oauth2.client** (1.0.1): OAuth2 client library
- **junit** (4.12): Unit testing framework

## Usage Instructions

### Build Project

On Windows:

```bash
mvnw.cmd clean install
```

On Unix/Linux/Mac:

```bash
./mvnw clean install
```

### Compile Project

```bash
mvnw.cmd compile
```

### Run Tests

```bash
mvnw.cmd test
```

### Package Project

```bash
mvnw.cmd package
```

The packaged WAR file will be located at `target/simple-maven-pj.war`

## Maven Configuration

The project is configured with a custom Maven local repository path: `.mvn/.m2/repository`

This means all downloaded dependencies will be stored in the `.mvn/.m2` folder within the project directory, rather than the system default `~/.m2` directory.

## Deployment Instructions

The generated WAR file can be deployed to any web container that supports Servlet 2.4+, such as:

- Apache Tomcat 8.x or higher
- Jetty 9.x or higher
- WildFly/JBoss

## Development Environment Requirements

- JDK 1.8 or higher
- Maven 3.3.9 (automatically downloaded via Maven Wrapper)

## VS Code Development Environment

The project has complete VS Code development environment support configured:

### Recommended Extensions

Install the following extensions for the best development experience:

- **Java Extension Pack** - Core Java development tools
- **Maven for Java** - Maven project management
- **Tomcat for Java** - Local Tomcat server support
- **XML** - XML file support
- **GitLens** - Git enhancement tools
- **SonarLint** - Code quality checking

After opening the project, VS Code will prompt you to install the recommended extensions.

### VS Code Tasks

The project has the following Maven tasks configured (press `Ctrl+Shift+P`, type "Run Task"):

- `maven: compile` - Compile project
- `maven: test` - Run tests
- `maven: package` - Package WAR file
- `maven: install` - Clean and install (default build task, shortcut `Ctrl+Shift+B`)
- `maven: clean install (skip tests)` - Fast build skipping tests

Additionally includes Tomcat related tasks:

- `tomcat: run` / `tomcat: debug` - Start using `tomcat7-maven-plugin` (debug port 8000), `Ctrl+C` to stop directly
- `tomcat: shutdown (tomcat7:shutdown)` - Stop Tomcat
- `tomcat: stop` - Force stop (kill process listening on port 8080, fallback option)

#### Setting contextPath

- Maven property: `app.contextPath` (default `/`, see `pom.xml`)
- VS Code: workspace setting `applicationContextPath` (default `/a1stream`, see `.vscode/settings.json`)

You can override contextPath using either of these methods:

1. Override directly with Maven command:

```bash
mvnw.cmd "-Dapp.contextPath=/demo" tomcat7:run
```

2. Override with VS Code task:

- Open `.vscode/settings.json`, modify `applicationContextPath` (e.g., `/demo`)

#### Context XML Naming Rules (JNDI DataSource)

When using `tomcat: run (JNDI Context XML)` / `tomcat: debug (JNDI Context XML)`, Context XML is automatically selected based on contextPath:

- `applicationContextPath=/demo` → uses `.vscode/tomcat/Catalina/localhost/demo.xml`
- `applicationContextPath=/` or empty → uses `.vscode/tomcat/Catalina/localhost/ROOT.xml`

This means when migrating to other projects, you typically only need to:

- Modify `applicationContextPath`
- Copy `.vscode/tomcat/Catalina/localhost/demo.xml` to corresponding xml filename (and modify JDBC URL/credentials per project)

#### How to Add New `.vscode\\tomcat\\Catalina\\localhost\\*.xml`

1. First decide your contextPath (e.g., `/demo`, `/`):

- Recommend modifying `applicationContextPath` in `.vscode/settings.json`

2. Calculate the corresponding context filename:

- Rule: Remove leading `/`, the remainder is the filename
- Special case: `/` (or empty) uses `ROOT.xml`

Examples:

- `/demo` → `demo.xml`
- `/` → `ROOT.xml`

3. Create corresponding file in `.vscode/tomcat/Catalina/localhost/` (e.g., `ROOT.xml`), example content:

```xml
<Context reloadable="false">
	<Resource
			auth="Container"
			name="jdbc/a1_dms"
			type="javax.sql.DataSource"
			driverClassName="org.postgresql.Driver"
			url="jdbc:postgresql://127.0.0.1:5432/yourdb"
			username="your_user"
			password="your_pass"
			maxTotal="50"
			maxIdle="10"
			maxWaitMillis="10000"/>
</Context>
```

Notes:

- Only need `<Context> + <Resource>`, don't include `docBase` / `path` (avoid startup failures due to path inconsistencies in embedded/plugin mode)
- `<Resource name="jdbc/...">` corresponds to `<resource-ref>` in web.xml and runtime lookup of `java:comp/env/jdbc/...`

4. Start:

- With VS Code: In `TERMINAL` view, `Run Task...` and select `tomcat: run` (or debug version)

### Debug Configuration

- **Debug (Attach) - Tomcat** - Attach to running Tomcat (port 8000)
- **Run All Tests** - Run all tests

### Code Snippets

Project includes the following code snippets:

- `servlet` - Quickly create Servlet class
- `test` - Quickly create JUnit test class
- `oauth2` - Quickly create OAuth2 request

## Important Notes

1. When running Maven Wrapper for the first time, it will automatically download Maven 3.3.9
2. Local repository configuration is in `.mvn/settings.xml`
3. Project uses UTF-8 encoding
4. Ensure JDK 1.8 is installed and JAVA_HOME environment variable is properly configured

## Local JNDI Availability Verification (Optional)

The project provides a diagnostic endpoint and corresponding integration test to verify if `java:comp/env/jdbc/*` is bound:

- Runtime Servlet: `/__jndi` (see `WEB-INF/web.xml` mapping)
- Integration Test: `JndiResourcesTest`

If you change contextPath (e.g., to `/demo`) and prepare the corresponding Context XML (e.g., `demo.xml`), you can run the integration test like this:

```bash
mvnw.cmd "-Dapp.contextPath=/demo" "-Dtest=JndiResourcesTest" test
```
