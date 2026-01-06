package com.visualizer.instrumentation.core;

/**
 * Enum of all possible step actions for visualization
 */
public enum StepAction {
    // Array operations
    ARRAY_INIT,
    ARRAY_GET,
    ARRAY_SET,
    ARRAY_SWAP,
    ARRAY_HIGHLIGHT,
    POINTER_MOVE,

    // Map operations
    MAP_INIT,
    MAP_PUT,
    MAP_GET,
    MAP_CONTAINS,
    MAP_REMOVE,

    // Set operations
    SET_ADD,
    SET_CONTAINS,
    SET_REMOVE,

    // Stack operations
    STACK_PUSH,
    STACK_POP,
    STACK_PEEK,

    // Queue operations
    QUEUE_OFFER,
    QUEUE_POLL,

    // Variable operations
    VAR_INIT,
    VAR_UPDATE,

    // Comparison
    COMPARE,

    // Result
    RESULT,

    // Error
    ERROR
}
