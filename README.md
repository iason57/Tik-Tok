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

## broker hashing - even workload
* hash(port+ip) : ip dottes replaced with ""
* [ ta parnei ola ] --> 1 broker
* [ brokers.get(0).port + IP < ] An to hash tou channel Name einai mikrotero paei ston 1--> 2 brokers
* [ mirkotero tou prwtou-ston prwto < anamesa-ston deytero < edw trito ]
* omoiws 
## ^ done
 
> 674,675 broker kai 308,309 Consumer