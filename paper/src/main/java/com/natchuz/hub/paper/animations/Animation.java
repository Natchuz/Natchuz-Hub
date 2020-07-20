package com.natchuz.hub.paper.animations;

/**
 * Animation is holder for different objects in time. (Like video hold many pictures)
 *
 * @param <V> An object to hold
 */
public interface Animation<V> {

    V getFrame(int tick);

    int getLength();

    int getSpacing();

}
