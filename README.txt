## How to deploy
1. Clean existing gradle builds
```bash
gradle clean
```

2. Build gradle with changes
```bash
gradle build
```

3. Run script to publish JAR file to AWS
```bash
sh publish.sh AlexaCommands
```
