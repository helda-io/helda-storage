(ns helda.handlers.entities
  (:require
    [plumbing.core :refer [defnk]]
    [kekkonen.cqrs :refer :all]
    [schema.core :as s]

    [helda.schema :as hs]

    [helda.listeners.invoker :refer [fire-action]]
    [helda.storages.entities :as storage]
    ;todo get rid of :as
    [helda.storages.entity-listeners :as listeners-storage]
    )
  )

(defnk ^:query entities-by-world
  "Retrieves all entities for world."
  {:responses {:default {:schema [hs/Entity]}}}
  [
    [:components db]
    [:data world :- s/Keyword]
    ]
  (success
    (storage/find-entities-by-world db world)
    )
  )

(defnk ^:query entities-by-models
  "Retrieves all entities for world filtered by models."
  {:responses {:default {:schema [hs/Entity]}}}
  [
    [:components db]
    [:data world :- s/Keyword models :- [s/Keyword]]
    ]
  (success
    (storage/find-entities-by-world-and-models db world models)
    )
  )

(defnk ^:query entities-by-tags
  "Retrieves all entities for world filtered by tags."
  {:responses {:default {:schema [hs/Entity]}}}
  [
    [:components db]
    [:data world :- s/Keyword tags :- [s/Keyword]]
    ]
  (success
    (storage/find-entities-by-world-and-tags db world tags)
    )
  )

(defnk ^:query entities-by-tags-and-models
  "Retrieves all entities for world filtered by tags or models."
  {:responses {:default {:schema [hs/Entity]}}}
  [
    [:components db]
    [:data
      world :- s/Keyword
      tags :- [s/Keyword]
      models :- [s/Keyword]
      ]
    ]
  (success
    (storage/find-entities-by-tags-and-models db world tags models)
    )
  )

(defnk ^:query get-entity
  "Get entity by id"
  {:responses {:default {:schema hs/Entity}}}
  [
    [:components db]
    [:data id :- s/Str]
    ]
  (success
    (storage/find-entity-by-id db id)
    )
  )

(defnk ^:query listeners-by-entity-id
  "Get all entity listeners per id"
  {:responses {:default {:schema [hs/EntityListener]}}}
  [
    [:components db]
    [:data entity-id :- s/Str]
    ]
  (success
    (listeners-storage/find-listeners-by-entity-id
      db entity-id
      )
    )
  )

(defnk ^:command perform-action
  "Perform action"
  {:responses {:default {:schema hs/ActionResponse}}}
  [
    [:components db]
    data :- hs/ActionRequest
    ]
  (success
    (fire-action db data)
    )
  )

(defnk ^:command save-entity
  "Add entity"
  {:responses {:default {:schema hs/Entity}}}
  [
    [:components db]
    data :- hs/Entity
    ]
  ;todo add-entity
  (success
    (storage/save-entity db data)
    )
  )

(defnk ^:command add-entity-listener
  "Add entity listener"
  {:responses {:default {:schema hs/EntityListener}}}
  [
    [:components db]
    data :- hs/EntityListener
    ]
  (success
    (listeners-storage/save-listener db data)
    )
  )
