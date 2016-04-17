;; lein2 cljsbuild once

(ns csci3055final.client.core
    (:require [domina :as dom]
              [domina.css :as css]
              [domina.events :as events]
              [domina.xpath :as xpath]))

(enable-console-print!)

(def websocket* (atom nil))

(defn onload
    []
    (println "Dom loaded")

    (reset! websocket* (js/WebSocket. "ws://localhost:8080/"))

    (events/listen! (dom/by-id "chatInput")
        :submit
        (fn [e]
            (events/prevent-default e)
            (let [inputText (dom/value (css/sel "#chatFoot input[type='text']"))]
                (if (not= inputText "")
                    (do
                        (dom/append!
                            (css/sel "#chatMessages textarea")
                            (str "> " inputText "\n"))
                        (dom/set-value!
                            (css/sel "#chatFoot input[type='text']") "")))))))
(defn unload
    []
    (println "testing"))


(set! (.-onload js/window) onload)
(set! (.-unload js/window) unload)
