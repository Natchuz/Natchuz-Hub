package com.natchuz.hub.paper.animations;

import java.util.LinkedList;
import java.util.List;

public class BasicAnimation<V> implements Animation<V> {

    private final List<V> elements;
    private int spacing;

    private BasicAnimation() {
        elements = new LinkedList<>();
    }

    @Override
    public V getFrame(int tick) {
        return elements.get(tick);
    }

    @Override
    public int getLength() {
        return elements.size();
    }

    @Override
    public int getSpacing() {
        return spacing;
    }

    /**
     * Initializes new empty builder
     *
     * @param spacing spacing (in milliseconds) between ticks
     * @param <V>     target object of animation
     */
    public static <V> BasicAnimationBuilder<V> builder(Class<V> vClass, int spacing) {
        return new BasicAnimationBuilder<>(spacing);
    }

    public static class BasicAnimationBuilder<V> {

        private final BasicAnimation<V> animation;

        public BasicAnimationBuilder(int spacing) {
            animation = new BasicAnimation<>();
            animation.spacing = spacing;
        }

        public BasicAnimationBuilder<V> appendFrames(int times, V element) {
            for (int i = 0; i < times; i++)
                animation.elements.add(element);
            return this;
        }

        public BasicAnimation<V> build() {
            return animation;
        }

    }
}
