/**
 * 
 */
package com.flatironschool.javacs;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
/**
 * Implementation of a Map using a binary search tree.
 * 
 * @param <K>
 * @param <V>
 *
 */
public class MyTreeMap<K, V> implements Map<K, V> {

	private int size = 0;
	private Node root = null;

	/**
	 * Represents a node in the tree.
	 *
	 */
	protected class Node {
		public K key;
		public V value;
		public Node left = null;
		public Node right = null;
		
		/**
		 * @param key
		 * @param value
		 * @param left
		 * @param right
		 */
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
		
	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public boolean containsKey(Object target) {
		return findNode(target) != null;
	}

	/**
	 * Returns the entry that contains the target key, or null if there is none. 
	 * 
	 * @param target
	 */
	private Node findNode(Object target) {
		// some implementations can handle null as a key, but not this one
		if (target == null) {
            throw new NullPointerException();
	    }
		
		// something to make the compiler happy
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) target;
		Node currentNode = root;
		while(currentNode != null){
			// System.out.println("the current node is " + currentNode.key + 
			// 	"the node to find is " + k);
			if (k.compareTo(currentNode.key) == 0) {
				// System.out.println("WE FOUND THE NODE!");
				return currentNode;
			}

			else if(k.compareTo(currentNode.key) < 0) {
				currentNode = currentNode.left;
				// System.out.println("moved to the left");
			}

			else {
				currentNode = currentNode.right;
				// System.out.println("moved to the right");
			}

		}

		return null;
	}

	/**
	 * Compares two keys or two values, handling null correctly.
	 * 
	 * @param target
	 * @param obj
	 * @return
	 */
	private boolean equals(Object target, Object obj) {
		if (target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}

	@Override
	public boolean containsValue(Object target) {
		Node currentNode = root;
		Set<V> values = new LinkedHashSet<V>();
		values = containsValueHelper(root, values);
		//System.out.println(values);
		return (values.contains(target));
	


	}

	/**
	 * recursive helper method
	 * visits every node in the tree (in order) and accumulates the nodes' keys in 
	 * a set
	 * @param Node (root) and empty set
	 * @return arrayList Obj containing all of the trees' nodes' values
	 */

	private Set<V> containsValueHelper (Node node, Set<V> result){

		if (node.left != null){
		containsValueHelper(node.left, result);
		}	

		result.add(node.value);

		

		if (node.right != null){
		containsValueHelper(node.right, result);
		}

		return result;

	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		Node node = findNode(key);
		if (node == null) {
			return null;
		}
		return node.value;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<K>();
        set = keySetHelper(root, set);
		return set;
	}

	/* resursive helper 
	 * same logic as with the contains value helper 
	 */

	private Set<K> keySetHelper (Node node, Set<K> result){
		// inOrder traversals: left, visit, right

		if (node.left != null){
		keySetHelper(node.left, result);
		}	

		result.add(node.key);

		

		if (node.right != null){
		keySetHelper(node.right, result);
		}

		return result;

	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		else if (root == null) {
			root = new Node(key, value);
			size++;
			return null;
		}

		return putHelper(root, key, value);
	}

	/* 
	 * recursive helper 
	 * if a node with the same key already exists, assign the key a new value
	 * otherwise, insert the a new Node at the appropriate 
	 * location in the binary search tree
	 */

	private V putHelper(Node node, K key, V value) {

		if(findNode(key) != null){
        	Node changeNode = findNode(key);
        	V oldValue = changeNode.value;
        	changeNode.value = value;
        	return oldValue;
        }

   		//makes the key (of type K) a comparable object 
		Comparable<? super K> k = (Comparable<? super K>) key;

		
		if (k.compareTo(node.key) < 0){
			//if the node is a leaf, we can add the new node here
			if (node.left == null){
				node.left = new Node(key,value);
				// make sure to update the size of the tree
				size++;
			//otherwise, we want to recursively check the the left side of the ree
			} else
				putHelper(node.left, key, value);
		} 

		// we do the same with the right side of the tree;
		else {
			if (node.right == null){
				node.right = new Node(key,value);
				size ++;
			} else
				putHelper(node.right,key,value);
		}

		return null;

        
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<V>();
		Deque<Node> stack = new LinkedList<Node>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			if (node == null) continue;
			set.add(node.value);
			stack.push(node.left);
			stack.push(node.right);
		}
		return set;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyTreeMap<String, Integer>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		Integer value = map.get("Word1");
		System.out.println(value);
		
		for (String key: map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	/**
	 * Makes a node.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public MyTreeMap<K, V>.Node makeNode(K key, V value) {
		return new Node(key, value);
	}

	/**
	 * Sets the instance variables.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @param node
	 * @param size
	 */
	public void setTree(Node node, int size ) {
		this.root = node;
		this.size = size;
	}

	/**
	 * Returns the height of the tree.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @return
	 */
	public int height() {
		return heightHelper(root);
	}

	private int heightHelper(Node node) {
		if (node == null) {
			return 0;
		}
		int left = heightHelper(node.left);
		int right = heightHelper(node.right);
		return Math.max(left, right) + 1;
	}
}
