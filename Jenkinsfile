node {
		stage("Checkout project") {
			cd /code
			git checkout dev			
		}
        
        stage("Unit tests") {
        	sh "docker start mysqltest"
	        sh "docker run --network=isolated_nw  --name=maventest mavencustom"
	        sh "docker rm maventest"
	        sh "docker stop mysqltest"
        }
        
        stage("Deploy") {
        	echo 'recreate image'
        	echo 'recreate container'
        }
}