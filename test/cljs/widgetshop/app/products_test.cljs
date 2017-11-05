(ns widgetshop.app.products-test
  (:require [cljs.test :as test :refer-macros [deftest is testing]]
            [widgetshop.app.products :as p]))

(deftest products-by-category
  (testing "Value is set to the right path"
    (is (= (p/products-by-category {} :foo [1 2 3])
           {:products-by-category {:foo [1 2 3]}}))))

(deftest setting-categories
  (is (= (p/set-categories {:bar :baz} {:foo [] :baz []})
         {:categories {:foo [] :baz []}
          :bar :baz})))

(deftest loading-products-by-category
  (is (= (p/load-products-by-category!
           {:categories [{:id 2 :name :foo}
                         {:id 1 :name :bar}]}
           (constantly "whatever doesn't matter")
           1)
         {:category {:id 1 :name :bar}
          :categories [{:id 2 :name :foo}
                       {:id 1 :name :bar}]
          :products-by-category {{:id 1 :name :bar} :loading}})))

(deftest by-category
  (testing "Products are fetched correctly"
    (let [db {:products-by-category {{:id 2 :name :foo-category} [{:id :product-1} {:id :product-2}]}}
          expected [{:id :product-1} {:id :product-2}]]
      (is (= (p/by-category db {:id 2 :name :foo-category}) expected)))))

(deftest add-to-cart
  (testing "Adding new item to category"
    (let [item {:id 1 :name :foo-item}
          db-before {:cart {}}
          expected {:cart {item 1}}]
      (is (= (p/add-to-cart db-before item) expected))))
  (testing "Adding another item to category"
    (let [item {:id 1 :name :foo-item}
          db-before {:cart {item 1}}
          expected {:cart {item 2}}]
      (is (= (p/add-to-cart db-before item) expected)))))

(deftest cart-size
  (testing "Empty cart"
    (let [db {:cart {}}
          expected 0]
      (is (= (p/cart-size db) expected ))))
  (testing "One product with two count"
    (let [item {:id 1 :name :foo-item}
          db {:cart {item 2}}
          expected 2]
      (is (= (p/cart-size db) expected))))
  (testing "Multiple products with multiple counts"
    (let [item {:id 1 :name :foo-item}
          item-2 {:id 1 :name :bar-item}
          db {:cart {item 2
                     item-2 6}}
          expected 8]
      (is (= (p/cart-size db) expected)))))



