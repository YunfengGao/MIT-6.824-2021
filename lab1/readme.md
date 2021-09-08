

原文地址 https://pdos.csail.mit.edu/6.824/labs/lab-mr.html

## 工作内容：
你的工作是实现一个分布式 MapReduce，它由两个程序组成，coordinator和worker。 将只有一个coordinator进程和一个或多个并行执行的worker。 在一个真实的系统中，worker 会在一堆不同的机器上运行，但对于这个实验室，你将在一台机器上运行它们。 worker将通过RPC与coordinator通信。 每个worker进程都会向coordinator请求一项任务，从一个或多个文件中读取任务的输入，执行任务，并将任务的输出写入一个或多个文件。 coordinator应注意worker是否没有在合理的时间内完成其任务（对于本实验，使用 10 秒），并将相同的任务分配给不同的worker。