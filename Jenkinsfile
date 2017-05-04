node {
		stage("Checkout project") {
			
		}
        
        stage("Unit tests") {
        	sh "docker start mysqltest"
	        sh "docker run --network=isolated_nw  --name=maventest mavencustom-test"
	        sh "docker rm maventest"
	        sh "docker stop mysqltest"
        }
}
