import os
import time

directory = os.fsencode("C:\\Users\\iason\\Desktop\\Tik-Tok\\")

count_sub =0;
count_search = 0;
em1 = "emulator-5554";
em2 = "emulator-5556";
em3 = "emulator-5558"
m = [em1,em2,em3];

while(True):
    count = 0
    for i in m:
        count = count + 1
        time.sleep(1)
        os.system("adb.exe -s "+i+" pull /storage/self/primary/DCIM/Camera/ C:\\Users\\iason\\Documents\\AndroidStudio\\DeviceExplorer\\emulator-5554\\storage\\self\\primary\\DCIM")
        os.system("adb.exe -s "+i+" pull /storage/self/primary/Download/ C:\\Users\\iason\\Documents\\AndroidStudio\\DeviceExplorer\\emulator-5554\\storage\\self\\primary")
        
        for file in os.listdir(directory):
            filename = os.fsdecode(file)
            if filename.endswith(".mp4") and (filename.find("publisher") != -1) and (filename.find("pushdone") == -1):
                count_sub = count_sub + 1
                time.sleep(1)
                os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_sub)+filename)
                os.system("adb.exe -s "+i+" push C:\\Users\\iason\\Desktop\\Tik-Tok\\"+str(count_sub)+filename+" /storage/self/primary/Movies/")
                if(count == len(m)):
                    os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_sub)+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\pushdone'+str(count_sub)+filename)
            #time.sleep(1)
            if filename.endswith(".mp4") and (filename.find("source") != -1) and (filename.find("pushdone") == -1) and (filename.find("publisher") == -1):
                count_search = count_search + 1
                os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_search)+filename)
                #time.sleep(1)
                os.system("adb.exe -s "+i+" push C:\\Users\\iason\\Desktop\\Tik-Tok\\"+str(count_search)+filename+" /storage/self/primary/search/")
                if(count == len(m)):
                    os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_search)+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\pushdone'+str(count_search)+filename)
            #time.sleep(1)
            
            
    