(ns csci3055final.client.core
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as events]))

(enable-console-print!)

(def ^:const currentHost
  (str js/window.location.host))
(def ^:const websocketURI (str "ws://" currentHost "/chat-ws/"))
(def websocket (atom nil))

(defn giveLoginError
  [error]
  (dom/set-text! (dom/by-id "loginErrorText") error)
  (dom/set-style! (dom/by-id "loginDialog") :height "100px")) ;; TODO: fix this from being hard coded

(defn formattedMessage
  [key value]
  (str "{\"" key "\" : \"" value "\"}"))

(defn connectWebsocket
  [username]
  (let [room (dom/attr (css/sel "meta[name='room']") "content")]
    (reset! websocket (js/WebSocket. (str websocketURI room))))
  (doall
    (map #(aset @websocket (first %) (second %))
     [["onopen"
        (fn []
          (println "socket: open")
          ;; send the username
          (.send @websocket (formattedMessage "username" username)))]
      ["onclose"   (fn []  (println "socket: close"))]
      ["onerror"   (fn [error] (println (str "socket: error " error)))]
      ["onmessage" (fn [message]
        ;; append message to message area
        (let [data (.-data message)]
          (dom/append! (css/sel "#chatMessages textarea") (str data "\n"))))]])))

(defn onload
  "called when the DOM has been loaded"
  []

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
            (dom/set-style! (dom/by-id "chat") :display "inline") ;; show chat dialog
            (.focus (js/document.getElementById "chatInputText"))) ;; focus chat box

          (giveLoginError "username can only conatin leters, numbers, underscores, or periods.")))))

  ;; event listener for chat input
  (events/listen! (dom/by-id "chatInput")
    :submit
    (fn [e]
      (events/prevent-default e) ;; don't reload the page
      (let [inputText (dom/value (css/sel "#chatFoot input[type='text']"))]
        (if (not= inputText "")
          (do
            (.send @websocket (formattedMessage "message" inputText)) ;; send inputText to server
            ;;(dom/append! (css/sel "#chatMessages textarea"))
            (dom/set-value!
              (css/sel "#chatFoot input[type='text']") "")))))))

(defn unload
  "called when the DOM is unloaded"
  []
  ;; close the socket connection
  (.close @websocket))


(set! (.-onload js/window) onload)
(set! (.-unload js/window) unload)
