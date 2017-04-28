node {
        stage("Docker MySQL") {
        	docker.image('mysqlcustom-test').withRun("--env MYSQL_ROOT_PASSWORD=root -env MYSQL_DATABASE=computer-database-db2 --network=isolated_nw --name=mysqltest") {
        	}
        }
        stage("Docker Maven") {
            docker.image('jamesdbloom/docker-java8-maven').withRun(" --network=isolated_nw --name=maventest") {
            }
        }
}