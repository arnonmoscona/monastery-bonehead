package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.api.core.Capability;

/**
 * A silly capability that knows how to say hello
 */
public interface GreetingCapability extends Capability {
    default public String greet(String name) {
        return "Hello "+name+"!";
    }
}
