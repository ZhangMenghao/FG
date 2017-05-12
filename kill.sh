pid=`ps ax | grep flood | grep java | awk '{print $1}'`
if [ -z "$pid" ]; then
    echo "no floodlight running"
else
    echo "kill $pid"
    kill -9 $pid
fi