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

### How to apply spring-formatter

To assign spring-formatter issue command `mvnw io.spring.javaformat:spring-javaformat-maven-plugin:apply` after adding plugin to pom.xml

### How to release a multi module project to git 

```
git checkout -b release/v0.2.1

mvn --batch-mode release:prepare release:perform -DreleaseVersion=0.2.1 -DdevelopmentVersion=0.2.2-SNAPSHOT

git checkout development
git merge --no-ff -m "v0.2.1-SNAPSHOT" release/v0.2.1

git checkout master
git merge --no-ff -m "v0.2.1" release/v0.2.1~1

git branch -d release/v0.2.1

git push --all && git push --tags
```
