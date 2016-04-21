# CSCI3055_Final_Project
CSCI 3055U Final Project for 100493227
- Uses *clojurescript*, and *clojure*
- if you don't have leiningen (2.0.0 or greater) installed
```
sudo curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -o /usr/local/bin/lein
sudo chmod a+x /usr/local/bin/lein
```
- with leiningen installed the project can be built with:
```
lein cljsbuild once
lein run
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
