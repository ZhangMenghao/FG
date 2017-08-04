ip0=""
ip1=""
srcMac=""
dstMac=""
if [ $1 = "h1" ]; then
    ip0="10.0.0.1"
    ip1="10.0.0.11"
    srcMac="00:00:00:00:00:01"
    dstMac="00:00:00:00:00:11"
elif [ $1 = "h2" ]; then
    ip0="10.0.0.2"
    ip1="10.0.0.12"
    srcMac="00:00:00:00:00:02"
    dstMac="00:00:00:00:00:12"
elif [ $1 = "h3" ]; then
    ip0="10.0.0.3"
    ip1="10.0.0.13"
    srcMac="00:00:00:00:00:03"
    dstMac="00:00:00:00:00:13"
else
    echo "./trans.sh [h1/h2/h3] [1/2/...]"
    exit
fi
tcpprep -a client -i in$2.pcap -o in$2.cach
tcprewrite --endpoints=$ip0:$ip1 --enet-smac=$srcMac,$dstMac --enet-dmac=$dstMac,$srcMac -i in$2.pcap -c in$2.cach -o out$2.pcap