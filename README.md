### Guide to CREATE A `CICD Pipeline` on Jenkins Using Docker & Ansible  

## Step1 -- `Pre-Requisite`

```
      Create a VM (on any cloud OR Virtual Box) with 1CPU 2GB RAM a

      login to the VM Install below tools
      
```
![](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/1_jenkins_vm.PNG)

## Step2 -- `Install Java & Jenkins`
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installJenkins.sh -P /tmp
sudo chmod 755 /tmp/installJenkins.sh
sudo bash /tmp/installJenkins.sh
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/2_java_install.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/3_jenkins_install.PNG)

## Step3 -- `Install Maven` 
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installMaven.sh -P /tmp
sudo chmod 755 /tmp/installMaven.sh
sudo bash /tmp/installMaven.sh
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/4_maven_install.PNG)

## Step4 -- `Install Docker`
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installDocker.sh -P /tmp
sudo chmod 755 /tmp/installDocker.sh
sudo bash /tmp/installDocker.sh
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/5_docker_version.PNG)

## Step5 -- `Install Ansible`
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installAnsible.sh -P /tmp
sudo chmod 755 /tmp/installAnsible.sh
sudo bash /tmp/installAnsible.sh
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/6_ansible_install.PNG)
```
# modify the ansible config file to ensure disable host key checking 

vi /etc/ansible/ansible.cfg 

# uncomment this to disable SSH key host checking
host_key_checking = False
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/7_ansible_config.PNG)

## Step6 -- `Login to Jenkins UI`

> **hit `http://IP:8080` in browser   ## incase of cloud please use Public IP ensure the Port is allowed to access**


![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/8_jenkins_login.PNG)
```
	enter `initialAdminPassword` the page to login ( cat /var/lib/jenkins/secrets/initialAdminPassword )

	click on `Install Suggested Plugins`
	
	continue next and finish the setup. 
```

## Step7 -- `Install reqired Plugins (Install from Jenkins UI)`
```
install all these from Jenkins UI 

  Manage Jenkins --> manage plugins -- Available -- search & install the below
  	1) warnings NG
  	2) cobertura
  	3) Junit
  	4) Build Pipeline
  	5) Docker Piepeline
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/9_Jenkins_plugin_search_Install.PNG)

## Step8 -- `Create Credentials (Setup from Jenkins UI)`

```
  Manage Jenkins -->  Manage Credentials ==> Stores scoped to Jenkins - global ==> Add Credentials 
	--> kind: username with password 
	--> scope: Global
	--> username: <enter your docker hub id>
	--> password: <enter your docker hub password> 
	--> ID: DOCKER_HUB_LOGIN 
	--> Description: DOCKER_HUB_LOGIN
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/14_credentials_jenkins_config.PNG)

## Step9 -- `Configure JAVA - MAVEN - Git (Setup from Jenkins UI)`

```
Java configuration in Jenkins console 
	
	Manage Jenkins --> Global Tool Configuration --> JDK --> Add JDK
		Name: myjava ( can be any string )
		JAVA_HOME: /path/to/javahome ( ex: /usr/lib/jvm/java-8-openjdk-amd64 )
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/10_java_ jenkins_config.PNG)
```
Maven Configuration in Jenkins console
	
	Manage Jenkins --> Global Tool Configuration --> Maven --> Add Maven
		Name: maven3.6 ( can be any string )
		MAVEN_HOME: /path/to/mavenhome ( ex: /opt/apache-maven-3.6.5 )
```
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/11_maven_jenkins_config.PNG)
```
Git Configuration in Jenkins console
	
	Manage Jenkins --> Global Tool Configuration --> Git --> Add Git
		Name: git ( can be any string )
		MAVEN_HOME: /path/to/githome ( ex: /usr/bin/git )
```

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/13_git_Jenkins_config.PNG)

## Step10 -- `configure Jenkins with Docker - from Jenkin Server CLI` 


> by default Jenkins process runs with Jenkins User, which mean any jenkins Jobs we run from jenkins console will be running jenkins user on Jenkins machine

> we need to configure Jenkins user can run the docker commands by adding jenkins user to docker group

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/12_docker_jenkins_config.PNG)

```
    sudo usermod -aG docker jenkins
