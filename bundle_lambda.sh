set -e

# Clean up any previous jar file
rm -rfv /lambda_src/app.zip

cd /lambda

# Move jar file to volume
mv app.zip ../lambda_src/
