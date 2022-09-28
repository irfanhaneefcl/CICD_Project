pipeline {
agent any
stages {
    stage('compile') {
	    steps { 
		    echo 'compiling..'
		    git url: 'https://github.com/irfanhaneefcl/edureka_project2'
		    sh script: '/opt/maven/bin/mvn compile'
	    }
    }
    stage('package/build-war') {
	    steps {
		    echo 'package......'
		    sh script: '/opt/maven/bin/mvn package'	
	    }		
    }
    stage('build & push docker image') {
	    steps {
		    sh 'cd $WORKSPACE'
		    sh 'docker build --file Dockerfile --tag irfanhaneefcl/edureka_project2:$BUILD_NUMBER .'
		    withCredentials([string(credentialsId: 'DOCKER_HUB_LOGIN', variable: 'DOCKER_HUB_LOGIN')]) {
			    sh "docker login -u irfanhaneefcl -p ${DOCKER_HUB_LOGIN}"
		    }
		    sh 'docker push irfanhaneefcl/edureka_project2:$BUILD_NUMBER'
	    }
    }
    stage('Deploy-QA') {
	    steps {
		    sh 'ansible-playbook --inventory /etc/ansible/myinv deploy/deploy-kube.yml --extra-vars "env=qa build=$BUILD_NUMBER"'
	    }
    }
}
}
