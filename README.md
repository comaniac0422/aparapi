Description
=======
This is a fork of APARAPI from https://github.com/aparapi/aparapi that is customized for running on other external devices (especially for FPGAs). The modified Aparapi has external (EXT) mode which generates OpenCL host and kernel from Java application automatically. Users are able to use commercial tools to synthesize OpenCL source code to generate kernel bit-stream later on. Once the modified Aparapi detects the existed bit-stream kernel, it adopts that kernel and run the application on FPGAs.

This version is compatible with applications that written based on vanilla Aparapi. 

Environment Setup
=======
Installation guideline: Linux https://code.google.com/p/aparapi/wiki/DevelopersGuideLinux

Windows: https://code.google.com/p/aparapi/wiki/DevelopersGuideWindows

Environment variable: APARAPI_HOME = path/to/your/apaiapi/dir/

How to Use
======
- Build

ant clean build dist

- Sample run: 

-Generate OpenCL source code:

cd samples/mmul

./build_jar.sh

./run.sh EXT

-Synthesize OpenCL kernel and put the host and bit-stream together with the original Java class files.

-Execute application on FPGA.

./run.sh EXT

To Do
======
- Generated host: self-test.
- Introduce more Java language features.
- Include GATK, Mlib applications.
