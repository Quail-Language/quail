#?toc-entry setEncoding
#?toc-entry getEncoding
#?toc-entry absolutePath
#?toc-entry exists
#?toc-entry isDirectory
#?toc-entry isFile
#?toc-entry filesIn
#?toc-entry fileName
#?toc-entry createBlank
#?toc-entry delete
#?toc-entry deleteFile
#?toc-entry deleteDirectory
#?toc-entry makeReadOnly
#?toc-entry mkdirs
#?toc-entry writeBinary
#?toc-entry readBinary

#?html <h2><code>lang/fs</code></h2>
#?html <hr>

void setEncoding(string encoding) {
    #? Set encoding for all operations
}
string getEncoding() {}


string absolutePath(string path) {
    #? Converts relative path to absolute
}
bool exists(string path) {}
bool isDirectory(string path) {}
bool isFile(string path) {}
list filesIn(string directory) {
    #? Get list of file names (not paths) in given directory
}
string fileName(string path) {
    #? Get file name (with extension)
}


bool createBlank(string path) {
    #? Creates blank file
}
bool delete(string path) {
    #? Delete file or directory (and all its contents)
}
bool deleteFile(string path) {}
bool deleteDirectory(string path) {
    #? Delete directory and all its contents
}
void makeReadOnly(string path) {}
bool mkdirs(string path) {
    #? Creates non-existent directories on given path
}
void writeBinary(string path, string base64Data) {
    #? Write file in binary format (base64)
}
string readBinary(string path) {
    #? Read file in binary format (base64)
}
