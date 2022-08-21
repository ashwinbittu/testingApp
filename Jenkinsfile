def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
]
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
        /*
        stage('Sonar Code Analysis') {
            environment {
                scannerHome = tool 'sonarqscan'
            }
            steps {
               withSonarQubeEnv('sonar') {
                   sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=radammcorp \
                   -Dsonar.projectName=radammcorp \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=target/test-classes/com/radammcorpit/account/controllerTest/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
              }
            }
        }  

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        */  

        stage ('UploadArtifact Artifactory') {
                    steps {
                        rtUpload (
                            buildName: JOB_NAME,
                            buildNumber: BUILD_NUMBER,
                            serverId: 'jfrog-artifactory-saas', // Obtain an Artifactory server instance, defined in Jenkins --> Manage:
                            spec: '''{
                                    "files": [
                                        {
                                        "pattern": "target/vprofile-v2.war",
                                        "target": "default-libs-release-local/",
                                        "recursive": "false"
                                        } 
                                    ]
                                }'''    
                        )                        
                    }
            }    
        post {
            always {
                echo 'Slack Notifications.'
                slackSend channel: '#jenkinscicd',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
            }
        }                      

    }
}
