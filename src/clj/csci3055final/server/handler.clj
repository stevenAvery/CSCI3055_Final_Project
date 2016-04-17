;; lein2 ring server
(ns csci3055final.server.handler
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.middleware.defaults :refer [wrap-defaults site-defaults] :as middleware]
              [org.httpkit.server :as http]
              [csci3055final.server.views :as views]))

(defonce channels (atom #{}))

(defn chat-handler [req]
    (http/with-channel req channel
        ;; websocket on connect
        (println "client connected")
        (swap! channels conj channel)
        (http/send! channel {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body    "server sending this over websocket"})

        ;; websocket on close
        (http/on-close channel (fn [status]
            (println "client disconnected")))

        ;; websocket on receive
        (http/on-receive channel (fn [data]
            (println (str "recieved: " data)
            (http/send! channel data))))))

(defroutes app-routes
    (GET "/" [] (views/indexPage))
    (GET "/ws" [] chat-handler)
    (route/not-found views/notFound))

(def app
    (middleware/wrap-defaults app-routes middleware/site-defaults))

(defn -main [& args]
    (println "starting server...")
    (http/run-server app {:port 8080}))
