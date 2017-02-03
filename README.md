# POC
	This repository contains all my POC which I have used to learn

# How to release a project
**Release using maven and git flow**
  - git checkout -b release/0.0.1 master(create branch release/0.0.1 from master)
  - mvn --batch-mode release:prepare release:perform -DscmCommentPrefix="first release" -DreleaseVersion=0.0.1 -DdevelopmentVersion=0.0.2-SNAPSHOT(sets comments, release version and development version)
  - git checkout master(get lastest master in local)
  - git merge --no-ff -m "first release Merge release/0.0.1 into master" release/0.0.1(merge master with release/0.0.1 branch)
  - git branch -D release/0.0.1(delete branch release/0.0.1)
  - git push --all && git push --tags(push master and tags to repository)
  - mvn release:clean(deleted backupfiles from local env)

# To run sonar
mvnw clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar