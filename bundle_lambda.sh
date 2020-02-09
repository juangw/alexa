set -e

# Clean up any previous jar file
rm -rfv /lambda_src/app.jar

cd /lambda

# Move jar file to volume
mv app.jar ../lambda_src/
