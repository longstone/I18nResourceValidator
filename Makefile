build:
	mvn clean install
  
release:
	gpg --import ./travis/deployment-key.gpg
	mvn versions:set -DnewVersion=${TRAVIS_TAG}
	mvn clean deploy -P release --settings ./travis/settings.xml
