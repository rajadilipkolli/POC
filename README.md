[![Build Status](https://travis-ci.org/rajadilipkolli/POC.svg?branch=master)](https://travis-ci.org/rajadilipkolli/POC)
[![codecov](https://codecov.io/gh/rajadilipkolli/POC/branch/master/graph/badge.svg)](https://codecov.io/gh/rajadilipkolli/POC)
[![License](https://img.shields.io/:license-apache-blue.svg?style=flat-square) ](https://github.com/rajadilipkolli/POC/blob/master/LICENSE)

# My POC

## Introduction
This repo is for all POC which I does and learnings I learnt
Below are the projects that I have completed POC
- [mongodb-redis-integration](mongodb-redis-integration/README.md) -> adding fault tolerant caching layer to MongoDB.
- [spring-boot-rest-poc](spring-boot-rest/README.md) -> Enterprise application integrated with spring data, datasource-proxy, JPA, ActiveMQ(for demostration)

### Filing Issues

If you encounter any bug, please file an issue [here](https://github.com/rajadilipkolli/POC/issues/new).

To suggest a new feature or changes that could be made, file an issue the same way you would for a bug.

### Pull Requests

Pull requests are welcome. To open your own pull request, click [here](https://github.com/rajadilipkolli/POC/compare). When creating a pull request, make sure you are pointing to the fork and branch that your changes were made in.

### How to setup Project

This project is using lombok so you need to prepare your IDE as described [here](http://www.vogella.com/tutorials/Lombok/article.html).
After Setup import project as a maven project.

### Jacoco
**Code coverage** is a software metric used to measure how many lines of our code are executed during automated tests.
JaCoCo reports help you visually analyze code coverage by using diamonds with colors for branches and background colors for lines:

 - **Red diamond** means that no branches have been exercised during the test phase.
 - **Yellow diamond** shows that the code is partially covered – some branches have not been exercised.
 - **Green diamond** means that all branches have been exercised during the test.
The same color code applies to the background color, but for lines coverage.

JaCoCo mainly provides three important metrics:

 - **Lines coverage** reflects the amount of code that has been exercised based on the number of Java byte code instructions called by the tests.
 - **Branches coverage** shows the percent of exercised branches in the code – typically related to if/else and switch statements.
- **Cyclomatic complexity** reflects the complexity of code by giving the number of paths needed to cover all the possible paths in a code through linear combination.
To take a trivial example, if there is no if or switch statements in the code, the cyclomatic complexity will be 1, as we only need one execution path to cover the entire code.

Generally the cyclomatic complexity reflects the number of test cases we need to implement in order to cover the entire code.

### How to apply spring-formatter

To assign spring-formatter issue command `mvnw io.spring.javaformat:spring-javaformat-maven-plugin:apply` after adding plugin to pom.xml

### How to release a multi module project to git 

__Release using maven and git flow__
  - git checkout -b release master(create branch release from master)
  - mvn --batch-mode release:prepare release:perform -DscmCommentPrefix="bumping versions" -DreleaseVersion=0.0.1 -DdevelopmentVersion=0.0.2-SNAPSHOT(sets comments, release version and development version)
  - git checkout master(get lastest master in local)
  - git merge --no-ff -m "Release Merge release into master" release (merge master with release/0.0.1 branch) or use git merge branchname
  - git branch -D release(delete branch release)
  - git push --all && git push --tags(push master and tags to repository)
  - mvn release:clean(deleted backupfiles from local env)
  
  
### Commands to ensure that dependencies are upgraded

mvn versions:display-plugin-updates
mvn versions:display-property-updates 
