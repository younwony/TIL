package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a5639_이진_검색_트리 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        Tree tree = new Tree();
        while(true){
            String input = bufferedReader.readLine();
            if(input == null | "".equals(input)) break;
            tree.insert(Integer.parseInt(input));
        }
        StringBuilder result = new StringBuilder();
        tree.postFix(tree.root, result);
        System.out.println(result.toString());


    }

    static class Tree{
        Node root = null;

        public void insert(int nodeValue){
            Node node = new Node(nodeValue);
            if(root == null){
                root = node;
            }else{
                /* 탐색용 노드 , 초기는 root*/
                Node searchNode = root;
                /* 탐색용 노드 부모 */
                Node parent;

                while(true){
                    /* 부모를 탐색용 노드로 */
                    parent = searchNode;

                    /* 입력된 노드가 부모 노드보다 작을 경우*/
                    if(nodeValue < parent.node){
                        /* 탐색 노드를 부모 노드의 좌측 노드로 삽입 */
                        searchNode = parent.leftNode;
                        /* 삽입 된 노드가 비어 있을 경우 부모의 좌측 노드에 입력된 노드 삽입 */
                        if(searchNode == null){
                            parent.leftNode = node;
                            return;
                        }
                    }

                    /* 입력된 노드가 부모 노드보다 클 경우, 현재는 같을 경우를 제외 했다 */
                    if(nodeValue > parent.node){
                        /* 탐색 노드를 부모 노드의 우측 노드로 삽입 */
                        searchNode = parent.rightNode;
                        /* 삽입 된 노드가 비어 있을 경우 부모의 우측 노드에 입력된 노드 삽입 */
                        if(searchNode == null){
                            parent.rightNode = node;
                            return;
                        }
                    }
                }
            }
        }

        public void postFix(Node node, StringBuilder result){
            if(node != null) {
                if (node.leftNode != null) postFix(node.leftNode, result);
                if (node.rightNode != null) postFix(node.rightNode, result);
                result.append(node.node).append("\n");
            }
        }
    }

    static class Node{
        int node;
        Node leftNode;
        Node rightNode;

        public Node(int node) {
            this.node = node;
            this.leftNode = null;
            this.rightNode = null;
        }
    }
}
