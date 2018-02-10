 class SkipListNode <T>{
    int key ;
    SkipListLevel[] level ;
    T val ;
    public SkipListNode(int key, int level, T val) {
        this.key = key;
        this.level = new SkipListLevel[level];
        this.val = val;
    }
}
