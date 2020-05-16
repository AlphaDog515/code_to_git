package com.demo15.avl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyTreeMap<K,V> {

    private static final boolean BLACK = true;
    private static final boolean RED = false;

    private Entry<K,V> root;
    private int size = 0;
    private final Comparator<K> comparator;
    MyTreeMap(){
        comparator =null;
    }

    public MyTreeMap(Comparator comparator){
        this.comparator = comparator;
    }

    public V put(K key,V value){
        if (root == null){
            root = new Entry<>(key,value,null);
            size++;
            return null;
        }else {
            int ret = 0;
            Entry<K,V> p = null;
            Entry<K,V> current = root;
            if (comparator == null){
                if (key == null) throw  new NullPointerException("key = null");
                Comparable<K> k = (Comparable<K>) key;
                while (current != null){
                    p =current;
                    ret = k.compareTo(current.key);
                    if (ret < 0)
                        current = current.left;
                    else if(ret > 0)
                        current = current.right;
                    else {
                        current.value = value;
                        return current.value;
                    }
                }
            }else {
                do {
                    p = current;
                    ret = comparator.compare(key,current.key);
                    if (ret < 0)
                        current = current.left;
                    else if (ret > 0)
                        current = current.right;
                    else {
                        current.value = value;
                        return value;
                    }
                }while (current != null);
            }
            Entry<K,V> e = new Entry<>(key,value,p);
            if (ret < 0)
                p.left = e;
            else
                p.right = e;
            size++;
            fixAfterInsertion(e);
            return e.value;
        }
    }

    /**
     * �����½ڵ��ƽ������
     * @param e �½ڵ�
     */
    private void fixAfterInsertion(Entry<K, V> e) {
        //���²���ڵ�����Ϊ��ɫ
        setRed(e);
        Entry<K,V> p,g,u;//���ڵ���游�ڵ������ڵ�
        Entry<K,V> current = e;//�½ڵ�
        /**
         * ����ͨ��ѭ����������ƽ��
         */
        while ((p = parentOf(current)) != null && isRed(p)){
            g = parentOf(p);//�游�ڵ�
            if (p == g.left){
                u = g.right;
                //���1������ڵ�Ϊ��ɫ
                if (u != null && isRed(u)){
                    setBlack(p);//���ڵ���Ϊ��ɫ
                    setBlack(u);//����ڵ���Ϊ��ɫ
                    setRed(g);//�游�ڵ���Ϊ��ɫ
                    current = g;//���游�ڵ���Ϊ��ǰ�ڵ�
                    //��������ƽ��
                    continue;
                }
                //���2����ǰ�ڵ�Ϊ�ҽڵ㣬����ڵ�Ϊ��ɫ
                if (current == p.right){
                    leftRotate(p);//���ڵ�Ϊ֧������
                    Entry<K,V> tmp = p;
                    p = current;//���ڵ�͵�ǰ�ڵ㻥��
                    current = tmp;//���ڵ���Ϊ��ǰ�ڵ�
                }
                //���3����ǰ�ڵ�Ϊ��ڵ㣬����ڵ�Ϊ��ɫ
                setBlack(p);//���ڵ���Ϊ��ɫ
                setRed(g);//�游�ڵ���Ϊ��ɫ
                rightRotate(g);//�游�ڵ�Ϊ֧������
            }else {//�෴�Ĳ���
                u = g.left;
                if (u != null && isRed(u)){
                    setBlack(p);
                    setBlack(u);
                    setRed(g);
                    current = g;
                    continue;
                }
                if (current == p.left){
                    rightRotate(p);
                    Entry<K,V> tmp = p;
                    p = current;
                    current = tmp;
                }
                setBlack(p);
                setRed(g);
                leftRotate(g);
            }
        }
        //��󽫸��ڵ�����Ϊ��ɫ
        setBlack(root);
    }

    public boolean containsKey(Object key){
        return getEntry(key) != null;
    }

    public Set<Entry<K,V>> entrySet(){
        Set<Entry<K,V>> list = new HashSet<>(size + 4);
        entries(root,list);
        return list;
    }

    private void entries(Entry<K,V> e,Set<Entry<K,V>> list){
        if (e != null){
            entries(e.left,list);
            list.add(e);
            entries(e.right,list);
        }
    }

    public boolean containsValue(V v){
        return values().contains(v);
    }

    public V get(Object key){
        Entry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    private void setColor(Entry<K,V> e,boolean color){
        if (e != null) e.color = color;
    }

    private void setRed(Entry<K,V> e){
        setColor(e,RED);
    }

    private void setBlack(Entry<K,V> e){
        setColor(e,BLACK);
    }

    private void setParent(Entry<K,V> e,Entry<K,V> p){
        if (e != null) e.parent = p;
    }

    private boolean isBlack(Entry<K,V> e){
        return colorOf(e) == BLACK;
    }

    private boolean isRed(Entry<K,V> e){
        return !isBlack(e);
    }

    private Entry<K,V> parentOf(Entry<K,V> e){
        return e == null ? null : e.parent;
    }

    private boolean colorOf(Entry<K,V> e){
        return e == null ? BLACK : e.color;
    }

    /**
     * ����
     * @param e ��ת֧��
     */
    private void rightRotate(Entry<K,V> e){
        //ԭ֧�����ڵ�
        Entry<K,V> left = e.left;
        //ԭ֧�����ڵ���ҽڵ�
        Entry<K,V> leftOfRight = left.right;
        //�¾�֧����滻
        left.parent = e.parent;
        if (e.parent == null){//֧��ĸ��ڵ�Ϊ���ڵ�����
            root = left;
        }else {//�Ǹ��ڵ�
            if (e == e.parent.left)
                e.parent.left = left;
            else
                e.parent.right = left;
        }
        //��ԭ֧���Ϊ��֧����ҽڵ�
        left.right = e;
        e.parent = left;
        //����֧��δ��תǰ���ҽڵ��Ϊת�����ԭ֧�����ڵ�
        e.left = leftOfRight;
        if (leftOfRight != null)
            leftOfRight.parent = e;
    }

    /**
     * ����
     * @param e ֧��
     */
    private void leftRotate(Entry<K,V> e){
        //֧������ӽڵ�
        Entry<K,V> right = e.right;
        //֧�����ӽڵ�����ӽڵ�
        Entry<K,V> rightOfLeft = right.left;
        //�¾�֧����滻
        right.parent = e.parent;
        if (e.parent == null){
            root = right;
        }else {
            if (e == e.parent.left)
                e.parent.left = right;
            else
                e.parent.right = right;
        }
        //��ԭ֧���Ϊ��֧�����ڵ�
        right.left = e;
        e.parent = right;
        //����֧�����ڵ��Ϊ��֧����ҽڵ�
        e.right = rightOfLeft;
        if (rightOfLeft != null)
            rightOfLeft.parent = e;
    }

    public int getDeep(){
        return deep(root);
    }

    private int deep(Entry<K,V> e){
        int deep = 0;
        if (e != null){
            int leftDeep = deep(e.left);
            int rightDeep = deep(e.right);
            deep = leftDeep > rightDeep ? leftDeep + 1 : rightDeep + 1;
        }
        return deep;
    }

    public V remove(Object key){
        if (key == null) return null;
        Entry<K,V> delEntry;
        delEntry = getEntry(key);
        if (delEntry == null) return null;
        size--;
        Entry<K,V> p = delEntry.parent;
        if (delEntry.right == null && delEntry.left == null){
            if (p == null){
                root = null;
            }else {
                if (p.left == delEntry){
                    p.left = null;
                }else {
                    p.right = null;
                }
            }
        }else if (delEntry.right == null){//ֻ����ڵ�
            Entry<K,V> lc = delEntry.left;
            if (p == null) {
                lc.parent = null;
                root = lc;
            } else {
                if (delEntry == p.left){
                    p.left = lc;
                }else {
                    p.right = lc;
                }
                lc.parent = p;
            }
        }else if (delEntry.left == null){//ֻ���ҽڵ�
                Entry<K,V> rc = delEntry.right;
            if (p == null) {
                rc.parent = null;
                root = rc;
            }else {
                if (delEntry == p.left)
                    p.left = rc;
                else
                    p.right = rc;
                rc.parent = p;
            }
        }else {//�������ڵ�,�ҵ���̽ڵ㣬��ֵ����ɾ���ڵ㣬Ȼ�󽫺�̽ڵ�ɾ��������
            Entry<K,V> successor = successor(delEntry);//��ȡ����̽ڵ�
            boolean color = successor.color;
            V old = delEntry.value;
            delEntry.value = successor.value;
            delEntry.key = successor.key;
            if (delEntry.right == successor){//��̽ڵ�Ϊ���ӽڵ㣬
                if (successor.right != null) {//���ӽڵ������ӽڵ�
                    delEntry.right = successor.right;
                    successor.right.parent = delEntry;
                }else {//���ӽڵ�û���ӽڵ�
                    delEntry.right = null;
                }
            }else {
                successor.parent.left = null;
            }
            if (color == BLACK)
                //fixUpAfterRemove(child,parent);
            return old;
        }
        V old = delEntry.value;
        if (delEntry.color == BLACK)//ɾ��Ϊ��ɫʱ����Ҫ����ƽ����
            if (delEntry.right != null)//ɾ���ڵ���ӽڵ�ֻ���ҽڵ�
                fixUpAfterRemove(delEntry.right,delEntry.parent);
            else if (delEntry.left != null)//ɾ���ڵ�ֻ����ڵ�
                fixUpAfterRemove(delEntry.left,delEntry.parent);
            else
                fixUpAfterRemove(null,delEntry.parent);
        delEntry.parent = null;
        delEntry.left = null;
        delEntry.right = null;
        return old;
    }

    private Entry<K, V> getEntry(Object key) {
        if (key == null) return null;
        Entry<K, V> delEntry = null;
        Entry<K, V> current = root;
        int ret;
        if (comparator == null){
            Comparable<K> k = (Comparable<K>) key;
            while (current != null){
                ret = k.compareTo(current.key);
                if (ret < 0)
                    current = current.left;
                else if (ret > 0)
                    current = current.right;
                else{
                    delEntry = current;
                    break;
                }
            }
        }else {
            for (;current != null;){
                ret = comparator.compare(current.key, (K) key);
                if (ret < 0)
                    current = current.left;
                else if (ret > 0)
                    current = current.right;
                else{
                    delEntry = current;
                    break;
                }
            }
        }
        return delEntry;
    }

    //node��ʾ�������Ľڵ㣬����̽ڵ���ӽڵ㣨��Ϊ��̽ڵ㱻Ų��ɾ���ڵ��λ��ȥ�ˣ�
    private void fixUpAfterRemove(Entry<K, V> node,Entry<K,V> parent) {
        Entry<K,V> other;
        while((node == null || isBlack(node)) && (node != root)) {
            if(parent.left == node) { //node�����ӽڵ㣬����else������ĸպ��෴
                other = parent.right; //node���ֵܽڵ�
                if(isRed(other)) { //case1: node���ֵܽڵ�other�Ǻ�ɫ��
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                //case2: node���ֵܽڵ�other�Ǻ�ɫ�ģ���other�������ӽڵ�Ҳ���Ǻ�ɫ��
                if((other.left == null || isBlack(other.left)) &&
                        (other.right == null || isBlack(other.right))) {
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    //case3: node���ֵܽڵ�other�Ǻ�ɫ�ģ���other�����ӽڵ��Ǻ�ɫ�����ӽڵ��Ǻ�ɫ
                    if(other.right == null || isBlack(other.right)) {
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }

                    //case4: node���ֵܽڵ�other�Ǻ�ɫ�ģ���other�����ӽڵ��Ǻ�ɫ�����ӽڵ�������ɫ
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else { //������ĶԳ�
                other = parent.left;

                if (isRed(other)) {
                    // Case 1: node���ֵ�other�Ǻ�ɫ��
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: node���ֵ�other�Ǻ�ɫ����other�������ӽڵ㶼�Ǻ�ɫ��
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.left==null || isBlack(other.left)) {
                        // Case 3: node���ֵ�other�Ǻ�ɫ�ģ�����other�����ӽڵ��Ǻ�ɫ�����ӽڵ�Ϊ��ɫ��
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }

                    // Case 4: node���ֵ�other�Ǻ�ɫ�ģ�����other�����ӽڵ��Ǻ�ɫ�ģ����ӽڵ�������ɫ
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }
        if (node!=null)
            setBlack(node);
    }

    private Entry<K, V> successor(Entry<K, V> delEntry) {
        Entry<K,V> r = delEntry.right;//assert r != null;
        while (r.left != null){
            r = r.left;
        }
        return r;
    }

    List<V> values(){
        List<V> set = new ArrayList<>(size+4);
        midIterator(root,set);
        return set;
    }

    private void midIterator(Entry<K,V> e, List<V> values){
        if (e != null){
            midIterator(e.left,values);
            values.add(e.value);
            midIterator(e.right,values);
        }
    }

    public void clear(){
        clear(root);
        root = null;
    }

    private void clear(Entry<K,V> node) {
        if (node != null){
            clear(node.left);
            node.left = null;
            clear(node.right);
            node.right = null;
        }
    }

    public int size(){return size;}

    static final class Entry<K,V>{
        private K key;
        private V value;
        private Entry<K,V> left;
        private Entry<K,V> right;
        private Entry<K,V> parent;
        private boolean color = BLACK;
        Entry(K key,V value,Entry<K,V> parent){
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}