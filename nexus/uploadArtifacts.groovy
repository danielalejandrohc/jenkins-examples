pipeline {
    agent {
        label "master"
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "https"
        NEXUS_URL = "localhost:3333/nexus"
        NEXUS_REPOSITORY = "repository-example"
        NEXUS_CREDENTIAL_ID = "jenkins"
    }

    stage("clone java code") {
        steps {
            script {

            }
        }
    }

    stage("mvn build") {
        steps {
            script {
                sh "mvn package"
            }
        }
    }

    stage("publish to nexus") {
        steps {
            script {
                // Read POM xml file using 'readMavenPom' step 
                pom = readMavenPom file: POM_LOCATION_PARAM;
                // Build the .jar file name, usually this is compose by: artifactId-version.packaging from the pom.xml file
                jarFileName = "${pom.artifactId}-${pom.version}.${pom.packaging}";
                // By default java maven artifacts will be located at: target/*.jar folder, (or .war, .ear in that's the case)
                jarFilePath = "target/${jarFileName}";
                // Assign to a boolean response verifying If the artifact name exists
                jarFilePathExists = fileExists jarFilePath;
                
                if(jarFilePathExists) {
                    echo "*** File: ${jarFilePath}, group ${pom.groupId} version type ${pom.packaging}";
                    
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: pom.groupId,
                        version: pom.version,
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            // Artifact generated such as .jar, .ear and .war files.
                            [artifactId: pom.artifactId,
                            classifier: '',
                            file: jarFilePath,
                            type: pom.packaging]

                            // Lets upload the pom.xml file for additional information for Transitive dependencies
                            [artifactId: pom.artifactId,
                            classifier: '',
                            file: "pom.xml",
                            type: "pom"]
                        ]
                    );
                } else {
                    error "*** File: ${jarFilePath}, could not be found";
                }
            }
        }
    }
}