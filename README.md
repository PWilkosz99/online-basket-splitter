# Basket Splitter Library

This Java library is designed to split items in a customer's basket into delivery groups based on predefined delivery methods. It aims to minimize the number of required deliveries while maximizing the size of each delivery group.

## Features

- Efficiently splits items into delivery groups.
- Allows customization of delivery methods for each item.
- Parses configuration from a JSON file.
- Provides a simple API for integration into existing systems.

## Usage

1. **Installation**: Include the library JAR file in your project's dependencies.
   
2. **Initialization**: Create an instance of `BasketSplitter` by providing the absolute path to the configuration file.
   
   ```java
   BasketSplitter splitter = new BasketSplitter("/path/to/config.json");
   ```

3. **Splitting Items**: Use the `split` method to divide a list of items into delivery groups.

   ```java
   List<String> items = Arrays.asList("Steak (300g)", "Carrots (1kg)", "Soda (24x330ml)");
   Map<String, List<String>> deliveryGroups = splitter.split(items);
   ```

## Configuration File Format

The configuration file should be in JSON format and contain mappings of items to their possible delivery methods.

Example:

```json
{
  "Carrots (1kg)": ["Express Delivery", "Click&Collect"],
  "Cold Beer (330ml)": ["Express Delivery"],
  "Steak (300g)": ["Express Delivery", "Click&Collect"]
}
```

## Constraints

- Maximum of 1000 products in the configuration file.
- Up to 10 different delivery methods for each product.
- Maximum of 100 products in the customer's basket.


## Expected Output

The `split` method returns a map where keys represent delivery methods and values contain lists of items.

Example:

```json
{
  "Express Delivery": ["Steak (300g)", "Carrots (1kg)"],
  "Click&Collect": ["Cold Beer (330ml)"]
}
```

## Testing

Unit tests are provided to ensure the correctness of the library. Additional tests can be added as needed.

## Dependencies

This library relies on Jackson for JSON parsing.