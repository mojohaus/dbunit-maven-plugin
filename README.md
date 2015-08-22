# dbunit-maven-plugin

Use this plugin to invoke DbUnit utilities
 
[![Build Status](https://travis-ci.org/mojohaus/dbunit-maven-plugin.svg?branch=master)](https://travis-ci.org/mojohaus/dbunit-maven-plugin)

## Releasing

* Make sure `gpg-agent` is running.
* Execute `mvn -B release:prepare release:perform`

For publishing the site do the following:

```
cd target/checkout
mvn verify site site:stage scm-publish:publish-scm
```
