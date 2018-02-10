 class SkipList<T> {

    SkipListNode header;

    //max level of this skiplist
    int maxLevel;
    //count of skiplist total element
    long count;
    public int randomLevel(){
        int level =0;
        while(Math.random()<Const.ratio)
            level++;
        return level<Const.MAX_LEVEL? level:Const.MAX_LEVEL;
    }
    public void insertSkipListNode(int key ,T val){
         SkipListNode<T>[] update = (SkipListNode<T>[]) new SkipListNode[Const.MAX_LEVEL];
         SkipListNode<T> temp=header;
         //使用rank来记录每个结点前往下一个结点时越过的个数
         int[] rank = new int[maxLevel];
         //search for the previous node ,store in the update
         for(int i = maxLevel-1;i>=0;i--){
               rank[i]= i==(maxLevel-1)? 0: rank[i+1];
           while(temp.level[i].forward!=null&&
              temp.level[i].forward.key<key){
                 //更新跳过的那些间距。
                  rank[i]+=temp.level[i].span;
                  temp=temp.level[i].forward;
           }
           //update this val
           if(temp.level[i].forward!=null&&
                   temp.level[i].forward.key==key)
           {
               temp.level[i].forward.val=val;
               if(i==0) return;
           }
           //record temp to update arrays
           update[i]=temp;

         }
         // create new node
         int levelCount = randomLevel();
         SkipListNode<T> node = new SkipListNode<>(key,levelCount,val);
         //insert this node and update span

         //if levelCount bigger than this maxLevel
         if(this.maxLevel<levelCount){
             for(int i = maxLevel;i<levelCount;i++){
                 // 因为pre结点是header，所以是0
                 rank[i]=0;
                 update[i]=header;
                 update[i].level[i].span=this.count;
             }
             this.maxLevel=levelCount;
         }
         //
        for(int i = 0 ;i<levelCount;i++){
            //insert this node
            node.level[i].forward=update[i].level[i].forward;
            update[i].level[i].forward=node;
            //插入node的排位是rank[0]+1，rank[0]是node在所有结点中的排位
            //更新node的间距
            node.level[i].span=update[i].level[i].span-(rank[0]-rank[i]);
            //更新update的间距 +1是因为node也计算在内
            update[i].level[i].span=rank[0]-rank[i]+1;
        }
        //那些没有有node接触的pre结点也要加上1
        //这些结点的范围是从levelCount到maxLevel
        //如果之前this.maxLevel<levelCount，那么这个循环不会被执行。
        for(int i = levelCount;i<maxLevel;i++){
             update[i].level[i].span+=1;
        }
        //元素加1
        this.count++;

     }

    public boolean deleteNode(int key){
           SkipListNode<T>[]  update = new SkipListNode[Const.MAX_LEVEL];
           int[] rank = new int[Const.MAX_LEVEL];
           SkipListNode<T> temp=header;
           // record pre node
           for(int i = maxLevel-1 ;i>=0;i--){
               rank[i]= i==(maxLevel-1)? 0: rank[i+1];
               while(temp.level[i].forward!=null&&
                       temp.level[i].forward.key<key){
                   rank[i]+=temp.level[i].span;
                   temp=temp.level[i].forward;

               }
               update[i]=temp;
           }
           //now temp is in level[0] and temp.level[0].forward.key is greater or equal to key
           //change temp to the position
           temp=temp.level[0].forward;
           //check equality
           if(temp!=null&&temp.key==key){
               //now delete this node
               //and change it's span
               for(int i = maxLevel-1;i>=0;i--){
                     if(update[i].level[i].forward==temp){
                         update[i].level[i].span+=temp.level[i].span-1;
                         update[i].level[i].forward=temp.level[i].forward;
                     }else{
                         update[i].level[i].span-=1;
                     }
               }
               this.count--;
               //检查是否需要减小level,条件是header上对应的maxLevel层的forward指向了null
               if(this.maxLevel>=1&&header.level[maxLevel-1].forward==null)
                   this.maxLevel--;
               return true;
           }
           // miss this key
           else
               return false;


     }
     public boolean findNode(int key){
         SkipListNode<T> temp=header;
         for(int i = maxLevel ;i>=0;i--){
             while(temp.level[i].forward!=null&&
                     temp.level[i].forward.key<key){
                 temp=temp.level[i].forward;
             }
             if(temp.level[i].forward!=null&&
                temp.level[i].forward.key==key)
                 return true;
         }
         return false;
     }

}
