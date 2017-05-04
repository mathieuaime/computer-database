node {
        stage("Docker MySQL") {
        	docker.image('mysqlcustom-test').withRun("--env MYSQL_ROOT_PASSWORD=root --network=isolated_nw --name=mysqltest") {
        	}
        }
        stage("Docker Maven") {
            docker.image('jamesdbloom/docker-java8-maven').withRun(" --network=isolated_nw --name=maventest") {
        	}
        }
}
