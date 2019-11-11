build:
	mvn clean install
  
release:
	mvn versions:set -DnewVersion=${TRAVIS_TAG}
	mvn clean deploy -P release --settings ./travis/settings.xml
