NSQ_DIR=$1

$NSQ_DIR/nsqd --lookupd-tcp-address=127.0.0.1:4160 --snappy true --msg-timeout 5s &
$NSQ_DIR/nsqlookupd &
