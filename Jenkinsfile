pipeline {
    agent any

    stages{
        stage('fetch code') {
          steps{
              git branch: 'vp-rem', url: "https://github.com/ashwinbittu/testingApp.git"
          }  
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
            post {
                success {
                    echo "Now Archiving."
                    archiveArtifacts artifacts: '**/*.war'
                }
            }
        }
        stage('Test'){
            steps {
                sh 'mvn test'
            }

        }
         stage('CheckStyle Code Ananlysis'){
            steps {
                sh 'mvn checkstyle:checkstyle'
            }

        }

        stage('Sonar Code Analysis') {
            environment {
                scannerHome = tool 'sonarqscan'
            }
            steps {
               withSonarQubeEnv('sonar') {
                   sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=vprofile \
                   -Dsonar.projectName=vprofile \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=target/test-classes/com/visualpathit/account/controllerTest/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
              }
            }
        }  

        /*stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("UploadArtifact Nexus"){
                    steps{
                        nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: '172.31.23.157:8081',
                        groupId: 'QA',
                        version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                        repository: 'vprofile-repo',
                        credentialsId: 'nexuslogin',
                        artifacts: [
                            [artifactId: 'vproapp',
                            classifier: '',
                            file: 'target/vprofile-v2.war',
                            type: 'war']
            ]
        )
            }
        }*/  

        stage ('UploadArtifact Artifactory') {
                    steps {
                        rtServer (
                            id: "jfrog",
                            url: "https://ashwinbittu.jfrog.io/artifactory",
                            credentialsId: "jfrog-artifactory-saas"
                        )

                        rtMavenDeployer (
                            id: "MAVEN_DEPLOYER",
                            serverId: "jfrog",
                            releaseRepo: "default-libs-release-local",
                            snapshotRepo: "default-libs-snapshot-local"
                        )

                        rtMavenResolver (
                            id: "MAVEN_RESOLVER",
                            serverId: "jfrog",
                            releaseRepo: "default-libs-release",
                            snapshotRepo: "default-libs-snapshot"
                        )
                    }
            }              

    }
}
