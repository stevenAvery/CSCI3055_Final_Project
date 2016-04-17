;; lein2 cljsbuild once

(ns csci3055final.client.core
    (:require [domina :as dom]
              [domina.css :as css]
              [domina.events :as events]
              [domina.xpath :as xpath]))

(enable-console-print!)

;;(def ws (new js/WebSocket "ws://localhost:8080/ws"))
(def websocket (atom nil))


(defn onload
    []
    (println "Dom loaded")

    ;; connect websocket
    ;; based on https://github.com/aiba/clojurescript-chat-example/blob/master/src/cljs/cljs_chat_example/client/chat.cljs
    (reset! websocket (js/WebSocket. "ws://localhost:8080/ws"))
    (doall
        (map #(aset @websocket (first %) (second %))
           [["onopen" (fn []     (println "OPEN"))]
            ["onclose" (fn []    (println "CLOSE"))]
            ["onerror" (fn [e]   (println (str "ERROR: " e)))]
            ["onmessage" (fn [m] (println (str "MESSAGE: " (.-data m))))]]))

    ;; event listener for chat input
    (events/listen! (dom/by-id "chatInput")
        :submit
        (fn [e]
            (events/prevent-default e)
            (let [inputText (dom/value (css/sel "#chatFoot input[type='text']"))]
                (if (not= inputText "")
                    (do
                        (.send @websocket inputText)
                        (dom/append!
                            (css/sel "#chatMessages textarea")
                            (str "> " inputText "\n"))
                        (dom/set-value!
                            (css/sel "#chatFoot input[type='text']") "")))))))
(defn unload
    []
    (println "testing")
    (.close @websocket))


(set! (.-onload js/window) onload)
(set! (.-unload js/window) unload)
