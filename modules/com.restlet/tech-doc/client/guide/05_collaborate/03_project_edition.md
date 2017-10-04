A shared project can be edited at the same time by multiple team members. Two situations can happen: 

- Non concurrent editions. In this case, they are updated **every 40 seconds** for each team members.
- Concurrent editions. They are resolved by the **last-in-wins** strategy (the last writer wins). 

> __Note:__ The requests order is an information saved at the scenario level. 
This is why, when re-ordering requests of a scenario A, a concurrent edition happens when at the same time, 
another team member renames the scenario A into B.

> __Note:__ If there is an update of an entity while there is a deletion done on this very same entity by another team member, 
only the update is taken into account. No deletion happens.

