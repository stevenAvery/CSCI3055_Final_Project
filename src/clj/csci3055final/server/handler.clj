;; lein2 ring server
(ns csci3055final.server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
              :as middleware]
            [org.httpkit.server :as http]
            [csci3055final.server.views :as views]))

(defonce channels (atom ()))

(defn chat-handler [req]
  (http/with-channel req currentChannel
    ;; websocket on connect
    (println "client connected")
    (swap! channels conj currentChannel)
    (http/send! currentChannel {:status 200
                                :headers {"Content-Type" "text/plain"}
                                :body    "welcome"})

      ;; websocket on close
    (http/on-close currentChannel
      (fn [status]
        (println "client disconnected")))

    ;; websocket on receive
    (http/on-receive currentChannel
      (fn [data]
        (println (str "recieved: \"" data "\""))
        ;;(http/send! currentChannel data)
        (doseq [channel @channels]
          (if (not= channel currentChannel) ;; send to all but currentChannel
            (do
              (println (str "sending \"" data "\" to: " channel))
              (http/send! channel data))))))))

(defroutes app-routes
  (GET "/" [] (views/indexPage))
  (GET "/ws" [] chat-handler)
  (route/not-found views/notFound))

(def app
  (middleware/wrap-defaults app-routes middleware/site-defaults))

(defn -main [& args]
  (println "starting server (http://127.0.0.1:8080/)...")
  (http/run-server app {:port 8080}))
