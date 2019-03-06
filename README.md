FTGuard: A Priority-Aware Strategy Against the Flow Table Overflow Attack in SDN
====================================

A brief introduction
-------------------

SDN allows *dynamic* and *fine-grained* forwarding rules in the data plane. While TCAM enables fast lookups in hardware switches to achieve line-rate packet processing, the manufacturing cost and power consumption limit the capacity of flow table, which is usually insufficient in many SDN circumstances.

When the flow table is full, existing SDN switches adopt a LRU eviction mechanism to eliminate some entries to make space for new-coming flow rules. This eviction mechanism could be exploited by an attacker to commit the **flow table overflow attack**. By sending packets filled with deliberately forged header values, numerous new flow rules are tricked from the controller and frequent replacements would happen in the flow table, resulting in substantial table-misses from benign users.

FTGuard is a behavior-based priority-aware defense strategy to cope with overflow attack mentioned above. The basic idea is to commit evaluations for users in the network by their traffic feature. Benign users tend to have higher priorities while suspected users are more likely to have lower priorities. When the flow table is full, flow entries with low priority are preferred to be evicted. The architecture of FT-Guard is shown in Figure 1. Please refer to our [paper](https://zhangmenghao.github.io/papers/sigcomm2017-poster-ftguard.pdf) for more details.

![](http://i.imgur.com/oEUNAtB.png)

We implement a prototype of FTGuard based on [Floodlight v1.2](https://github.com/ZhangMenghao/FG/commit/b5be294d8484d7eab0ed840dd15c6259a3b0eaaa). A module *FTGuardManager* is created to initialize data structures and parameters used in FT-Guard. Parameters used in the evaluation criterion can be set in *src/main/resources/floodlightdefault.properties* manually.


Contact us
---------------------
Any suggestion is appreciated as FTGuard is still a research prototype. Please feel free to fork FTGuard from us on the github or send email(zhangmenghao0503@gmail.com) to us for any questions.
