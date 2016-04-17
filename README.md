# CSCI3055_Final_Project
CSCI 3055U Final Project for 100493227
- Uses *clojurescript*, and *clojure*
- can be built with:
``` sh
lein2 deps

# to compile client
lein2 cljsbuild once
# or
lein2 cljsbuild auto

# to compile and run server
lein2 ring server
```
- tentative structure:
```
.
├── resources
│   └── public
├── src
│   ├── clj
│   └── cljs
└── project.clj
```
