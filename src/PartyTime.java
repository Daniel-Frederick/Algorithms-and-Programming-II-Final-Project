import java.util.NoSuchElementException;
import java.util.Scanner;
import static java.lang.System.out;

public class PartyTime {

    private static SLND<String> guests, victuals;
    static int cmdCount = 0;    // command counter
    private static final Scanner cin = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        out.println("CPS 151 Assignment 3 by Danny Frederick and Ramon Bordelies");
        tellPurpose();
        guests = new SLND_LinkedList<>("Guest List");
        victuals = new SLND_LinkedList<>("Food and Beverage List");
        mainMenu();
        out.println("\nFinal lists:");
        out.println(guests);
        out.println(victuals);
        out.println("\nCPS 151 Assignment 3 complete");
    } // end main

    private static void mainMenu() {
        char choice = mainMenuGetChoice();

        while (choice != 'Q') {
            // TODO: call "maintain" with appropriate argument
            // or say that the choice is invalid

            if(choice == 'F'){
                maintain(victuals);
            }
            else if(choice == 'G'){
                maintain(guests);
            }
            else
                out.println("Invalid input");

            choice = mainMenuGetChoice();
        } // end loop
    } // end mainMenu

    private static char mainMenuGetChoice() {
        cmdCount++;
        out.println("\nMaintain: F)ood and beverage list, or G)uest list, " +
                "or Q)uit the program");
        out.print(cmdCount + ". Your choice (F/G/Q): ");
        return cin.nextLine().toUpperCase().charAt(0);
    } // end method

    private static void maintain(SLND<String> theList) {
        out.println("\nWorking with " + theList.name);
        char choice = subMenuGetChoice();

        while (choice != 'Q') {
            // begin try block
            // TODO: call appropriate client side method to handle user's choice
            // or say that the choice is invalid
            // end try block
            // catch block to catch the exception
            // print the message from the exception
            // end catch block

            try{ //why the try block? I don't understand what the exception is
                if(choice == 'A'){
                    add(theList);
                }
                else if(choice == 'F'){
                    find(theList);
                }
                else if(choice == 'P'){
                    theList.toString();
                }
                else if(choice == 'R'){
                    remove(theList);
                }
                else out.println("Invalid choice");
            }
            catch(Exception e)
            {
                out.println(e.getMessage());
            }
            choice = subMenuGetChoice();
        } // end loop
        out.println("Ended working with " + theList.name);
    } // end method

    private static char subMenuGetChoice() {
        cmdCount++;
        out.println("\nChoices: A)dd, F)ind, P)rint, R)emove, Q)uit");
        out.print(cmdCount + ". Your choice (A/F/P/R/Q): ");
        return cin.nextLine().toUpperCase().charAt(0);
    } // end method

    private static void add(SLND<String> theList) {
        out.print("Add to " + theList.name + "? ");
        String item = cin.nextLine();
        theList.add(item); // the add method may throw exception, handled within "maintain"

        out.println("Add succeeded");
    } // end method

    private static void find(SLND<String> theList) {
        // TODO: complete this method
        out.print("Who to find in " + theList.name + "? ");
        String item = cin.nextLine();
        if (theList.find(item)) {
            out.print(item + " is in the list\n");
        } else {
            out.print(item + " is not in the list\n");
        }

    } // end method

    private static void remove(SLND<String> theList) {
        // TODO: complete this method
        out.print("Who to remove in " + theList.name + "? ");
        String item = cin.nextLine();
        theList.remove(item);
        out.print("Item removed\n");
    } // end method

    private static void tellPurpose() {
        out.println("Party Planner: This program lets you maintain" +
                "\n\ta guest list and a food/beverage list");
    } // end method

} // end class


//TODO:
// ----------------- abstract generic class SLND (Sorted List No Duplicates)
// No changes needed here
abstract class SLND<E extends Comparable<E>> {

    public final String name;

    public SLND(String name) {
        this.name = name;
    }

    public abstract void add(E item);

    public abstract void remove(E item);

    public abstract boolean find(E item);

    public abstract String toString();
} // end class

//TODO:
// ----------------- generic class Node (used to create singly linked list)
// No changes needed here
// The fields (instance variables) are public
class Node<T> {

    public T data;
    public Node next;

    // Constructor 0
    public Node() {
        this(null);
    }

    // Constructor 1
    public Node(T data) {
        this(data, null); // use Constructor 2 specifying both instance variables
    }

    // Constructor 2
    public Node(T data, Node next) {
        this.data = data;
        this.next = next;
    }
} // end class


//TODO:
// ----------------- generic class SLND_LinkedList
//this class has the add, remove, find, and toString methods for the linkedList being involved with both the Guest List and the Food and Bevarage List
class SLND_LinkedList<E extends Comparable<E>> extends SLND<E> {

    // fields of Node are public, but making head private does data hiding
    private Node<E> head;

    public SLND_LinkedList(String name) {
        super(name);
        head = null;
    }    // Constructor

    public void add(E item) throws IllegalStateException {
        Node<E> cur = head, prev = null;

        if (head == null) {
            head = new Node(item, head);
            return; // exit early since this is the only node in the list
        }

        // Find the (possible) insertion point.
        while (cur != null && item.compareTo(cur.data) > 0) {
            prev = cur;
            cur = cur.next;
        } // end while

        // If duplicate item, throw IllegalStateException
        if (cur != null && item.equals(cur.data)) {
            throw new IllegalStateException("No duplicate items!");
        }

        // Insert the new node just before cur. May need to modify head.
        if (prev != null) {
            prev.next = new Node(item, cur);
        } else {
            head = new Node(item, cur);
        }
    } // end method



    public void remove(E item) throws NoSuchElementException {
        // Declare local variables needed
        Node<E> prev = null, cur = this.head;

        // Check if the item is in the list
        if (find(item) == false) {
            throw new NoSuchElementException("Item not found");
        }

        // Traverse the list
        while (cur != null) {
            // If the data at cur does not match value, move on
            if (!cur.data.equals(item)) {
                prev = cur;
                cur = cur.next;
            } else { // node at cur holds the value, remove it
                if (prev == null) { // removing the first node in the list
                    this.head = cur.next;
                } else { // delink node with reference cur
                    prev.next = cur.next;
                }
                break;
            } // end if
        } // end loop
    } // end method remove

    // if item cannot be located, throw NoSuchElementException
    // remove the item, may have to modify head
    // end method

    public boolean find(E item) {
        // TODO: complete this method, may need local variables

        // if match found return true
        Node cur = head;
        while (cur != null) {
            if (cur.data.equals(item))
                return true;

            // did not find a match yet, so move on
            cur = cur.next;
        } // end loop

        // reached end of list without finding a match, so
        return false;
    } // end method

    public String toString() {
        // TODO: complete this method, may need local variables
        Node cur = head;
        out.println(name);
        for(int i=0;i<name.length();i++){
            out.print("-");
        }
        out.println();
        if(head == null){
            return "Empty List\n";
        }

        while(cur != null){
            out.println(cur.data);
            cur = cur.next;
        }
        return ""; // stub code remove when method completed
    } // end method


    // add any other private methods you want

} // end class