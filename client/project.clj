(defproject none "0.1.0-SNAPSHOT"
	:description "CSCI 3055U Final Project Client"
	:url "https://github.com/stevenAvery/CSCI3055_Final_Project"

	:dependencies [[org.clojure/clojure "1.7.0"]
	               [org.clojure/clojurescript "0.0-3211"]
	               [domina "1.0.3"]]

	:plugins [[lein-cljsbuild "1.1.3"]]

	:source-paths ["src/cljs"]

	:cljsbuild {
		:builds [{:id "CSCI3055UFinal"
		          :source-paths ["src/cljs"]
		          :compiler {
		          :output-to "src/js/CSCI3055UFinal.js"
		          :output-dir "src/out"
		          :optimizations :none
		          :source-map true}}]})
