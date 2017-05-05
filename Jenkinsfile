node {
		stage("Checkout project") {
			dir("/code") {
			    sh "git checkout dev"
			}
		}
        
        stage("Unit tests") {
        	sh "docker start mysqltest"
	        sh "docker run --network=isolated_nw  --name=maventest --volumes-from jenkins mavencustom"
	        sh "docker rm maventest"
	        sh "docker stop mysqltest"
        }
        
        stage("Deploy") {
        	echo 'recreate image'
        	echo 'recreate container'
        }
}