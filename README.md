# Tik-Tok

Starting 27/4 : making the base skeleton of the project

## links:
https://www.baeldung.com/a-guide-to-java-sockets

## next steps :
1. save every file in txt (100.000 chunks) and then open read and connect them so 
we can send files through the home network and not only the same computer
2. Make other methods and see how we can do that with the Consumer objects
in the List and not randomly
3. Make publisher exactly like broker                                        (registeredUsers me disconnect-PUSH-PULL and send video send to subs klp klp)
4. Make hashtags and values, subscribe --> sto kanali
5. Stop threads ? Consumer continues to run and doesn't stop
6. make interfaces with function definitions
7. o publisher na mh dexetai ta video pou exei anebasei.
8. ftiaxnoume kai to subscribe gia na elegxei kai th lista me tous publishers pou einai kai consumers.
9. save in broker and then remove the file
10. making publisher every video, we dont have download in publisher and we dont have download on command- just random at start of consumer
11. 2nd and 3rd publisher etc give broker correct video but he doesnt write it correctly on consumers
12. we should make it send it only to subs in future
13. disconnect --> remove from subs ? maybe
14. check publisher that he cannot sub to himself

## broker hashing - even workload
* hash(port+ip) : ip dottes replaced with ""
* [ ta parnei ola ] --> 1 broker
* [ brokers.get(0).port + IP < ] An to hash tou channel Name einai mikrotero paei ston 1--> 2 brokers
* [ mirkotero tou prwtou-ston prwto < anamesa-ston deytero < edw trito ]
* omoiws 
## ^ done


## pull
tha vriskei ayth se poion broker that eprepe na einai to hashtag h to channel name kai tha kanei
broker.getChannel().getAllVideos()


> 1106