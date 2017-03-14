package numberencoding;
import java.util.LinkedHashMap;


/**
 * A duplicate-free single linked hash tree
 * 
 * @author Matthias Hutter
 *
 * @param <T> abstract type of stored data
 */
public class DictTreeNode<T> {

    /**
     * Constructor, initializes child node map
     */
    public DictTreeNode () {
    	this.children = new LinkedHashMap<T, DictTreeNode<T>>();
    }
    /**
     * Adds data as child node
     * 
     * @param childData the data to be added as child node
     * @return child node
     */
    public DictTreeNode<T> addChild (T childData){
    	if (this.hasChild(childData)) //test for duplicates
    		return this.getChild(childData);
    	DictTreeNode<T> newChild = new DictTreeNode<T>();
    	this.children.put(childData, newChild); 
    	return newChild;
    }
    /**
     * Specifies that the a word ends at the current node
     */
    public void setWord (){
    	word = true;
    }
    /**
     * Checks if a word ends at the current node
     */
    public boolean isWord(){
    	return word;
    }
    /**
     * Checks if this node has child nodes
     * 
     * @return true if is has a child node
     */
    public boolean hasChildren (){
    	if (children.size() > 0) return true;
    	return false;
    }
    /**
     * Checks if this node contains a specific child node
     * 
     * @param child the node to be checked
     * @return true if it contains the child node
     */
    public boolean hasChild (T child){
    	return this.children.containsKey(child);
    }
    /**
     * @return the data stored in this node 
     */
    public T getData (){
    	return this.data;
    }
    /**
     * Returns the child for a given data
     * 
     * @param childData
     * @return node for given data, null if no such data was saved
     */
    public DictTreeNode<T> getChild(T childData){
    	return this.children.get(childData);
    }

    // Private
    private boolean word = false;
	private T data;
    private LinkedHashMap<T, DictTreeNode<T>> children;

}
