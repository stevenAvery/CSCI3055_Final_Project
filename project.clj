(defproject none "0.1.0-SNAPSHOT"
    :description "CSCI 3055U Final Project (Steven Avery)"
    :url "https://github.com/stevenAvery/CSCI3055_Final_Project"

    :min-lein-version "2.0.0"

    :plugins [[lein-ring "0.9.7"]
              [lein-cljsbuild "1.1.3"]]

    :dependencies [[org.clojure/clojure "1.7.0"]
                   [compojure "1.4.0"]
                   [ring/ring-defaults "0.1.5"]
                   [hiccup "1.0.4"]
                   [org.clojure/clojurescript "0.0-3211"]
                   [domina "1.0.3"]]

    :source-paths ["src/clj" "src/cljs"]

    :ring {:handler csci3055final.server.handler/app
           :port 8080
           :auto-reload? true
           :auto-refresh? true}
    :profiles
    {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                          [ring/ring-mock "0.3.0"]]}}

    :cljsbuild {
        :builds [{:source-paths ["src/cljs"]
                  :compiler {
                      :output-to "resources/public/js/main.js"
                      :output-dir "resources/public/out"
                  :optimizations :none
                  :source-map true}}]})
