;; lein2 ring server
(ns csci3055final.server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
              :as middleware]
            [org.httpkit.server :as http]
            [csci3055final.server.views :as views]))

(def ^:const roomRegex #"\w+")

(defonce webSockets (atom ()))

(defn chat-handler [req]
  (http/with-channel req currChannel
    (let [currRoom (get (:params req) :room)]
      ;; websocket on connect
      (println "client connected")
      ;; add client to list of the clients
      (swap! webSockets conj {:channel currChannel :room currRoom})
      ;; send response
      (http/send! currChannel {:status 200
                               :headers {"Content-Type" "text/plain"}
                               :body    (str "welcome to " currRoom)})

        ;; websocket on close
      (http/on-close currChannel
        (fn [status]
          (println "client disconnected")))

      ;; websocket on receive
      (http/on-receive currChannel
        (fn [data]
          (println (str "recieved: \"" data "\""))

          ;; pass the message onto all necessary clients
          (doseq [client @webSockets]
            (let [clientChannel (get client :channel)
                  clientRoom    (get client :room)]
              (if (= currRoom clientRoom)
                (do
                  (println (str "sending \"" data "\" to: " clientChannel))
                  (http/send! clientChannel data))))))))))

(defroutes app-routes
  (GET ["/index/:room", :room roomRegex] [room] views/indexPage)
  (GET ["/ws/:room",    :room roomRegex] [room] chat-handler)
  (route/not-found (views/notFound)))

(def app
  (middleware/wrap-defaults app-routes middleware/site-defaults))

(defn -main [& args]
  (println "starting server (http://127.0.0.1:8080/)...")
  (http/run-server app {:port 8080}))
