import os
import time

directory = os.fsencode("C:\\Users\\iason\\Desktop\\Tik-Tok\\")

count_sub =0;
count_search = 0;
    
while(True):
    time.sleep(1)
    os.system("adb.exe pull /storage/self/primary/DCIM/Camera/ C:\\Users\\iason\\Documents\\AndroidStudio\\DeviceExplorer\\emulator-5554\\storage\\self\\primary\\DCIM")
    os.system("adb.exe pull /storage/self/primary/Download/ C:\\Users\\iason\\Documents\\AndroidStudio\\DeviceExplorer\\emulator-5554\\storage\\self\\primary")
    
    #String temp = "adb.exe pull "+new_path+" C:\\Users\\iason\\Desktop\\personal"; 
    #String temp = adb.exe push C:\\Users\\iason\\Desktop\\personal\\video1.mp4 /storage/self/primary/DCIM/Camera/
    for file in os.listdir(directory):
        filename = os.fsdecode(file)
        if filename.endswith(".mp4") and (filename.find("publisher") != -1) and (filename.find("pushdone") == -1):
            count_sub = count_sub + 1
            os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_sub)+filename)
            os.system("adb.exe push C:\\Users\\iason\\Desktop\\Tik-Tok\\"+str(count_sub)+filename+" /storage/self/primary/Movies/")
            os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_sub)+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\pushdone'+str(count_sub)+filename)
        
        if filename.endswith(".mp4") and (filename.find("source") != -1) and (filename.find("pushdone") == -1) and (filename.find("publisher") == -1):
            count_search = count_search + 1
            os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_search)+filename)
            os.system("adb.exe push C:\\Users\\iason\\Desktop\\Tik-Tok\\"+str(count_search)+filename+" /storage/self/primary/search/")
            os.rename(r'C:\\Users\\iason\\Desktop\\Tik-Tok\\'+str(count_search)+filename,r'C:\\Users\\iason\\Desktop\\Tik-Tok\\pushdone'+str(count_search)+filename)
        
            
            
    