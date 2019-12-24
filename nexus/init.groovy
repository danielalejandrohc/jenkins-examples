// You can check more about this class here: https://javadoc.jenkins-ci.org/jenkins/model/Jenkins.html
job = jenkins.model.Jenkins.instance.getItemByFullName("nexus-create-repo");
job.scheduleBuild2(5)