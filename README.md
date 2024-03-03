# My-Box (药管家APP)
## 一、介绍

&ensp;&ensp;本作品利用了可拼接技术、人脸识别算法及指纹识别解决了多人使用药盒出现误食的问题以及老人吃药“难”的现状。本作品研发的多功能可拼接智能药盒，是由主控药盒、分控药盒、MYBOX APP、云平台四部分组成。家属可以在MYBOX（药管家） APP上设定吃药时间、吃药数量、修改提醒药品种类的种类及信息等，主控系统接收APP发来的指令，根据接收到的指令来完成相关设置，例如定时、显示服药信息。未到提示吃药时则处于待机状态。到点吃药时语言会提示，语音提示吃药注意事项，吃药指示灯亮红灯，吃药后亮绿灯，LCD液晶显示屏显示吃药数量和吃药注意事项。老人取药时将进行人脸、指纹识别，人脸、指纹识别成功后，药盒会弹出从相应的药物，吃完药后，药盒回自动收回药物，药盒回到待机状态。

&ensp;&ensp;同时药盒也有对应的温控系统对药物提供一个合适的存储环境。APP也会显示老人的服药记录，此外APP也具有提醒买药功能，可以获取药盒的数量，当数量不足时会提醒用户买药，自动搜索附近药店，让用户及时补充短缺药品，结合大数据分析算法与老人的电子病历结联系在一起进行药理分析，根据老人的用药时间长短和用药情况进行药理分析，适时提醒老人复诊检查是否需要换药。

## 二、组成
- 可拼接药盒
  
  &ensp;可拼接药盒具有人脸及指纹识别、语音提示、信息显示、药物检测等功能。通常该药盒由多个拼接在一起使用，以满足多用户、单用户多药类等使用场景的需求，亦可单个药盒使用，方便携带。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/9.JPG" alt="your-image-alt-text">
  </p>

- MyBox APP

  &ensp;家属可以在MYBOX APP查看服药记录，及时了解老人是否服药，若老人未服药，APP会信息通知家属，以便家属及时通知老人服药；设定提前定时提示、定时吃药，提醒老人服药；删除或者更改提醒药品的种类及服用剂量信息，以便药盒适应各种应用场景、适合药盒重复使用，存放不同的药物。我们MYBOX APP还有一个对应的数据库存储了大量药物的信息，当设定或更改药物信息时可以自动补全信息。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/1.JPG" alt="your-image-alt-text">
  </p>

## 三、APP功能
- 登录注册功能

  &ensp;&ensp;登录注册功能是每一个APP都应具备的基本功能，这里可以实用用户名或手机号，进行注册，注册完毕可以用用户名或手机号进行登录，这里采用了阿里云的服务器，我们将注册的用户信息录入到了后台数据库，可以进行用户的管理。以下是欢迎界面、注册/登录界面实现效果。
  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/2.JPG" alt="your-image-alt-text">
  </p>

- 服药提醒功能

  &ensp;&ensp;家属设定提前定时提示、定时吃药，提醒老人服药；删除或者更改提醒药品的种类及服用剂量信息，以便药盒适应各种应用场景、适合药盒重复使用，存放不同的药物。其中药物信息可以用扫描条形码来获取，利用ONENET云平台使用一个对应的数据库存储了大量药物的信息，当设定或更改药物信息时可以自动补全信息，如果服用剂量和次数不同还可以手动更改，这大大提高了更改药物信息的效率。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/3.JPG" alt="your-image-alt-text">
  </p>

- 服药记录功能
  &ensp;&ensp;服药记录功能根据药盒的打开收回状态来判断老人是否服药，其中利用药盒可以检测药物的数量、种类及服药时间进行服药的统计及可视化界面的设计，后面药理分析的功能提供数据基础。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/4.JPG" alt="your-image-alt-text">
  </p>

- 药理分析功能
  &ensp;&ensp;药理分析的功能更据用户使用药盒药物种类、次数、时间等信息进行一个星期数据的累计，每周更新一次进行分析。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/5.JPG" alt="your-image-alt-text">
  </p>

- 周边药店功能
  &ensp;&ensp; APP会根据药盒的数量进行预判，当设置服药的数量不符合时会进行买药的提醒，同时打开附近药店功能可以帮助用户寻找离自身位置最近的药店，此功能利用百度API，同时对地图里面额信息进行遍历，筛选出附近最近的药店便进行标记。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/6.JPG" alt="your-image-alt-text">
  </p>

- 语音设置功能
  &ensp;&ensp;语音设置功能是根据老人情感特点进行设计的，如子女不在老人身边会让老人产生一种孤独感，面对冷冰冰的机器提醒语音，必定没有自己子女的声音进行提醒来得温暖。此功能子女可以在远程录好提醒音并发送到药盒上，这里采用WAV的音频格式，药盒会自动根据APP发送的提醒音频进行提醒音的更换设置。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/7.JPG" alt="your-image-alt-text">
  </p>

- 提醒买药功能
  &ensp;&ensp;APP会根据药盒的数量进行预判，当时显示药盒药物的存储量，当设置服药的数量不符合时会进行买药的提醒。

  <p align="center">
      <img src="https://github.com/chensimian/My-Box/blob/main/result/8.JPG" alt="your-image-alt-text">
  </p>
