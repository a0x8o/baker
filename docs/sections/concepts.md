# Concepts

Baker introduces *interactions*, *ingredients*, and *events* as a model of abstracting.

With these three components we can create recipes (process blue prints)

## Ingredient

Ingredients are *pure data*.

This data is **immutable** and can not be changed after entering the process.

There is **no hierarchy** in this data. (`Animal -> Dog -> Labrador` is not possible to express)

Examples:

- an IBAN
- a track and trace code
- a list of phone numbers
- a customer information object with name, email, etc ...

An ingredient is defined by a *name* and *type*.

The *name* points to the intended meaning of the data. ("customerData", "orderNumber", ...)

The *type* sets limits on the form of data that is accepted. (a number, a list of strings, ...)

This type is expressed by the [Baker type system](../reference/baker-types-and-values/).

## Interaction

An interaction is similar to a function.

It requires *input* ([ingredients](../reference/main-abstractions/#ingredient-and-ingredientinstance)) and
provides *output* ([events](../reference/main-abstractions/#event-and-eventinstance)).

Within this contract it may do anything. For example:

- query an external system
- put a message on a bus
- generate a document or image
- extract or compose ingredients into others

When finished, an interaction provides an event as its output.

### Interaction failure

An interaction may fail to fulfill its intended purpose.

We distinquish two types of failures.

1. A *technical* failure is one that could be retried and succeed. For example:
    * Time outs because of an unreliable network or packet loss
    * External system is temporarily down or unresponsive
    * External system returned a malformed/unexpected response

    These failures are unexpected and are modeled by throwing an exception from the interaction.

2. A *functional* failure is one that cannot be retried. For example:
    * The customer is too young for the request.
    * Not enough credit to perform the transfer.

    These failures are expected possible outcomes of the interaction. They are modelled by returning an event from the interaction.

### Failure mitigation

In case of technical failures, baker offers two mitigation strategies:

1. Retry with incremental back-off

    This retries the interaction with some configurable parameters:

    - `initialTimeout`: The initial delay for the first retry.
    - `backoffFactor`: The back-off factor.
    - `maximumInterval`: The maximum interval between retries.

2. Continue with an event.

    This is analagous to a try/catch in Java code. The exception is logged but the process continues with a specified event.

The interaction gets *blocked* when no failure strategy is defined for it.

## Event

An event has a *name* and can (optionally) provide ingredients.

The purpose of events is therefore twofold.

1. It signifies that something of interest has happened for a [recipe instance](../reference/main-abstractions/#recipe-and-recipeinstance).

    Example, *"the customer placed the order"*, *"terms and conditions were accepted"*

2. The event may provide ingredients required to continue the process.

    Example, *"OrderPlaced"* -> `<list of products>`

We distinguish two conceptual types of events.

1. Sensory events (*external*)

    These events are provided from outside of the process.

2. Interaction output (*internal*)

    These events are a result of an interaction being executed.
    
Both of these are still just instances of the `EventInstance` class, and the distinction is only used as practical terms.

## **Recipe**

*Events*, *Interactions* and *Ingredients* can be composed into recipes.

Recipes are similar to process blueprints.

Baker provides a [recipe DSL](../reference/dsls/) in which you can declaratively describe your recipe.

A small example:
``` java
new Recipe("webshop")
    .withSensoryEvents(
        OrderPlaced.class,
        CustomerInfoReceived.class
    .withInteractions(
        of(ValidateOrder.class),
        of(ManufactureGoods.class));
```

The main take away is that when declaring your recipe you do not have to think about order.

Everything is automatically linked by the *data* requirements of the interactions.

## Continuing from here

After adding the dependencies you can continue to:

* Go through the [development life cycle section](../development-life-cycle/design-a-recipe) if you like learning by doing;
* Go through the [reference section](../reference/main-abstractions) if you like learning by description.
