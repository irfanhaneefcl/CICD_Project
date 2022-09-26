pipeline {
agent any
stages {
    stage('compile') {
	    steps { 
		    echo 'compiling..'
		    git url: 'https://github.com/irfanhaneefcl/CICD_Project'
		    sh script: ':/usr/share/gradle build'
	    }
    }
    stage('package/build-war') {
	    steps {
		    echo 'package......'
		    sh script: ':/usr/share/gradle package'	
	    }		
    }
    stage('build & push docker image') {
	    steps {
		    sh 'cd $WORKSPACE'
		    sh 'docker build --file Dockerfile --tag irfanhaneefcl/CICD_Project:$BUILD_NUMBER .'
		    withCredentials([string(credentialsId: 'DOCKER_HUB_LOGIN', variable: 'DOCKER_HUB_LOGIN')]) {
			    sh "docker login -u irfanhaneefcl -p ${DOCKER_HUB_LOGIN}"
		    }
		    sh 'docker push irfanhaneefcl/CICD_Project:$BUILD_NUMBER'
	    }
    }
    stage('Deploy-QA') {
	    steps {
		    sh 'ansible-playbook --inventory /tmp/myinv deploy/deploy-kube.yml --extra-vars "env=qa build=$BUILD_NUMBER"'
	    }
    }
}
}
