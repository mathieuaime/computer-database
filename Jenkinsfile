node {
	stage("Checkout project") {
		dir("/code") {
		    sh "git pull"
		}
	}

        stage("Unit tests") {
        	sh "docker start mysqltest"
	        sh "docker run --network=isolated_nw  --name=maventest --volumes-from jenkins mathieuaime/maven"
	        sh "docker rm maventest"
	        sh "docker stop mysqltest"
        }

        stage("Create image and push to DockerHub") {
        	dir("/code") {
        		def pom = readMavenPom file: 'pom.xml'
        		
        		sh "docker build -t computer-database ."
        		sh "docker tag computer-database mathieuaime/computer-database:${pom.version}"
        		sh "docker push mathieuaime/computer-database:${pom.version}"
        		sh "docker rmi computer-database"
        		sh "docker rmi mathieuaime/computer-database:${pom.version}"
        	}
        }
        
        stage("Deploy in production") {
            dir("/code") {
            	def pom = readMavenPom file: 'pom.xml'
            	
            	sh "docker stop computerdatabase || true"
            	sh "docker rm computerdatabase || true"
            	sh "docker run -d --name=computerdatabase --network=prod_nw mathieuaime/computer-database:${pom.version}"
        	} 
        }
}
