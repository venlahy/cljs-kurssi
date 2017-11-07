(ns widgetshop.app.state
  "Defines the application state atom"
  (:require [reagent.core :as r]
            [widgetshop.db :as db]
            [cljs.spec.alpha :as s]))

(defonce app (r/atom {:cart []
                      :categories :loading ;; list of product categories
                      :category nil ;; the selected category

                      ;; Loaded product listings keyd by selected category
                      :products-by-category {}}))

(defn update-state!
  "Updates the application state using a function, that accepts as parameters
  the current state and a variable number of arguments, and returns a new state.

  (defn set-foo [app n]
     (assoc app :foo n))

  (update-state! set-foo 1)"
  [update-fn & args]
  (swap! app
         (fn [current-app-state]
           (let [result (apply update-fn current-app-state args)]
             (if (s/valid? ::db/db result)
                 result
                 (do
                   (throw (ex-info (str "spec check failed: " (s/explain-str ::db/db result)) {}))))))))

