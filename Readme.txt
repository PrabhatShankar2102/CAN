Folder containes multiplle java files to implement Content Addressable Network.

1)To run the bootstrap server use
	java ServerController

2)To run any client(potenitial Node)
	java PeerNodeController
	
Supported Operations
1)Join: Allows clients to join Network , either via bootstrap or anyother active node as bootstrap.
2)View: Allows user to view a specific node details, if no node is specified it displays all node
3)Insert: Insert a file in CAN.
4)Search : Search a file in the Network.
5)Leave : Leave is not implemented yet.
