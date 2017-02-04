# Simple swarm project

## Setting up arquillian in IntelliJ
To run the tests: 
Simply add <i>Manual container configuration</i>, without any settings

To debug, in the <i>debug</i> tab of the config, set the port to 5012 and the container qualifier to 'debug-test'.


# ISSUES (retry with newer version of swarm):
 - `swaggerArchive.setContextRoot` doesn't work, and `@SwaggerDefinition(basePath)` too
 
## Questions
How can I replace a dependency version? (e.g. use newer swagger)



## TODO:
- websockets
- security
- Using project-stages.yml
- fix swagger base path