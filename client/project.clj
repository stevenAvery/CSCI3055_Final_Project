(defproject none "0.1.0-SNAPSHOT"
	:description "CSCI 3055U Final Project"
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
		          ;; Outputs main file as none.js in current directory
		          ;; This file mainly consists of code loading other files
		          :output-to "src/js/CSCI3055UFinal.js"
		          ;; Where all the other files are stored. This folder must
		          ;; be accessible from your web page, as it will be loaded
		          ;; from JavaScript
		          :output-dir "src/out"
		          ;; The :none option is much faster than the other ones,
		          ;; and is the only one to provide correct srouce-maps.
		          :optimizations :none
		          ;; source-maps are used by the browser to show the
		          ;; ClojureScript code in the debugger
		          :source-map true}}]})
