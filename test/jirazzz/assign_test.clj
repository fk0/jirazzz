(ns jirazzz.assign-test
  (:require
    [clojure.test :refer [deftest is] :as t]
    [jirazzz.test-util :refer [jirazzz] :as tu]))


(t/use-fixtures :each tu/with-server-reset)


(deftest assign
  (tu/respond (:meta tu/responses))
  (tu/respond (:sprint tu/responses))
  (tu/respond (:assignee tu/responses))

  (let [r (jirazzz assign
                   --issue "JZ-123"
                   jzuser)]
    (is (= 0
           (:exit r))
        (:err r))
    (is (= "JZ-123 assigned to jzuser\n"
           (:out r))
        (:err r)))

  (is (= [{:uri (:meta tu/jira-paths)
           :method "get"
           :query-string "projectKeys=JZ"}
          ; TODO don't get sprint if we aren't going to use it.
          {:uri (:sprint tu/jira-paths)
           :method "get"}
          {:uri (:assignee tu/jira-paths)
           :method "put"
           :body {:name "jzuser"}}]
         (tu/requests))))

