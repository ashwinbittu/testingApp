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
        cluster = "vprofile"
        service = "vprofileappsvc"

        appname = "testingapp"
        artficatreporeg = "https://ashwinbittu.jfrog.io"
        artifactrepo = "ashwinbittu.jfrog.io/docker-local/$appname"
        artifactrepocreds = 'jfrog-artifact-saas'
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
        stage("Quality Gate") {
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
                        sh 'docker tag ${appRegistry}:${BUILD_NUMBER} ${artifactrepo}:${BUILD_NUMBER}'
                        script {
                            docker.withRegistry( artficatreporeg, artifactrepocreds ) {
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

/*
        stage('Deploy to ecs') {
                steps {
                withAWS(credentials: 'awscreds', region: 'us-east-2') {
                sh 'aws ecs update-service --cluster ${cluster} --service ${service} --force-new-deployment'
                }
            }
            }
 
        */        
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