node {
        stage("Docker MySQL") {
        	docker.image('mysql').withRun("-e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=computer-database-test") {
        	}
        }
        stage("Docker Maven") {
            docker.image('jamesdbloom/docker-java8-maven') {
            }
        }
}