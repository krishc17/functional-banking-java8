node{
  stage ('Build') {
     git url: 'https://github.com/krishc17/functional-banking-java8.git'
     withMaven(
        maven: 'Maven1',
        mavenSettingsConfig: '2cad0513-a7a5-45c2-9988-d9c1ee3fb0e9') {
        sh "mvn clean package"
 
    } 
  }
  stage ("Deploy"){
sh "sudo docker run -d -p 81:8080 --name mytomcat tomcat"
sh "sudo docker cp /var/lib/jenkins/workspace/cicd_pipeline/multi-module/webapp/target/webapp.war mytomcat:/usr/local/tomcat/webapps/"
}
}
