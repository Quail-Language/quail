void setEncoding(string encoding) {}
string getEncoding() {}


string absolutePath(string path) {}
bool exists(string path) {}
bool isDirectory(string path) {}
bool isFile(string path) {}
list filesIn(string directory) {}
string fileName(string path) {}


bool createBlank(string path) {}
bool delete(string path) {}
bool deleteFile(string path) {}
bool deleteDirectory(string path) {}
void makeReadOnly(string path) {}
bool mkdirs(string path) {}
void writeBinary(string path, string base64Data) {}
string readBinary(string path) {}
