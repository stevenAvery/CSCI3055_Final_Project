;; lein2 ring server

(ns csci3055final.server.handler
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.util.response :as resp]
              [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
              [csci3055final.server.views :as views]))

(defroutes app-routes
    (GET "/" [] (views/indexPage))
    (route/not-found (views/notFound)))

(def app
    (wrap-defaults app-routes site-defaults))
