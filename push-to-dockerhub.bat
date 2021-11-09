docker build -t lgaljo/rt_basketball_users -f Dockerfile_with_maven_build .
docker tag lgaljo/rt_basketball_users lgaljo/rt_basketball_users:latest
docker push -a lgaljo/rt_basketball_users