;; lein2 cljsbuild once

(ns csci3055final.client.core
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as events]
            [domina.xpath :as xpath]))

(enable-console-print!)

(def websocket (atom nil))

(defn giveLoginError
  [error]
  (dom/set-text! (dom/by-id "loginErrorText") error)
  (dom/set-style! (dom/by-id "loginDialog") :height "100px")) ;; TODO: fix this from being hard coded

(defn basicJSONGenerator
  [key value]
  (str "{\"" key "\" : \"" value "\"}"))

(defn connectWebsocket
  [username]
  (let [room (dom/attr (css/sel "meta[name='room']") "content")]
    (reset! websocket (js/WebSocket. (str "ws://localhost:8080/ws/" room))))
  (doall
    (map #(aset @websocket (first %) (second %))
     [["onopen"
        (fn []
          (println "OPEN")
          ;; send the username
          (.send @websocket (basicJSONGenerator "username" username)))]
      ["onclose"   (fn []  (println "CLOSE"))]
      ["onerror"   (fn [e] (println (str "ERROR: " e)))]
      ["onmessage" (fn [m]
        (let [data (.-data m)]
          (println (str "MESSAGE: " data))
          (dom/append! ;; append message to message area
            (css/sel "#chatMessages textarea")
            (str data "\n"))))]])))

(defn onload
  []
  (println "Dom loaded")

  ;; event listener for login input
  (events/listen! (dom/by-id "loginInput")
    :submit
    (fn [e]
      (events/prevent-default e) ;; don't reload the page
      (let [username (dom/value (css/sel "#loginContent input[type='text']"))]
        (if (re-matches #"[\w._]+" username) ;; check for valid username
          (do
            (dom/destroy! (dom/by-id "loginDialog")) ;; destroy login dialog
            (connectWebsocket username) ;; connect websocket
            (dom/set-style! (dom/by-id "chat") :display "inline")) ;; show chat dialog
          (giveLoginError "username can only conatin leters, numbers, underscore, or period")))))

  ;; event listener for chat input
  (events/listen! (dom/by-id "chatInput")
    :submit
    (fn [e]
      (events/prevent-default e) ;; don't reload the page
      (let [inputText (dom/value (css/sel "#chatFoot input[type='text']"))]
        (if (not= inputText "")
          (do
            (.send @websocket (basicJSONGenerator "message" inputText)) ;; send inputText to server
            ;;(dom/append! (css/sel "#chatMessages textarea"))
            (dom/set-value!
              (css/sel "#chatFoot input[type='text']") "")))))))

(defn unload
  []
  (.close @websocket))


(set! (.-onload js/window) onload)
(set! (.-unload js/window) unload)