```		   

> **`restart the Jenkins Service`**  

```
    sudo service jenkins restart
```	

> **` validate, run docker command with jenkins`** 

```
	su - jenkins           ## switch to jenkins user
	docker ps              ## to list any containers running
	docker pull nginx      ## pull a docker image 
```


![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/15_config_jenkins_with_docker.PNG)

##### if the above commands execute without any error then we configured jenkins user properly 

## Step11 -- `Setup Deployment Environments`

> Setup kubernetes cluster

> Step1: On Master Node

> **` ## Install Docker`** 
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installDocker.sh -P /tmp
sudo chmod 755 /tmp/installDocker.sh
sudo bash /tmp/installDocker.sh
sudo systemctl restart docker
```

> **`## Install kubeadm,kubelet,kubectl`**
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installK8S-v1-23.sh -P /tmp
sudo chmod 755 /tmp/installK8S-v1-23.sh
sudo bash /tmp/installK8S-v1-23.sh
```

>**` ## Initialize kubernetes Master Node`**
 ```
   sudo kubeadm init --ignore-preflight-errors=all

   sudo mkdir -p $HOME/.kube
   sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
   sudo chown $(id -u):$(id -g) $HOME/.kube/config
 ```
 
 >**` ## Install weave networking driver`**
 ```    
   sudo kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')" 
```

>**`Validate:  kubectl get nodes`**

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/16_kube_master.PNG)

> Step2: On All Worker Nodes

> **`Install Docker`**

 ```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installDocker.sh -P /tmp
sudo chmod 755 /tmp/installDocker.sh
sudo bash /tmp/installDocker.sh
sudo systemctl restart docker
```

> **`Install kubeadm,kubelet,kubectl`**
```
sudo wget https://raw.githubusercontent.com/lerndevops/labs/master/scripts/installK8S-v1-23.sh -P /tmp
sudo chmod 755 /tmp/installK8S-v1-23.sh
sudo bash /tmp/installK8S-v1-23.sh
```

> **`Run Below on Master Node to get join token`**
```
kubeadm token create --print-join-command 

    copy the kubeadm join token from master & run it on all nodes
```

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/17_kube_worker1.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/18_kube_cluster_nodes.PNG)

## Step12 -- `Setup Ansible Inventory on Jenkins machine using CLI`
```
   vi /etc/ansible/myinv 
      enter your servers in groups like qa & prod 
   sudo chmod 755 /etc/ansible/myinv
   sudo chown jenkins:jenkins /etc/ansible/myinv
```

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/19_ansible_myinv.PNG)

## Step 13: Now Let's start creating CICD Pipeline Using Pipeline As Code Script

> **`Jenkins ( home page )`** 

```
  --> Click on New Item from left menu 
  --> Enter an item name: edureka_project
  --> Choose: Pipeline 
  --> Click: ok
```

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/20_jenkins_pipeline_setup_1.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/21_jenkins_pipeline_setup_2.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/22_jenkins_pipeline_setup_3.PNG)

> **`insdie job parameters as below`**

```
-->  Click on Pipeline (TAB) on top 
-->  Definition (drop down): Pipeline Script from SCM
-->  SCM (drop down): Git
-->  Repositories --> Repositories URL --> https://github.com/irfanhaneefcl/edureka_project2
-->  Script Path: /jenkins/cicd-docker-ansible-kube.gvy

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/23_jenkinsfile.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/24_dockerfile.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/24_kube_deploy.PNG)
	
-->  Click on Save 
-->  Build Now from left Menu 
```

