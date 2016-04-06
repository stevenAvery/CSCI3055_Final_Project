(ns CSCI3055UFinal.core
	(:require [domina :as dom]
	          [domina.css :as css]
	          [domina.events :as events]
	          [domina.xpath :as xpath]))

(enable-console-print!)

(defn onload []
	(println "Dom loaded")

	;; just for testing
	;;(dom/append! (dom/by-id "chat") "<p>testing</p>")

	(events/listen! (dom/by-id "chatInput")
		:submit
		(fn [e]
			(events/prevent-default e)

			(let [inputText (dom/value (css/sel "#chatFoot input[type='text']"))]
			(if (not= inputText "")
				(do
					(dom/append! (css/sel "#chatMessages textarea") (str "> " inputText "\n"))
					(dom/set-value! (css/sel "#chatFoot input[type='text']") "")))
			))


			))

(set! (.-onload js/window) onload)