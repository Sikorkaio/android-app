Not finding any peers
---------------------
Usually there are not enough light server on Ropsten, so there is a really high chance that 
the application will not be able to find any peers at all.

There is a peer list provided in the [assets](../app/src/main/assets/ropsten_peers.json) but it
might not be up to date. If you have an up to date peer list you could update it before deploying.

You should keep in mind that the peer list will be copied only the first time the application 
is installed and then it will not be updated, unless you delete the file from the device's storage.

You can find the copied file in your device's internal storage.
The full path is **/sdcard/.ropsten/GethDroid/static-nodes.json**.

You can find information about static-nodes [here](https://github.com/ethereum/go-ethereum/wiki/Connecting-to-the-network#static-nodes).

After the first deployment if you want to change the static-nodes.json file you can either 
delete it with your device's FileManager application modify the [peers](../app/src/main/assets/ropsten_peers.json)
and redeploy the application in order to have the new peers copied,
 
or

You can directly edit the static-nodes.json file.

Depending on the ROM your device is running you might be able to do the following 
using the terminal/console.
 
```bash
adb shell 
vim /sdcard/.ropsten/GethDroid/static-nodes.json

```

Or you can pull the static-nodes.json locally to your machine, modify it and then push it again
to your mobile device.

```bash
adb pull /sdcard/.ropsten/GethDroid/static-nodes.json 
vim static-nodes.json
adb push static-nodes.json /sdcard/.ropsten/GethDroid/static-nodes.json
```

The easier way to have a light server is run your own. To do so you have to start geth from the cli
with with [**--lightserv**](https://github.com/ethereum/go-ethereum/wiki/Command-Line-Options) flag.

After starting geth find the log line showing your node id.

```
self=enode://NodeId@[::]:30303
```  

Replace the \[::\] part with the IP address of the machine running the node, and update the 
static-nodes.json with your node.
 
You should keep in mind that while your light server is in fast sync, the light client will 
not start syncing. The sync will start after the fast sync catches up.



