set -e

chmod u+x bundle_lambda.sh

FUNCTION_NAME=$1

(docker-compose -f docker-compose.deploy.yml build && docker-compose -f docker-compose.deploy.yml run lambda_bundler)

ZIP_PATH="function.zip"

aws lambda update-function-code \
  --region us-east-1 \
  --function-name $FUNCTION_NAME \
  --zip-file "fileb://$ZIP_PATH" \
  --publish \
  --cli-connect-timeout 6000

rm -rf $ZIP_PATH
