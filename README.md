# 📦 Huffman Compression Tool

A CLI tool for file compression and decompression, built using **Huffman Trees**, implemented fully from scratch in Java.

This project was created as a **learning-first exercise** to explore how classic Data Structures & Algorithms are used in real systems beyond interview style problems.

---

## 💠 Installation & Usage
1. git clone https://github.com/ViragJain3010/Compression-Tool
2. cd Compression-Tool
3. bash install.sh
4. cx <file-path> 

After installation, the `cx` command becomes available globally.

## ❇️ Why This Project?

> *“Is DSA only useful for cracking interviews?”*

That question kept bothering me.

So instead of solving another isolated problem, I decided to **build a real system application using DSA**, where design choices, edge cases, and trade-offs actually matter.

This project helped me understand how:
* trees, heaps, and recursion work together in practice
* bit-level operations are handled in real software
* algorithms turn into actual applications
* clean abstractions matter when systems grow

The result is a fully working **Huffman-based compression tool**, written from scratch without using existing compression libraries.

---

## ✴️ Features

* Compresses any file using **Huffman Coding**
* Decompresses `.cx` files back to the original content
* Bit-level I/O (no byte-aligned shortcuts)
* Streaming-based processing (memory efficient)
* Automatic detection of compressed/uncompressed files
* Handles edge cases:
  * Empty file validation
  * Single-character files
  * Highly skewed frequency distributions
* Fully tested using JUnit

---

## 💎 Core Concepts Used

* Greedy Algorithms
* Priority Queue / Min-Heap
* Binary Trees / Preorder Traversal
* Recursion
* Bit Manipulation
* File I/O & Streaming
* Tree Serialization / Deserialization
* Defensive validation & error handling

---

## 🏗️ High-Level Architecture

```
Input File
   ↓
Frequency Table
   ↓
Huffman Tree
   ↓
Prefix Codes
   ↓
Encoded Bit Stream
   ↓
[Header + Tree + Data]  →  .cx file
```

### Decompression Flow

```
.cx file
   ↓
Read Header
   ↓
Rebuild Huffman Tree
   ↓
Read Bits
   ↓
Traverse Tree
   ↓
Original File Restored
```

---

## 🗃️ Project Structure

```
src/main/java/compressor
│
├── CompressionTool.java     # Entry point (auto compress / decompress)
├── Encoder.java             # Compression pipeline
├── Decoder.java             # Decompression pipeline
│
├── HuffmanTree.java         # Tree construction + prefix table
├── HuffmanBaseNode.java     # Tree interface
├── HuffmanInternalNode.java # Internal node
├── HuffmanLeafNode.java     # Leaf node (symbol + weight)
│
├── BitInputStream.java      # Bit-level reading
├── BitOutputStream.java     # Bit-level writing
│
├── FileValidator.java       # Input validation
```

## 📥 How Compression Works

1. Read input file as a byte stream
2. Build a frequency table
3. Construct a Huffman Tree using a min-heap
4. Generate prefix-free binary codes
5. Write:
   * File extension
   * Original file size
   * Huffman Tree (preorder traversal)
   * Encoded data bit-by-bit
6. Pad final byte safely

---

## 📤 How Decompression Works

1. Read metadata (extension + original size)
2. Rebuild Huffman Tree from preorder bit stream
3. Traverse tree using incoming bits
4. Output bytes until original size is reached

---

## 🪅 Running the program

### Compile

```bash
# excluding tests
javac -d out $(find src/main -name "*.java") 

# including tests
javac -cp "lib/*" -d out $(find src -name "*.java") 
```

### Run

```bash
# java program 
java -cp out src.main.java.compressor.CompressionTool <file-path>

# including tests
java -jar lib/junit-platform-console-standalone-*.jar execute \
  -cp out \
  --scan-classpath
```

### Example

```bash
java -cp out src.main.java.compressor.CompressionTool example.txt
# → creates example.cx

java -cp out src.main.java.compressor.CompressionTool example.cx
# → restores example.txt
```

---

## 🧿 Testing

The project includes unit and integration tests covering:

* Huffman tree correctness
* Prefix-free validation
* Edge cases (single symbol, empty file)
* File validation logic
* Encoder correctness
* End-to-end compression → decompression

All tests are written using **JUnit 5**.

---

## 🤓 Nerd Notes

This section documents **real things I learned while building the project**, not just the final outcome.

### 1. Why the frequency table uses `int → int` instead of `char → int`

Initially, I tried using `char` as the key for the frequency table.
That worked for simple text, but broke once I started treating the file as raw bytes for I/O.

Since compression works at the **byte level**, each symbol must represent a value in the range `0-255`. Using `int` instead of `char` made this explicit and avoided incorrect assumptions about text encoding.

This shift helped me understand the difference between:

* *text characters*, and
* *raw binary symbols* used in compression algorithms.

---

### 2. What a “symbol” actually means

Before this project, I loosely thought of a symbol as a character or string.

While implementing Huffman coding, I learned that:

* a **symbol is simply a unit of data**
* in this project, a symbol = one byte (0-255)
* symbols are not tied to characters, encoding, or language

This distinction made the algorithm feel much more general and low-level, and clarified why Huffman coding works for *any* binary data, not just text.

---

### 3. Learning Test-Driven Development (TDD) the hard way

This project forced me to rethink how I usually code.

Earlier, my workflow was:

> write code -> run -> debug -> repeat

With tests, the flow became:

> define expected behavior -> write test -> write code to satisfy it

This felt unintuitive at first, especially when writing tests for logic that didn’t exist yet.
But over time, it helped me:

* think in terms of contracts and behavior
* design cleaner APIs
* catch edge cases early
* refactor with confidence

This shift in mindset was one of the hardest -- and most valuable -- parts of the project.

---

### 4. Why streaming matters (a lesson learned the hard way)

Initially, I read the entire input file into a `byte[]` before processing it. This worked for small examples, but while debugging and printing intermediate structures like frequency tables and Huffman trees, the output quickly became overwhelming even for small inputs.

That moment made the issue clear: loading entire files (or materializing too much intermediate data) does not scale.

This pushed me to redesign the implementation around streaming, where data is processed incrementally instead of all at once. As a result:

- memory usage stays bounded
- large files can be handled safely
- the implementation behaves closer to real-world compression tools

This helped me understand that streaming is not just an optimization, but a fundamental design principle in systems that process large data.

---

### 5. Building a real CLI tool (not just a Java program)

Before this project, I knew how to write programs and web apps -- but not how to ship a **command-line tool**.

Through this project, I learned:

* how Java programs become runnable JARs
* how a CLI entry point works
* how shell wrappers invoke executables
* how tools are installed into `$PATH`
* how commands like `cx file.txt` actually work

This helped me understand how developer tools are structured and distributed in practice.

---

## 💤 Final Note

This project was built as a **learning-first engineering exercise**, not a library.

It helped me move from *“knowing DSA concepts”* to *applying them in a real system* where design decisions, trade-offs, and correctness actually matter.

Thanks for reading, feedback and suggestions are always welcome.

---