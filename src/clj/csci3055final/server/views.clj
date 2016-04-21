(ns csci3055final.server.views
  (require [hiccup.page :refer :all]))

(defn chat
  "generates the html for a chat box"
  []
  [:div {:id "chat"}
    [:div {:id "chatMessages"}
      [:textarea {:readonly "" :class "chatText"}]]
    [:div {:id "chatFoot"}
      [:form {:id "chatInput"}
        [:input {:type "text" :autofocus "autofocus" :class "chatText"}]
        [:input {:type "submit" :value "send"}]]
      [:div {:style "clear: both;"}]]
    ])

(defn getUsername
  "Get the username from the current client"
  []
  [:div {:id "loginDialog"}
    ;; title bar
    [:div {:id "loginTitle"}
      [:h3 "Username"]]

    ;; error
    [:div {:id "loginError"}
      [:p {:id "loginErrorText"}]]

    ;; content including form
    [:div {:id "loginContent"}
    [:form {:id "loginInput"}
      [:input {:type "text" :autofocus "autofocus" :class "chatText" :value "anonymous"}]
      [:input {:type "submit" :value "login"}]]
    [:div {:style "clear: both;"}]]
  ])

(defn indexPage
  "generates the html5 index page"
  [req]
  (html5 {:lang "en"}
  [:head
    [:title "CSCI3055 Final Project"]
    (include-css "/css/style.css")
    (include-js  "/out/goog/base.js"
                 "/js/main.js"
                 "/js/clientcore.js")]
    [:meta {:name "room" :content (get (:params req) :room)}]
  [:body
    (chat)
    (getUsername)
    ]))

(defn notFound
  "generates the html5 404 not found page"
  []
  (html5 {:lang "en"}
  [:head
    [:title "Page Not Found"]
    (include-css "/css/pageNotFound.css")]
  [:body
    [:div {:id "centreBox"}
      [:h1 "404 Page Not Found"]
      [:p "The page you've requested cannot be found"]
      [:a {:href "/chat/home"} [:p "return to home page"]]]]))
