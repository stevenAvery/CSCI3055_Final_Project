(ns csci3055final.server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
              :as middleware]
            [org.httpkit.server :as http]
            [csci3055final.server.views :as views]
            [csci3055final.server.chatHandler :as chat]))

(def ^:const roomRegex #"\w+")

(defroutes app-routes
  (GET ["/chat/:room",    :room roomRegex] [room] views/indexPage)
  (GET ["/chat-ws/:room", :room roomRegex] [room] chat/chat-handler)
  (route/not-found (views/notFound)))

(def app
  (middleware/wrap-defaults app-routes middleware/site-defaults))

(defn -main [& args]
  (println "starting server ...")
  (http/run-server app {:port 8080}))
