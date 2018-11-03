# WeChatMoment
1.使用Android原生方式实现

2.朋友圈中的avatar和image下载的是http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg,如果需要修改，按以下方式：

   a.com.brain.neuron.widget.MomentsItemView.java  line:115-116改为：
   
      ImageLoader.with(getContext()).load(avatar,ivAvatar);
      
   b.com.brain.neuron.widget.MomentsItemView.java  line:179-180改为：
   
      ImageLoader.with(getContext()).load(url.getUrl(),ivSingle);
      
   c.com.brain.neuron.widget.MomentsItemView.java  line:218-219改为：
   
      ImageLoader.with(getContext()).load(url,ivSingle);

3.朋友圈用户的头像和背景并未实现

4.想用Flutter来实现，所以有些功能并未完全实现。
