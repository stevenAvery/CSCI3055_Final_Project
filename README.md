# CSCI3055_Final_Project
CSCI 3055U Final Project for 100493227
- Uses *clojurescript*, and *clojure*
- can be built with:
``` sh
lein2 cljsbuild once
# or
lein2 cljsbuild auto
```
- tentative structure:
```
.
├── client
│   └── src
│       ├── index.html
│       ├── css
│       │   └── style.css
│       ├── js
│       │   └── CSCI3055UFinal.js
│       └── cljs
│           └── CSCI3055UFinal
│               └── core.cljs
└── server
    └── src
        └── clj
```
