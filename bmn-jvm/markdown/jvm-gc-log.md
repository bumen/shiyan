### GC日志

 * jmap -histo:live pid
 ``` 
    // 启动检查执行Full GC
    2020-03-05T03:00:03.074+0800: 1542.913: [Full GC (Heap Inspection Initiated GC) 2020-03-05T03:00:03.074+0800: 1542.913: [CMS: 972606K->435004K(11237376K), 1.2117560 secs] 1711398K->435004K(12883392K), [Metaspace: 98277K->982
    77K(1140736K)], 1.2149588 secs] [Times: user=1.14 sys=0.07, real=1.21 secs]
 ```
 
 * CMS
 ``` 
        2020-03-07T19:42:40.306+0800: 234500.145: [GC (CMS Initial Mark) [1 CMS-initial-mark: 9005021K(11237376K)] 9139575K(12883392K), 0.0139099 secs] [Times: user=0.07 sys=0.01, real=0.01 secs]
        2020-03-07T19:42:40.320+0800: 234500.160: [CMS-concurrent-mark-start]
        2020-03-07T19:42:40.759+0800: 234500.598: [CMS-concurrent-mark: 0.438/0.439 secs] [Times: user=0.94 sys=0.00, real=0.43 secs]
        2020-03-07T19:42:40.759+0800: 234500.598: [CMS-concurrent-preclean-start]
        2020-03-07T19:42:40.772+0800: 234500.611: [CMS-concurrent-preclean: 0.013/0.013 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]
        2020-03-07T19:42:40.772+0800: 234500.611: [CMS-concurrent-abortable-preclean-start]
         CMS: abort preclean due to time 2020-03-07T19:42:45.880+0800: 234505.719: [CMS-concurrent-abortable-preclean: 5.007/5.108 secs] [Times: user=5.13 sys=0.00, real=5.10 secs]
        2020-03-07T19:42:45.880+0800: 234505.720: [GC (CMS Final Remark) [YG occupancy: 226708 K (1646016 K)]2020-03-07T19:42:45.880+0800: 234505.720: [Rescan (parallel) , 0.0262083 secs]2020-03-07T19:42:45.906+0800: 234505.746: [we
        ak refs processing, 0.0018244 secs]2020-03-07T19:42:45.908+0800: 234505.748: [class unloading, 0.1477829 secs]2020-03-07T19:42:46.056+0800: 234505.896: [scrub symbol table, 0.0101040 secs]2020-03-07T19:42:46.066+0800: 234505
        .906: [scrub string table, 0.0014069 secs][1 CMS-remark: 9005021K(11237376K)] 9231729K(12883392K), 0.1877774 secs] [Times: user=0.37 sys=0.00, real=0.19 secs]
        2020-03-07T19:42:46.068+0800: 234505.908: [CMS-concurrent-sweep-start]
        2020-03-07T19:43:00.681+0800: 234520.521: [CMS-concurrent-sweep: 14.608/14.613 secs] [Times: user=15.08 sys=0.00, real=14.61 secs]
        2020-03-07T19:43:00.682+0800: 234520.522: [CMS-concurrent-reset-start]
        2020-03-07T19:43:00.700+0800: 234520.540: [CMS-concurrent-reset: 0.018/0.018 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
 ```