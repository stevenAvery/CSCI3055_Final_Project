(ns CSCI3055UFinal.handler
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.util.response :as resp]
              [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
    (GET "/" [] "hello world")
    (route/not-found "Not Found"))

(def app
    (wrap-defaults app-routes site-defaults))
