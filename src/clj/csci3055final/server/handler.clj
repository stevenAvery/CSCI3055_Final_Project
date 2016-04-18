;; lein2 ring server
(ns csci3055final.server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
              :as middleware]
            [org.httpkit.server :as http]
            [clojure.data.json :as json]
            [csci3055final.server.views :as views]))

(def ^:const roomRegex #"\w+")

(defonce webSockets (atom ()))

(defn webSocketMatch
  [key value]
  (first (filter #(= (get % key) value) @webSockets)))

(defn parseJSON
  "basic JSON parsing for websocket data"
  [inputJSON]
  (first (json/read-str inputJSON)))

(defn sendMessageToRoom
  "sends the given message to all users in given room"
  [room message]
  ;; search through all web sockets to find the ones that are in the given room
  (doseq [client @webSockets]
    (let [clientChannel (get client :channel)
          clientRoom    (get client :room)]
      (if (= room clientRoom)
          (http/send! clientChannel message)))))

(defn websocketUsername
  "with the initial connect the user sends there username"
  [webSockets parsedData currChannel currRoom]
  ;; add client to list of the clients
  (swap! webSockets conj {:channel currChannel
                          :room currRoom
                          :username (second parsedData)})
  ;; let the entire room know, that the client has joined
  (sendMessageToRoom currRoom (str (second parsedData) " has entered the chat")))


(defn websocketMessage
  "manage incomming messages"
  [webSockets data currChannel currRoom]
  ;; pass the message onto all necessary clients
  (let [username (get (webSocketMatch :channel currChannel) :username)
        messageToSend (str username ": " data)]
    (sendMessageToRoom currRoom messageToSend))
  )

(defn chat-handler
  "handling the chat websocket"
  [req]
  (http/with-channel req currChannel
    (let [currRoom (get (:params req) :room)]
      ;; websocket on connect
      (println (str "client connected to " currRoom))

        ;; websocket on close
      (http/on-close currChannel
        (fn [status]
          (println (str "client disconnected from " currRoom))))

      ;; websocket on receive
      (http/on-receive currChannel
        (fn [data]
          (let [parsedData (parseJSON data)]
            (cond
              ;; initial connect (send username)
              (= (first parsedData) "username")
                (websocketUsername webSockets parsedData currChannel currRoom)
              (= (first parsedData) "message")
                (websocketMessage webSockets (second parsedData) currChannel currRoom)
              )))))))

(defroutes app-routes
  (GET ["/chat/:room",    :room roomRegex] [room] views/indexPage)
  (GET ["/chat-ws/:room", :room roomRegex] [room] chat-handler)
  (route/not-found (views/notFound)))

(def app
  (middleware/wrap-defaults app-routes middleware/site-defaults))

(defn -main [& args]
  (println "starting server (http://127.0.0.1:8080/)...")
  (http/run-server app {:port 8080}))