![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/25_jenkins_pipeline1.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/26_Kube1.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/27_replica5.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/28_jenkins_build_replica.PNG)
![Snapshot](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/29_kube_pods_view.PNG)
![](https://github.com/irfanhaneefcl/edureka_project2/blob/master/snapshots/30_application_view_in_browser.PNG)

>**'Build Logs from Jenkins Console Output'**

```
Started by user Irfan Haneef CL
Obtained jenkins/cicd-docker-ansible-kube.gvy from git https://github.com/irfanhaneefcl/edureka_project2
[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins in /var/lib/jenkins/workspace/edureka_project
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Declarative: Checkout SCM)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > /usr/bin/git rev-parse --resolve-git-dir /var/lib/jenkins/workspace/edureka_project/.git # timeout=10
Fetching changes from the remote Git repository
 > /usr/bin/git config remote.origin.url https://github.com/irfanhaneefcl/edureka_project2 # timeout=10
Fetching upstream changes from https://github.com/irfanhaneefcl/edureka_project2
 > /usr/bin/git --version # timeout=10
 > git --version # 'git version 2.34.1'
 > /usr/bin/git fetch --tags --force --progress -- https://github.com/irfanhaneefcl/edureka_project2 +refs/heads/*:refs/remotes/origin/* # timeout=10
 > /usr/bin/git rev-parse refs/remotes/origin/master^{commit} # timeout=10
Checking out Revision e1177ed7cddd25b2c1f20fb92179698efa7c900c (refs/remotes/origin/master)
 > /usr/bin/git config core.sparsecheckout # timeout=10
 > /usr/bin/git checkout -f e1177ed7cddd25b2c1f20fb92179698efa7c900c # timeout=10
Commit message: "Update deploy-kube.yml"
 > /usr/bin/git rev-list --no-walk af10c579039cb22eace8f244e5d1d8dc69a72469 # timeout=10
[Pipeline] }
[Pipeline] // stage
[Pipeline] withEnv
[Pipeline] {
[Pipeline] stage
[Pipeline] { (compile)
[Pipeline] echo
compiling..
[Pipeline] git
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > /usr/bin/git rev-parse --resolve-git-dir /var/lib/jenkins/workspace/edureka_project/.git # timeout=10
Fetching changes from the remote Git repository
 > /usr/bin/git config remote.origin.url https://github.com/irfanhaneefcl/edureka_project2 # timeout=10
Fetching upstream changes from https://github.com/irfanhaneefcl/edureka_project2
 > /usr/bin/git --version # timeout=10
 > git --version # 'git version 2.34.1'
 > /usr/bin/git fetch --tags --force --progress -- https://github.com/irfanhaneefcl/edureka_project2 +refs/heads/*:refs/remotes/origin/* # timeout=10
 > /usr/bin/git rev-parse refs/remotes/origin/master^{commit} # timeout=10
Checking out Revision e1177ed7cddd25b2c1f20fb92179698efa7c900c (refs/remotes/origin/master)
 > /usr/bin/git config core.sparsecheckout # timeout=10
 > /usr/bin/git checkout -f e1177ed7cddd25b2c1f20fb92179698efa7c900c # timeout=10
 > /usr/bin/git branch -a -v --no-abbrev # timeout=10
 > /usr/bin/git branch -D master # timeout=10
 > /usr/bin/git checkout -b master e1177ed7cddd25b2c1f20fb92179698efa7c900c # timeout=10
Commit message: "Update deploy-kube.yml"
[Pipeline] sh
+ /opt/maven/bin/mvn compile
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< q3.group.id:eduproj >-------------------------
[INFO] Building eduproj unspecified
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ eduproj ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /var/lib/jenkins/workspace/edureka_project/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ eduproj ---
[INFO] No sources to compile
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.614 s
[INFO] Finished at: 2022-09-28T06:28:54Z
[INFO] ------------------------------------------------------------------------
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (package/build-war)
[Pipeline] echo
package......
[Pipeline] sh
+ /opt/maven/bin/mvn package
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< q3.group.id:eduproj >-------------------------
[INFO] Building eduproj unspecified
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ eduproj ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /var/lib/jenkins/workspace/edureka_project/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ eduproj ---
[INFO] No sources to compile
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ eduproj ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /var/lib/jenkins/workspace/edureka_project/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ eduproj ---
[INFO] No sources to compile
[INFO] 
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ eduproj ---
[INFO] No tests to run.
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ eduproj ---
[WARNING] JAR will be empty - no content was marked for inclusion!
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.106 s
[INFO] Finished at: 2022-09-28T06:28:59Z
[INFO] ------------------------------------------------------------------------
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (build & push docker image)
[Pipeline] sh
+ cd /var/lib/jenkins/workspace/edureka_project
[Pipeline] sh
+ docker build --file Dockerfile --tag irfanhaneefcl/edureka_project2:3 .
Sending build context to Docker daemon    510kB

Step 1/7 : FROM node:carbon
 ---> 8eeadf3757f4
Step 2/7 : WORKDIR /usr/src/app
 ---> Using cache
 ---> b85793de0464
Step 3/7 : COPY package*.json ./
 ---> Using cache
 ---> 451afb107e78
Step 4/7 : RUN npm install
 ---> Using cache
 ---> 6d291c8e61a9
Step 5/7 : COPY . .
 ---> 97363741f234
Step 6/7 : EXPOSE 8080
 ---> Running in 2792bde448b3
Removing intermediate container 2792bde448b3
 ---> 43d5f78198a5
Step 7/7 : CMD [ "npm", "start" ]
 ---> Running in f57b276dbfb0
Removing intermediate container f57b276dbfb0
 ---> 88a7b217fb61
Successfully built 88a7b217fb61
Successfully tagged irfanhaneefcl/edureka_project2:3
[Pipeline] withCredentials
Masking supported pattern matches of $DOCKER_HUB_LOGIN
[Pipeline] {
[Pipeline] sh
Warning: A secret was passed to "sh" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DOCKER_HUB_LOGIN]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
+ docker login -u irfanhaneefcl -p ****
WARNING! Using --password via the CLI is insecure. Use --password-stdin.
WARNING! Your password will be stored unencrypted in /var/lib/jenkins/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] sh
+ docker push irfanhaneefcl/edureka_project2:3
The push refers to repository [docker.io/irfanhaneefcl/edureka_project2]
38b642e30f8e: Preparing
b85fd73b0321: Preparing
0cca4a97dd0a: Preparing
eddf066f9c0d: Preparing
423451ed44f2: Preparing
b2aaf85d6633: Preparing
88601a85ce11: Preparing
42f9c2f9c08e: Preparing
99e8bd3efaaf: Preparing
bee1e39d7c3a: Preparing
1f59a4b2e206: Preparing
0ca7f54856c0: Preparing
ebb9ae013834: Preparing
b2aaf85d6633: Waiting
88601a85ce11: Waiting
42f9c2f9c08e: Waiting
99e8bd3efaaf: Waiting
bee1e39d7c3a: Waiting
1f59a4b2e206: Waiting
0ca7f54856c0: Waiting
ebb9ae013834: Waiting
0cca4a97dd0a: Layer already exists
eddf066f9c0d: Layer already exists
423451ed44f2: Layer already exists
b85fd73b0321: Layer already exists
88601a85ce11: Layer already exists
b2aaf85d6633: Layer already exists
42f9c2f9c08e: Layer already exists
99e8bd3efaaf: Layer already exists
1f59a4b2e206: Layer already exists
bee1e39d7c3a: Layer already exists
ebb9ae013834: Layer already exists
0ca7f54856c0: Layer already exists
38b642e30f8e: Pushed
3: digest: sha256:f2156a5ba8480d75bc77ba1b4ccec295f8ea6de4096419c151c3119fd7bda360 size: 3053
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Deploy-QA)
[Pipeline] sh
+ ansible-playbook --inventory /etc/ansible/myinv deploy/deploy-kube.yml --extra-vars env=qa build=3

PLAY [qa] **********************************************************************

TASK [Gathering Facts] *********************************************************
ok: [18.206.255.118]

TASK [Deploying Application pods...] *******************************************
changed: [18.206.255.118]

TASK [deploying service] *******************************************************
changed: [18.206.255.118]

TASK [increase replicas] *******************************************************
changed: [18.206.255.118]

PLAY RECAP *********************************************************************
18.206.255.118             : ok=4    changed=3    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   

[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```


