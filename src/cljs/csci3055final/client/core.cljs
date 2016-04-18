;; lein2 cljsbuild once

(ns csci3055final.client.core
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as events]
            [domina.xpath :as xpath]))

(enable-console-print!)

(def websocket (atom nil))

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
          (let [username (dom/value (css/sel "#loginContent input[type='text']"))]
            (.send @websocket username)))]
      ["onclose"   (fn []  (println "CLOSE"))]
      ["onerror"   (fn [e] (println (str "ERROR: " e)))]
      ["onmessage" (fn [m]
        (let [data (.-data m)]
          (println (str "MESSAGE: " data))
          (dom/append! ;; append message to message area
            (css/sel "#chatMessages textarea")
            (str "SERVER: " data "\n"))))]])))

(defn onload
  []
  (println "Dom loaded")

  ;; event listener for login input
  (events/listen! (dom/by-id "loginInput")
    :submit
    (fn [e]
      (events/prevent-default e) ;; don't reload the page
      (dom/destroy! (dom/by-id "loginDialog")) ;; destroy login dialog
      (connectWebsocket) ;; connect websocket
      (dom/set-style! (dom/by-id "chat") :display "inline"))) ;; show chat dialog

  ;; event listener for chat input
  (events/listen! (dom/by-id "chatInput")
    :submit
    (fn [e]
      (events/prevent-default e) ;; don't reload the page
      (let [inputText (dom/value (css/sel "#chatFoot input[type='text']"))]
        (if (not= inputText "")
          (do
            (.send @websocket inputText) ;; send inputText to server
            (dom/append!
              (css/sel "#chatMessages textarea")
              (str "> " inputText "\n"))
            (dom/set-value!
              (css/sel "#chatFoot input[type='text']") "")))))))

(defn unload
  []
  (.close @websocket))


(set! (.-onload js/window) onload)
(set! (.-unload js/window) unload)
