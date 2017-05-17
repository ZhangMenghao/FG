FlowTable-Guard
====================================


What is FT-Guard
-------------------

Flow table of each switch is the key component of SDN. Due to the manufacturing cost and power consumption of TCAM, most commodity switches are equipped with relatively limited flow table space, which is usually insufficient in many SDN circumstances.

When the flow table is full, existing SDN switches adopt a LRU eviction mechanism to eliminate some entries to make space for new-coming flow rules. This eviction mechanism could be exploited by an attacker to commit the **flow table overflow attack**. By sending packets filled with deliberately forged header values, numerous new flow rules are tricked from the controller and frequent replacements woule happen in the flow table, resulting in more table-misses from benign users.

FT-Guard is a behavior-based priority-aware defense strategy to cope with overflow attack mentioned above, the basic idea is to commit evaluations for users in the network by their traffic feature. Benign users tend to have higher priority while suspected usrs are more likely to have lower priority. When the flow table is full, flow entries with low priority are preferred to be evicted. The architecture of FT-Guard is shown in Figure 1.

![](http://i.imgur.com/oEUNAtB.png)

We fork the [Floodlight v1.2](https://github.com/ZhangMenghao/FG/commit/b5be294d8484d7eab0ed840dd15c6259a3b0eaaa) and prototype the FT-Guard. A Floodlight Module FTGuardManager is created to initialize data structures and parameters needed in FT-Guard, parameters used for user evaluation can be manually set in src/main/resources/floodlightdefault.properties.

---------------------

Any suggestion is appreciated as FT-Guard is still a research prototype, please fell free to pull requests on the github or send us email(zhangmenghao0503@gmail.com).