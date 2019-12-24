pipeline {

    agent {
        label "master"
    }

    environment {
        // This can be nexus3 or nexus2
        NEXUS_VERSION = "nexus3"
        // This can be http or https
        NEXUS_PROTOCOL = "http"
        // Where your Nexus is running. 'nexus-3' is defined in the docker-compose file
        NEXUS_URL = "nexus-3:8081"
        // Repository where we will upload the artifact
        NEXUS_REPOSITORY = "repository-example"
        // Jenkins credential id to authenticate to Nexus OSS
        NEXUS_CREDENTIAL_ID = "nexus-credentials"
        NEXUS_SCRIPT = "maven-create-hosted"
    }

    stages {
        // You might get more details in these links:
        // https://github.com/sonatype/nexus-public/blob/master/plugins/nexus-script-plugin/src/main/java/org/sonatype/nexus/script/plugin/RepositoryApi.java
        
        stage("clone code") {
            steps {
                script {
                    // Get the script and check the want we want to create does not exists
                    response = httpRequest authentication: NEXUS_CREDENTIAL_ID, url: "${NEXUS_PROTOCOL}://${NEXUS_URL}/service/rest/v1/script";
                    echo "Response: ${response.content}"
                    jsonGetResponse = readJSON text: response.content;
                    
                    findResult = jsonGetResponse.find{element -> element.name.trim().equals(NEXUS_SCRIPT)};
                    echo "Result of finding: ${findResult}"
                    if(findResult == null) {
                        echo "Creating script"
                        // Create it!
                        jsonPayload =   "{ " +
                                        "    \"name\": \"${NEXUS_SCRIPT}\", " +
                                        "    \"type\": \"groovy\", " +
                                        "    \"content\":\"repository.createMavenHosted('${NEXUS_REPOSITORY}', 'default', true, org.sonatype.nexus.repository.maven.VersionPolicy.MIXED, org.sonatype.nexus.repository.storage.WritePolicy.ALLOW, org.sonatype.nexus.repository.maven.LayoutPolicy.PERMISSIVE)\" " +
                                        "}";
                        httpRequest authentication: NEXUS_CREDENTIAL_ID, 
                                    url: "${NEXUS_PROTOCOL}://${NEXUS_URL}/service/rest/v1/script",
                                    contentType: 'APPLICATION_JSON',
                                    httpMode: 'POST',
                                    requestBody: jsonPayload;
                        echo "Using payload ${jsonPayload}"
                        
                        // Invoke it!
                        httpRequest authentication: NEXUS_CREDENTIAL_ID, 
                                    contentType: 'TEXT_PLAIN',
                                    url: "${NEXUS_PROTOCOL}://${NEXUS_URL}/service/rest/v1/script/${NEXUS_SCRIPT}/run",
                                    httpMode: 'POST';
                    }
                }
            }
        }
        
    }
}
