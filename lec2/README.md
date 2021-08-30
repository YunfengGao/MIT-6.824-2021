# LEC 2: RPC and Threads

## vote
1. VoteCount1: 非线程安全        
2. VoteCount2: 互斥锁      
3. VoteCount3: 互斥锁 + sleep 减少忙等     
4. VoteCount4: 互斥锁 + condition 减少忙等     
5. VoteCount5: 阻塞队列部分消费，可能发生vote线程阻塞导致JVM不退出    
6. VoteCount6: 阻塞队列全部消费     