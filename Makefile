build:
	mvn clean install
  
master:
 mvn clean deploy -P release --settings ./travis/settings.xml

deploy:
	mvn versions:set -DnewVersion=${TRAVIS_TAG}
	mvn clean deploy -P release --settings ./travis/settings.xml
