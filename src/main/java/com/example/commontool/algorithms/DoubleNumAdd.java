package com.example.commontool.algorithms;

/**
 * @ClassName DoubleNumAdd
 * @Description 两数相加
 * @Author tianhuan
 * @Date 2022/6/17
 **/
public class DoubleNumAdd {
    public static void main(String[] args) {
        ListNode l1 = generateNode(1,2,4);
        ListNode l2 = generateNode(1,3,4);

        printNode(l1);
        printNode(l2);
        ListNode listNode = mergeTwoLists(l1, l2);
        printNode(listNode);


    }

    /**
     * 移除有序链表中重复的元素
     *
     * @param head:
     * @return com.example.commontool.algorithms.DoubleNumAdd.ListNode
     * @Author tianhuan
     * @Date 2022/6/23
     **/
    public static ListNode deleteDuplicates(ListNode head) {

        if (head==null||head.next==null) {
            return head;
        }
        // 如果重复，则当前节点舍弃
        if (head.val==head.next.val) {
            head=deleteDuplicates(head.next);
        }else {
            //不重复，则进行下一个节点的重复移除
            head.next=deleteDuplicates(head.next);
        }

        return head;
    }

    private static void printNode(ListNode listNode) {
        while (listNode != null) {
            System.out.print(listNode.val);
            listNode = listNode.next;
        }
        System.out.println("");
    }


    public static ListNode generateNode(int... nodes) {
        ListNode initNode = new ListNode(-1);
        ListNode curNode = initNode;
        for (int node : nodes) {
            curNode.next = new ListNode(node);
            curNode = curNode.next;
        }
        return initNode.next;
    }

    //Definition for singly-linked list.
    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
     *
     * @param list1:
     * @param list2:
     * @return com.example.commontool.algorithms.DoubleNumAdd.ListNode
     * @Author tianhuan
     * @Date 2022/6/21
     **/
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode result = new ListNode(-1);
        ListNode cur = result;
        while (list1 != null || list2 != null) {
            int x = (list1 == null) ? -999 : list1.val;
            int y = (list2 == null) ? -999 : list2.val;
            if (x > y && y != -999) {
                cur.next = new ListNode(y);
                list2 = list2.next;
            } else {
                if (x != -999) {
                    cur.next = new ListNode(x);
                    list1 = list1.next;
                }else if(y != -999) {
                    cur.next = new ListNode(y);
                    list2 = list2.next;
                }else {
                    break;
                }
            }
            cur = cur.next;
        }
        return result.next;
    }
    public static ListNode mergeTwoLists2(ListNode list1, ListNode list2) {
        ListNode result = new ListNode(-1);
        ListNode cur = result;
        while (list1 != null && list2 != null) {
            if (list1==null) {
                list1=list2;
            }else if (list2==null){
                list2=list1;
            }
            int x =  list1.val;
            int y =  list2.val;
            if (x > y ) {
                cur.next = new ListNode(y);
                list2 = list2.next;
            } else {
                    cur.next = new ListNode(x);
                    list1 = list1.next;
            }
            cur = cur.next;
        }
        cur.next=list1==null?list2:list1;
        return result.next;
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int carry = 0;
        boolean l1hasNext = l1 != null;
        boolean l2hasNext = l2 != null;
        ListNode dummyNode = new ListNode(-1);
        ListNode curNode = dummyNode;
        while (l1hasNext || l2hasNext) {
            int x = (l1 == null) ? 0 : l1.val;
            int y = (l2 == null) ? 0 : l2.val;
            int sum = x + y + carry;
            carry = sum / 10;
            sum = sum % 10;
            curNode.next = new ListNode(sum);
            curNode = curNode.next;
            //两个工作指针后移
            if (l1 != null) {
                l1 = l1.next;
            } else {
                l1 = null;
            }
            if (l2 != null) {
                l2 = l2.next;
            } else {
                l2 = null;
            }
            l1hasNext = l1 != null;
            l2hasNext = l2 != null;
        }
        // 最后一次进位位1需要补一个
        if (carry == 1) {
            curNode.next = new ListNode(1);
            curNode = curNode.next;
        }
        return dummyNode.next;
    }

}
