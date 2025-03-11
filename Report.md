# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.
   ```java
   // your code here
   String str1 = new String("Java");
   String str2 = new String("Java");
   System.out.println(str1 == str2); // false, compares references
   System.out.println(str1.equals(str2)); // true, compares contents
   ```



2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner?
   
   For me, I lowercased the string first, then do the sort.



3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
    Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point.
 
   The order matters because a longer operator string (like “>=”) contains the shorter one (”>”). If you check for the shorter operator first, it will match even when the intended operator is the longer one. This can lead to incorrect behavior.


4. What is the difference between a List and a Set in Java? When would you use one over the other? 

   A List is an ordered collection that allows duplicate elements.
   A Set is an unordered collection that does not allow duplicates.



5. In [GamesLoader.java](src/main/java/student/GamesLoader.java), we use a Map to help figure out the columns. What is a map? Why would we use a Map here?
   
   We use a Map<GameData, Integer> to link each column with its index in the CSV header. This approach makes our CSV parsing order-independent because even if the columns appear in a different order in the file, we can still find the correct index for each attribute by its name.


6. [GameData.java](src/main/java/student/GameData.java) is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?

   An enum in Java is a special data type that defines a fixed set of constants.

   The enum ensures that references to a CSV column are consistent across the codebase.
   We also ensure methods like fromColumnName allow you to convert the raw CSV header into meaningful enum constants.
   If the CSV format changes, you only need to update the enum, rather than hunting down every occurrence of a column name in your code.


7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ``` 

    ```java
    // your code here, don't forget the class name that is dropped in the switch block..
    
   if (ct == CMD_QUESTION || ct == CMD_HELP) {
    processHelp();
   } else {
   CONSOLE.printf("%s%n", ConsoleText.INVALID);
   }
    ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block. 

```text
// your consoles output here
welcome=Welcome to the Console App!
menu.option=Please select an option:
```
to 
```
welcome=Bienvenido a la Aplicación de Consola!
menu.option=Por favor, seleccione una opción:
```
After making these changes, a sample console run might look like this:
```
Bienvenido a la Aplicación de Consola!
Por favor, seleccione una opción:
1. Opción Uno
2. Opción Dos
3. Salir

```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?


As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry. 

Localization is critical not only for expanding market share but also for enhancing user experience across different languages and cultures. By externalizing all display strings into resource files (like .properties files in Java), developers can easily adapt their applications to various languages without changing the codebase. This separation of code and content allows for flexibility in translation and ensures consistency across locales. According to research on globalization and internationalization (see, for example, IBM’s Globalization Guidelines and various studies on software usability), providing localized interfaces helps in reaching wider audiences and increasing user satisfaction. Moreover, poor localization can lead to misinterpretation or even cultural insensitivity—cautionary examples often cited (though sometimes exaggerated, as with the infamous car name misadventures in Mexico) illustrate the importance of accurate translation and context. As developers, to reduce such “hiccups,” it’s important to use established internationalization frameworks, adhere to Unicode standards, involve native speakers during testing, and design layouts that can accommodate text expansion. This approach not only minimizes errors but also ensures the application resonates well with a global audience.

References:
•  IBM Globalization Guidelines：https://www.ibm.com/docs/en/i/7.5?topic=globalization-checklists
•  Oracle Java Internationalization documentation：https://docs.oracle.com/javase/8/docs/technotes/guides/intl/index.html