set -e

# Clean up any previous function.zip
rm -rfv /lambda_src/function.zip

cd /lambda

ls -la

# Zip the file for lambda code update
zip -r9 /lambda_src/function.zip .
