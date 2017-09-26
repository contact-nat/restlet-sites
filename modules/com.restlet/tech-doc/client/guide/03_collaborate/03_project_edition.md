# Editing a shared project

Non concurrent edition is updated every 40seconds, on each members of the team.

Concurrent edition is resolved by the principle: last edition wins.
Corner cases: 

- if there is an update of an entity while there is a deletion of this same entity, then only the update is taken into account. No deletion happens.
- the requests order is an information saved at the scenario level. This is why, re-ordering requests can provoke a concurrent edition when at the same time, another team member renames the scenario.



