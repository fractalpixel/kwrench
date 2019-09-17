package org.kwrench.collections.bag;

/*
 Borrowed from the Artemis-odb project ( https://github.com/junkdog/artemis-odb ) under the Apache 2.0 license.
 */

/**
 * A non-modifiable bag.
 * <p>
 * A bag is a set that can also hold duplicates. Also known as multiset.
 * The Bag can be accessed by index, but the index is not permanent, it can be modified e.g. when
 * other components are removed.
 * </p>
 *
 * @author Arni Arent
 *
 * @see Bag
 */
public interface ImmutableBag<E> extends Iterable<E> {


    /**
     * Returns the element at the specified position in Bag.
     * @param index index of the element to return
     * @return the element at the specified position in bag
     */
    E get(int index);

    /**
     * Returns the element at the specified position in Bag, or null if the index is out of range.
     * @param index index of the element to return
     * @return the element at the specified position in bag or null
     */
    E getOrNull(int index);

    /**
     * @return the number of elements in this bag
     */
    int size();

    /**
     * @return true if this bag contains no elements
     */
    boolean isEmpty();

    /**
     * Check if bag contains this element.
     * @return true if the bag contains this element
     */
    boolean contains(E e);

}
