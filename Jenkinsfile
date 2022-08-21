def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
]
pipeline {
    agent any

    environment {
        registryCredential = 'ecr:ap-southeast-2:awscreds'
        appRegistry = "043042377913.dkr.ecr.ap-southeast-2.amazonaws.com/testappimg"
        profileRegistry = "https://043042377913.dkr.ecr.ap-southeast-2.amazonaws.com"


        appname = "testingapp"
        artficatreporeg = "https://ashwinbittu.jfrog.io"
        artifactrepo = "ashwinbittu.jfrog.io/docker-local/$appname"
        artifactrepocreds = 'jfrog-artifact-saas'

        cluster = "testcluster"
        service = "testingappsvc"        
    }

    stages{
        stage('fetch code') {
          steps{
              git branch: 'docker', url: "https://github.com/ashwinbittu/testingApp.git"
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

        stage('Build App Image') {
            steps {
            
                script {
                        dockerImage = docker.build( appRegistry + ":$BUILD_NUMBER", "./Docker-files/app/multistage/")
                    }

            }
        
        }

        stage('Upload App Image to ECR') {
            steps{
                script {
                    docker.withRegistry( profileRegistry, registryCredential ) {
                    dockerImage.push("$BUILD_NUMBER")
                    dockerImage.push('latest')
                }
                }
            }
        }

        stage ('Upload App Image to Artifactory') {
                    steps {
                        script {
                            docker.withRegistry( artficatreporeg, artifactrepocreds ) {
                                sh 'docker tag ${appRegistry}:${BUILD_NUMBER} ${artifactrepo}:${BUILD_NUMBER}'
                                sh 'docker push ${artifactrepo}:${BUILD_NUMBER}'
                            }
                        }
                        
                        /*rtUpload (
                            buildName: JOB_NAME,
                            buildNumber: BUILD_NUMBER,
                            serverId: 'jfrog-artifactory-saas', // Obtain an Artifactory server instance, defined in Jenkins --> Manage:
                            spec: '''{
                                    "files": [
                                        {
                                        "pattern": "${artifactrepo}/${appname}:${BUILD_NUMBER}",
                                        "target": "/tmp/",
                                        "recursive": "false"
                                        } 
                                    ]
                                }'''    
                        )*/                         
                    }
        }


        stage('Deploy to ECS') {
                steps {
                    withAWS(credentials: 'awscreds', region: 'ap-southeast-2') {
                    sh 'aws ecs update-service --cluster ${cluster} --service ${service} --force-new-deployment'
                }
            }
            }
 
              
        /*
        post {
            always {
                echo 'Slack Notifications.'
                slackSend channel: '#jenkinscicd',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
            }
        }                      
        */ 
    }
}