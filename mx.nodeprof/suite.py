suite = {
  "mxversion" : "5.174.0",
  "name" : "nodeprof",

  "imports" : {
    "suites" : [
      {
        "name" : "graal-nodejs",
        "version" : "6f5372a759e7b1783bf8f56b8eaa49400ad11534",
        "subdir" : True,
        "urls" : [
          {"url" : "https://github.com/graalvm/graaljs.git", "kind" : "git"},
        ]
      }
    ],
  },

  "developer" : {
    "name" : "Haiyang Sun",
    "email" : "haiyang.sun@usi.ch",
    "organization" : "USI",
    "organizationUrl" : "http://www.inf.usi.ch/",
  },

  "licenses" : {
    "Apache" : { 
      "name" : "Apache License, Version 2.0",
      "url" : "http://www.apache.org/licenses/LICENSE-2.0",
    },
  },

  "defaultLicense" : "Apache",


  "projects" : {
    "ch.usi.inf.nodeprof" : {
      "subDir" : "src",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "graal-js:GRAALJS"
      ],
      "annotationProcessors" : ["truffle:TRUFFLE_DSL_PROCESSOR"],
      "javaCompliance" : "1.8",
      "workingSets" : "Truffle,JavaScript",
      "checkstyle" : "ch.usi.inf.nodeprof",
    },
    "ch.usi.inf.nodeprof.test" : {
      "subDir" : "src",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "ch.usi.inf.nodeprof",
        "mx:JUNIT"
      ],
      "annotationProcessors" : ["truffle:TRUFFLE_DSL_PROCESSOR"],
      "javaCompliance" : "1.8",
      "workingSets" : "Truffle,JavaScript,Test",
      "testProject": True,
      "checkstyle" : "ch.usi.inf.nodeprof",
    },
  },

  "distributions" : {
    "NODEPROF" : {
      "path" : "build/nodeprof.jar",
      "sourcesPath" : "build/nodeprof.src.zip",
      "dependencies" : ["ch.usi.inf.nodeprof"],
      "distDependencies" : [
        "graal-js:GRAALJS"
      ],
      "description" : "NodeProf for Graal Node.js",
      "maven" : {
        "artifactId" : "nodeprof",
      }
    },
    "NODEPROF_TEST" : {
      "path" : "build/nodeprof-test.jar",
      "sourcesPath" : "build/nodeprof-test.src.zip",
      "dependencies" : ["ch.usi.inf.nodeprof.test"],
      "distDependencies" : [
        "NODEPROF",
        "graal-js:GRAALJS"
      ],
      "description" : "NodeProf Tests",
      "maven" : {
        "artifactId" : "nodeprof-test",
      },
    },
    "NODEPROF_GRAALVM_SUPPORT" : {
      "native" : True,
      "description" : "NodeProf support distribution for the GraalVM",
      "layout" : {
        "./" : [
          "file:mx.nodeprof/native-image.properties",
          "file:src/ch.usi.inf.nodeprof/js/analysis/trivial/emptyTemplate.js",
          "file:src/ch.usi.inf.nodeprof/js/jalangi.js",
          "file:README.md",
          "file:Tutorial.md"
        ],
      },
    },
  },
}
