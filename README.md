You'll need to run the server (https://github.com/nashjain/java-legacy-chat-server) locally and then update the code with your local IP address.

How to run server:
===
* download and double click on target/chat-server.jar
or
* java -jar target/chat-server.jar

API example:
==
* http://localhost:4567/send?message=Yo_this_is_a_message&senderName=naresh
* http://localhost:4567/fetchAllMessages

#Defect by Javed
1. Application was crashing and was not even launching due to the messages size issue.
Fix: Handled the size of object messages  before setting to the Adaptor.
2. Application was crashing due to url connection issue and "Could not fetch messages" toast message was keep on coming.
Fix: Changed localhost to 10.0.2.2 for emulator and to local system Ip for actual device.
3. Message was not visible on the screen.
Fix: Set the text color to Black in ItemList.xml