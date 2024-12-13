SONAR_URL := http://localhost:9000
SONAR_PROJECT_KEY := gatling-yaml-wrapper
SONAR_PROJECT_NAME := gatling-yaml-wrapper

clean:
	mvn clean

deps-check:
	mvn -DnvdApiKey=$(NVD_API_KEY) dependency-check:check

pmd-check:
	mvn jxr:jxr pmd:check pmd:cpd-check

checkstyle:
	mvn checkstyle:checkstyle

spotbugs:
	mvn spotbugs:spotbugs

coverage:
	mvn test jacoco:report

all-compile:
	mvn compile test-compile

sca: deps-check pmd-check checkstyle spotbugs
	mvn verify

sonar: sca
	mvn sonar:sonar \
      -Dsonar.host.url=$(SONAR_URL) \
      -Dsonar.projectKey=$(SONAR_PROJECT_KEY) \
      -Dsonar.projectName=$(SONAR_PROJECT_NAME) \
      -Dsonar.token=$(SONAR_TOKEN) \
      -Dsonar.sources="src/main" \
      -Dsonar.tests="src/test" \
      -Dsonar.dependencyCheck.jsonReportPath=target/dependency-check-report.json \
      -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html \
      -Dsonar.java.pmd.reportPaths=target/pmd.xml \
      -Dsonar.cpd.java.minimumTokens=5 \
      -Dsonar.cpd.java.minimumLines=5 \
      -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml \
      -Dsonar.java.spotbugs.reportPaths=target/spotbugs.xml \
      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

simple: all-compile
	mvn gatling:test -Denv=dev -DlogLevel=DEBUG \
		-Dsimulation=simple/simulation.yml \
		-Dgatling.simulationClass=tool.gatling.wrapper.core.FrameworkSimulation

reqres: all-compile
	mvn gatling:test -Denv=dev -DlogLevel=DEBUG \
		-Dsimulation=reqres/simulation.yml \
		-Dgatling.simulationClass=tool.gatling.wrapper.core.FrameworkSimulation

