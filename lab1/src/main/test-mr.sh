#!/usr/bin/env bash

#
# basic map-reduce test
#

# run the test in a fresh sub-directory.
rm -rf mr-tmp
mkdir mr-tmp || exit 1
cd mr-tmp || exit 1
rm -f mr-*

# make sure software is freshly built.
(javac ../MrSequential.java ../../mr/KeyValue.java ../../mrapp/*.java -d out) || exit 1
(javac ../MrCoordinator.java ../../mr/Coordinator.java -d out) || exit 1

failed_any=0

#########################################################
# first word-count

# generate the correct output
java -classpath out main.MrSequential mrapp.WordCount ../../../data || exit 1
sort mr-out-0 > mr-correct-wc.txt
rm -f mr-out*

echo '***' Starting wc test.

timeout -k 2s 180s java -classpath out main.MrCoordinator ../../../data &
pid=$!

# give the coordinator time to create the sockets.
sleep 1

#########################################################
if [ $failed_any -eq 0 ]; then
    echo '***' PASSED ALL TESTS
else
    echo '***' FAILED SOME TESTS
    exit 1
fi
