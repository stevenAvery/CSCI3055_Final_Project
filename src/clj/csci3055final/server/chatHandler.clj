(ns csci3055final.server.chatHandler
  (:require [compojure.core :refer :all]
            [org.httpkit.server :as http]
            [clojure.data.json :as json]
            [csci3055final.server.views :as views]))

(defonce webSockets (atom #{}))

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
  [webSockets dataContent currChannel currRoom]
  ;; add client to list of the clients
  (swap! webSockets conj {:channel currChannel
                          :room currRoom
                          :username dataContent})
  ;; let the entire room know, that the client has joined
  (sendMessageToRoom currRoom (str dataContent " has entered the chat")))


(defn websocketMessage
  "manage incomming messages"
  [webSockets dataContent currChannel currRoom]
  ;; pass the message onto all necessary clients
  (let [username (get (webSocketMatch :channel currChannel) :username)
        messageToSend (str username ": " dataContent)]
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
          (println (str "client disconnected from " currRoom))

          ;; tell the room the user left
          (let [username (get (webSocketMatch :channel currChannel) :username)]
            (sendMessageToRoom currRoom (str username " has left the chat"))

          ;; remove the channel from the list
          (swap! webSockets disj (webSocketMatch :channel currChannel)))))

      ;; websocket on receive
      (http/on-receive currChannel
        (fn [data]
          (let [parsedData (parseJSON data)]
            (cond
              ;; if client sends username
              (= (first parsedData) "username")
                (websocketUsername webSockets (second parsedData) currChannel currRoom)
              ;; if client sends message
              (= (first parsedData) "message")
                (websocketMessage  webSockets (second parsedData) currChannel currRoom))))))))
