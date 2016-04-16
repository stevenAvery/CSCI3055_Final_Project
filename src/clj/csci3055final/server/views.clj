(ns csci3055final.server.views
    (require [hiccup.page :refer :all]))

(defn chatbox
    []
    [:div {:id "chat"}
        ;;[:div {id "chatMessages"}]
        ;;[:div {id "chatFoot"}]
        ])

(defn indexPage
    []
    (html5 {:lang "en"}
    [:head
        [:title "CSCI3055 Final Project"]
        (include-css "resources/public/css/style.css")
        (include-js  "resources/public/out/goog/base.js"
                     "resources/public/js/main.js")
        ]
    [:body
        [:h1 "updated testing"]]))
