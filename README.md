javac -cp "lib/*" -d out $(find src -name "*.java")

java -jar lib/junit-platform-console-standalone-*.jar execute \
  -cp out \
  --scan-classpath

1. buildFrequencyTable
2. buildHuffmanTree
3. buildPrefixCodeTable