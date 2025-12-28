#!/usr/bin/env bash
set -e

echo "🔧 Installing cx (Huffman Compression CLI)..."

# Project paths
PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$PROJECT_ROOT/out"
BIN_DIR="$HOME/.local/bin"
JAR_NAME="cx.jar"
CMD_NAME="cx"

# Ensure Java exists
if ! command -v java >/dev/null 2>&1; then
  echo "❌ Java not found. Please install Java 17+ first."
  exit 1
fi

# Create build & bin directories
mkdir -p "$BUILD_DIR"
mkdir -p "$BIN_DIR"

echo "📦 Compiling Java sources..."
javac -d "$BUILD_DIR" src/main/java/compressor/*.java

echo "📦 Creating executable JAR..."
jar cfm "$BIN_DIR/$JAR_NAME" src/main/resources/META-INF/MANIFEST.MF -C "$BUILD_DIR" .

echo "🔗 Creating launcher script..."

cat > "$BIN_DIR/$CMD_NAME" << 'EOF'
#!/usr/bin/env bash
java -jar "$HOME/.local/bin/cx.jar" "$@"
EOF

chmod +x "$BIN_DIR/$CMD_NAME"

# Ensure PATH
if ! echo "$PATH" | grep -q "$HOME/.local/bin"; then
  echo ""
  echo "⚠️  ~/.local/bin is not in your PATH."
  echo "Add this line to your shell config or run it in your terminal:"
  echo ""
  echo 'export PATH="$HOME/.local/bin:$PATH"'
  echo ""
fi

echo ""
echo "✅ Installation complete!"
echo "Try running:"
echo "  cx <file>"
