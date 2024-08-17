# FNVW Scripts

**All scripts must be executed from inside the script directory!**

## test.sh

This script is for testing the backend. Run it to see if all the integration tests pass.

## build.sh

This is the script for building the application. It first builds the frontend, then copies the artfifacts to the correct location and lastly builds the spring boot project.

## run.sh

This script runs the project. It asks you whether you want to run the project in detached mode or not. Press _y_ for detached mode, and _n_ for running not in detached mode.

## stop.sh

This script stops the project if it's running. It makes use of the 
docker compose down command.

